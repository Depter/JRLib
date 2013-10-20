/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.gui.data.api.wizard;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.NamedDataSourceProvider;
import org.jreserve.gui.data.dataobject.DataEventUtil;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.jreserve.gui.data.dataobject.DataSourceUtil;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.modules.project.uiapi.ProjectChooserFactory;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - current",
    "# {1} - total",
    "LBL.AbstractDataSourceWizardIterator.Name={0} of {1}"
})
public abstract class AbstractDataSourceWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {

    public static boolean canInitialize() {
        return canInitialize(Utilities.actionsGlobalContext());
    }
    
    public static boolean canInitialize(Lookup context) {
        Project p = context.lookup(Project.class);
        return p!=null &&
               p.getLookup().lookup(NamedDataSourceProvider.class) != null;
    }
    
    public final static String PROP_PROJECT = "dataSource.Project";
    public final static String PROP_OBJECT_PROVIDER = "dataSource.ObjectProject";
    public final static String PROP_FOLDER = "dataSource.Folder";
    
    public final static String PROP_PATH = "dataSource.Path";
    public final static String PROP_DATA_TYPE = "dataSource.DataType";
    public final static String PROP_EXTRA_PROPERTIES = "dataSource.ExtraProperties";
    public final static String PROP_FACTORY_ID = "dataSource.FactoryId";
    
    private final static Logger logger = Logger.getLogger(AbstractDataSourceWizardIterator.class.getName());
    
    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel> panels;
    private int index;
    private int panelCount;
        
