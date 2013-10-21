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
package org.jreserve.gui.misc.namedcontent.folder;

import java.awt.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.misc.namedcontent.NamedContentProvider;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@TemplateRegistration(
    folder = "Other",
    displayName = "#LBL.CreateFolderIterator.DisplayName",
    iconBase = "org/jreserve/gui/misc/utils/folder.png",
    id = "org.jreserve.gui.misc.namedcontent.folder.CreateFolderIterator",
    category = {"misc-folder"},
    position = 100
)
@Messages({
    "LBL.CreateFolderIterator.DisplayName=Folder",
    "# {0} - current",
    "# {1} - total",
    "LBL.CreateFolderIterator.Name={0} of {1}"
})
public class CreateFolderIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {
    
    final static String PROP_PROJECT = "project";
    final static String PROP_OBJECT_PROVIDER = "dataObjectProvider";
    final static String PROP_FOLDER = "initialDataFolder";
    
    final static String PROP_PATH = "newFolderPath";
    final static String PROP_PROVIDER = "objectProvider";
    
    private final static Logger logger = Logger.getLogger(CreateFolderIterator.class.getName());
    
    private CreateFolderWizardPanel panel;
    private WizardDescriptor wizard;
    private Project initialProject;
    private NamedContentProvider dop;
    
    public CreateFolderIterator() {
    }
    
    public CreateFolderIterator(NamedContentProvider dop) {
        this.dop = dop;
        this.initialProject = getProjectFromDOP(dop);
    }
    
    private Project getProjectFromDOP(NamedContentProvider dop) {
        if(dop == null) return null;
        FileObject root = dop.getRoot();
        if(root == null) return null;
        return FileOwnerQuery.getOwner(root);
    }
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        initializeProperties();
        initializePanels();
    }
    
    private void initializeProperties() {
        Project p = (Project) wizard.getProperty(PROP_PROJECT);
        if(p == null && initialProject != null)
            wizard.putProperty(PROP_PROJECT, initialProject);
        
        if(dop != null)
            wizard.putProperty(PROP_OBJECT_PROVIDER, dop);
        DataFolder folder = Utilities.actionsGlobalContext().lookup(DataFolder.class);
        if(folder != null)
            wizard.putProperty(PROP_FOLDER, folder);
    }
    
    private void initializePanels() {
        panel = new CreateFolderWizardPanel();
        Component c = panel.getComponent();
        String[] steps = new String[]{c.getName()};
        initPanel(steps, c);
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
    }
    
    private void initPanel(String[] steps, Component c) {
        if(c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(0));
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
        }
    }    

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panel = null;
        this.wizard = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panel;
    }

    @Override
    public String name() {
        return Bundle.LBL_CreateFolderIterator_Name(1, 1);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public void nextPanel() {
        throw new NoSuchElementException();
    }

    @Override
    public void previousPanel() {
        throw new NoSuchElementException();
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
        
        String path = (String) wizard.getProperty(PROP_PATH);
        NamedContentProvider provider = (NamedContentProvider) wizard.getProperty(PROP_PROVIDER);
        Project project = (Project) wizard.getProperty(PROP_PROJECT);
        try {
            FileObject folder = createFolder(provider.getRoot(), path);
            return Collections.singleton(folder);
        } catch(Exception ex) {
            String msg = "Unable to create new folder %s/%s in project %s!";
            msg = String.format(msg, provider.getRoot().getName(), path, project.getProjectDirectory().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            handle.finish();
        }
    }
    
    private FileObject createFolder(FileObject parent, String path) throws IOException {
        if(path.length()>0 && path.charAt(0) == '/')
            path = path.substring(1);
        return FileUtil.createFolder(parent, path);
    }
}