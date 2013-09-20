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

package org.jreserve.gui.calculations.triangle.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.calculations.CalculationObjectProvider;
import org.jreserve.gui.calculations.triangle.ClaimTriangleDataObject;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
import org.jreserve.gui.misc.utils.wizard.AbstractWizardIterator;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.templates.support.Templates;
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
@TemplateRegistration(
    folder = "Calculation",
    displayName = "#LBL.CreateClaimTriangleWizardIterator.DisplayName",
    iconBase = "org/jreserve/gui/calculations/icons/triangle.png",
//    id = "org.jreserve.gui.calculations.triangle.wizard.CreateClaimTriangleWizardIterator",
    category = {"jreserve-calculation"},
    position = 100,
    content = "ClaimTriangleTemplate.jct",
    scriptEngine = "freemarker"
)
@Messages({
    "LBL.CreateClaimTriangleWizardIterator.DisplayName=Claim Triangle",
    "# {0} - current",
    "# {1} - total",
    "LBL.CreateClaimTriangleWizardIterator.Name={0} of {1}"
})
public class CreateClaimTriangleWizardIterator extends AbstractWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {
    
    private final static String TEMPLATE_FILE = "Templates/Calculation/ClaimTriangleTemplate.jct"; //NOI18
    
    final static String PROP_PROJECT = "project";
    final static String PROP_INIT_FOLDER = "initFolder";
    
    final static String PROP_FOLDER = "selectedFolder";
    final static String PROP_NAME = "selectedName";
    final static String PROP_OBJECT_PROVIDER = "objectProvider";
    final static String PROP_GEOMETRY = "triangleGeometry";
    final static String PROP_DATA_SOURCE = "dataSource";
    
    private final static Logger logger = Logger.getLogger(CreateClaimTriangleWizardIterator.class.getName());
    
    private Project project;
    
    public CreateClaimTriangleWizardIterator() {
        this(null);
    }
    
    CreateClaimTriangleWizardIterator(Project project) {
        this.project = project;
    }
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        super.setWizardDescriptor(wizard);
        initializeProperties(wizard);
        createPanels();
    }
    
    private void initializeProperties(WizardDescriptor wizard) {
        Lookup lkp = Utilities.actionsGlobalContext();
        
        Project p = (Project) wizard.getProperty(PROP_PROJECT);
        if(p == null) {
            if(project != null) {
                wizard.putProperty(PROP_PROJECT, project);
            } else {
                Collection<? extends Project> ps = lkp.lookupAll(Project.class);
                if(ps.size() == 1)
                    wizard.putProperty(PROP_PROJECT, ps.iterator().next());
            }
        }
        
        wizard.putProperty(PROP_INIT_FOLDER, lkp.lookup(DataFolder.class));
    }
    
    private void createPanels() {
        List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>(2);
        panels.add(new CreateClaimTriangleWizardPanel1());
        panels.add(new CreateClaimTriangleWizardPanel2());
        super.setPanels(panels);
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        super.setWizardDescriptor(null);
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
            return Collections.singleton(buildClaimTriangle());
        } catch(IOException ex) {
            String msg = "Unable to create new ClaimTriangle!";
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            handle.finish();
        }
    }
    
    private DataObject buildClaimTriangle() throws IOException {
        DataObject template = getTemplateObject();
        DataFolder folder = getParentFolder();
        String name = getFileName();
        Map parameters = getParameters();
        return template.createFromTemplate(folder, name, parameters);
    }
    
    private DataObject getTemplateObject() throws IOException {
        WizardDescriptor wizard = super.getWizardDescriptor();
        FileObject template = Templates.getTemplate(wizard);
        if(template == null)
            template = FileUtil.getConfigFile(TEMPLATE_FILE);
        return DataObject.find(template);
    }
    
    private DataFolder getParentFolder() throws IOException {
        WizardDescriptor wizard = super.getWizardDescriptor();
        
        CalculationObjectProvider cop = (CalculationObjectProvider) wizard.getProperty(PROP_OBJECT_PROVIDER);
        FileObject root = cop.getRootFolder().getPrimaryFile();
        String folderPath = (String) wizard.getProperty(PROP_FOLDER);
        FileObject folder = FileUtil.createData(root, folderPath);
        return (DataFolder) DataObject.find(folder);
    }
    
    private String getFileName() {
        WizardDescriptor wizard = super.getWizardDescriptor();
        return (String) wizard.getProperty(PROP_NAME);
    }
    
    private Map getParameters() {
        WizardDescriptor wizard = super.getWizardDescriptor();
        
        Project p = (Project) wizard.getProperty(PROP_PROJECT);
        long auditId = AuditDbManager.getInstance().getNextObjectId(p);
        DataSource ds = (DataSource) wizard.getProperty(PROP_DATA_SOURCE);
        TriangleGeometry geometry = (TriangleGeometry) wizard.getProperty(PROP_GEOMETRY);
        
        Map params = new HashMap();
        params.put("auditId", auditId);
        params.put("dataSource", ds.getPath());
        params.put("startDate", geometry.getStartDate().toString());
        params.put("endDate", geometry.getEndDate().toString());
        params.put("accidentLength", geometry.getAccidentLength());
        params.put("developmentLength", geometry.getDevelopmentLength());
        return params;
    }
    
//    private DataObject buildClaimTriangle() throws IOException {
//        WizardDescriptor wizard = super.getWizardDescriptor();
//        Element root = createXml(wizard);
//        
//        final FileObject file = createFile(wizard);
//        DataObject obj;
//        synchronized(file) {
//            JDomUtil.save(file, root);
//            obj = DataObject.find(file);
//            CalculationEventUtil.fireCreated(obj);
//        }
//        
//        return obj;
//    }
//    
//    private FileObject createFile(WizardDescriptor wizard) throws IOException {
//        String path = (String) wizard.getProperty(PROP_FOLDER);
//        if(!path.endsWith("/"))
//            path += "/";
//        path += (String) wizard.getProperty(PROP_NAME);
//        path += "."+ClaimTriangleDataObject.EXTENSION;
//        
//        CalculationObjectProvider cop = (CalculationObjectProvider) wizard.getProperty(PROP_OBJECT_PROVIDER);
//        FileObject root = cop.getRootFolder().getPrimaryFile();
//        return FileUtil.createData(root, path);
//    }
//    
//    private Element createXml(WizardDescriptor wizard) {
//        Project p = (Project) wizard.getProperty(PROP_PROJECT);
//        long auditId = AuditDbManager.getInstance().getNextObjectId(p);
//        DataSource ds = (DataSource) wizard.getProperty(PROP_DATA_SOURCE);
//        TriangleGeometry geometry = (TriangleGeometry) wizard.getProperty(PROP_GEOMETRY);
//        return new ClaimTriangleCalculation(p, ds, geometry).toXml();
//    }
}