    public void initializeFrom(Lookup lkp) {
        Project p = lkp.lookup(Project.class);
        NamedDataSourceProvider dsop = p.getLookup().lookup(NamedDataSourceProvider.class);
        if(dsop != null) {
            wizard.putProperty(PROP_OBJECT_PROVIDER, dsop);
            wizard.putProperty(PROP_PROJECT, p);
        }
        
        DataFolder folder = lkp.lookup(DataFolder.class);
        if(folder != null)
            wizard.putProperty(PROP_PATH, lkp);
    }
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        initializeProperties();
        initializePanels();
    }
    
    private void initializeProperties() {
        Project p = (Project) wizard.getProperty(ProjectChooserFactory.WIZARD_KEY_PROJECT);
        if(p != null) {
            wizard.putProperty(PROP_PROJECT, p);
            wizard.putProperty(PROP_OBJECT_PROVIDER, p.getLookup().lookup(NamedDataSourceProvider.class));
        }
        
        DataFolder folder = (DataFolder) wizard.getProperty(ProjectChooserFactory.WIZARD_KEY_TARGET_FOLDER);
        if(folder == null)
            folder = Utilities.actionsGlobalContext().lookup(DataFolder.class);
        if(folder != null)
            wizard.putProperty(PROP_FOLDER, folder);
    }
    
    private void initializePanels() {
        panels = new ArrayList<WizardDescriptor.Panel>(createPanels());
        panelCount = panels.size();
        calculateState();
    }
    
    protected List<WizardDescriptor.Panel> createPanels() {
        return Collections.singletonList((WizardDescriptor.Panel) new DataSourceNameWizardPanel());
    }
    
    private void calculateState() {
        String[] steps = initSteps();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
        index = 0;
    }
    
    private String[] initSteps() {
        String[] steps = new String[panelCount];
        for(int i=0; i<panelCount; i++) {
            Component c = panels.get(i).getComponent();
            steps[i] = c.getName();
            initPanel(i, steps, c);
        }
        return steps;
    }
    
    private void initPanel(int i, String[] steps, Component c) {
        if(c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(i));
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
        }
    }    
    
    @Override
    public void uninitialize(WizardDescriptor wizard) {
        this.wizard = null;
        this.panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels.get(index);
    }

    @Override
    public String name() {
        return Bundle.LBL_AbstractDataSourceWizardIterator_Name(index+1, panels.size());
    }

    @Override
    public boolean hasNext() {
        return index < (panels.size()-1);
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if(!hasNext())
            throw new NoSuchElementException();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(++index));
    }

    @Override
    public void previousPanel() {
        if(!hasPrevious())
            throw new NoSuchElementException();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(--index));
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set instantiate(ProgressHandle handle) throws IOException {
        handle.start();
        handle.switchToIndeterminate();
        try {
            FileBuilder builder = new FileBuilder();
            FileUtil.runAtomicAction(builder);
            
            FileObject result = builder.getResult();
            DataSourceDataObject ds = (DataSourceDataObject) DataObject.find(result);
            DataEventUtil.fireCreated(ds);
            return Collections.singleton(result);
        } catch(IOException ex) {
            String msg = "Unable to create new DataSource!";
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            handle.finish();
        }
    }
    
    private class FileBuilder implements Runnable {
        
        private FileObject result;
        private IOException exception;
        
        @Override
        public void run() {
            try {
                result = createPrimaryFile();
                Properties props = createProperties();
                DataSourceUtil.saveProperties(result, props);
                createSecondaryFiles(result);
            } catch (IOException ex) {
                exception = ex;
            }
        }
    
        private FileObject createPrimaryFile() throws IOException {
            String path = getPath();
            NamedDataSourceProvider dop = (NamedDataSourceProvider) wizard.getProperty(PROP_OBJECT_PROVIDER);
            FileObject root = dop.getRootFolder();
//            if(!path.startsWith(root.getName()+"/")) {
//                String msg = "Path '%s' does not start with '%s'/!";
//                throw new IOException(String.format(msg, path, root.getName()));
//            }
//
//            FileObject parent = root.getParent();
            return FileUtil.createData(root, path);
        }
    
        private String getPath() throws IOException {
            String path = (String) wizard.getProperty(PROP_PATH);
            if(path == null)
                throw new IOException("DataSoruce name not set!");
            return path + "." + DataSourceDataObject.EXTENSION;
        }
    
        private Properties createProperties() throws IOException {
            Properties props = new Properties();
            props.setProperty(DataSourceDataObject.PROP_AUDIT_ID, getAuditId());
            props.setProperty(DataSourceDataObject.PROP_DATA_TYPE, getDataType());
            props.setProperty(DataSourceDataObject.PROP_FACTORY_ID, getFactoryId());
            addProviderProperties(props);
            return props;
        }

        private String getAuditId() {
            Project project = (Project) wizard.getProperty(PROP_PROJECT);
            return ""+AuditDbManager.getInstance().getNextObjectId(project);
        }

        private String getDataType() throws IOException {
            DataType dataType = (DataType) wizard.getProperty(PROP_DATA_TYPE);
            if(dataType == null)
                throw new IOException("DataType not set!");
            return dataType.name();
        }

        private String getFactoryId() throws IOException {
            String factoryId = (String) wizard.getProperty(PROP_FACTORY_ID);
            if(factoryId == null || factoryId.length()==0)
                throw new IOException("FactoryId not set!");
            return factoryId;
        }

        private void addProviderProperties(Properties props) {
            Properties extraProps = (Properties) wizard.getProperty(PROP_EXTRA_PROPERTIES);
            if(extraProps == null)
                return;

            for(String name : extraProps.stringPropertyNames()) {
                if(isProtectedProperty(name)) {
                    logger.log(Level.WARNING, "Property ''{0}'' can not be overwritten!", name);
                } else {
                    String value = extraProps.getProperty(name);
                    if(value != null)
                        props.setProperty(name, value);
                }
            }
        }

        private boolean isProtectedProperty(String name) {
            return DataSourceDataObject.PROP_AUDIT_ID.equals(name) ||
                   DataSourceDataObject.PROP_DATA_TYPE.equals(name) ||
                   DataSourceDataObject.PROP_FACTORY_ID.equals(name);
        }
       
        FileObject getResult() throws IOException {
            if(exception != null)
                throw exception;
            return result;
        }
    }
     
    protected abstract void createSecondaryFiles(FileObject primaryFile) throws IOException;
}
