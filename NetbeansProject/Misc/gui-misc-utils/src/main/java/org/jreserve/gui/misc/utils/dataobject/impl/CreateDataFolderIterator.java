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
package org.jreserve.gui.misc.utils.dataobject.impl;

import java.awt.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
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
    displayName = "#LBL.CreateDataFolderIterator.DisplayName",
    iconBase = "org/jreserve/gui/misc/utils/folder.png",
    id = "org.jreserve.gui.project.api.util.CreateTriangleIterator",
    category = {"misc-folder"},
    position = 100
)
@Messages({
    "LBL.CreateDataFolderIterator.DisplayName=Folder",
    "# {0} - current",
    "# {1} - total",
    "LBL.CreateDataFolderIterator.Name={0} of {1}"
})
public class CreateDataFolderIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {
    
    final static String PROP_PROJECT = "project";
    final static String PROP_OBJECT_PROVIDER = "dataObjectProvider";
    final static String PROP_FOLDER = "initialDataFolder";
    
    final static String PROP_PATH = "newFolderPath";
    final static String PROP_PROVIDER = "objectProvider";
    
    private final static Logger logger = Logger.getLogger(CreateDataFolderIterator.class.getName());
    
    private CreateFolderWizardPanel panel;
    private WizardDescriptor wizard;
    private Project initialProject;
    private DataObjectProvider dop;
    
    public CreateDataFolderIterator() {
    }
    
    public CreateDataFolderIterator(DataObjectProvider dop) {
        this.dop = dop;
        this.initialProject = getProjectFromDOP(dop);
    }
    
    private Project getProjectFromDOP(DataObjectProvider dop) {
        if(dop == null) return null;
        DataFolder root = dop.getRootFolder();
        if(root == null) return null;
        return FileOwnerQuery.getOwner(root.getPrimaryFile());
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
        return Bundle.LBL_CreateDataFolderIterator_Name(1, 1);
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
        DataObjectProvider provider = (DataObjectProvider) wizard.getProperty(PROP_PROVIDER);
        Project project = (Project) wizard.getProperty("project");  //NOI18
        try {
            return Collections.singleton(provider.createFolder(path));
        } catch(Exception ex) {
            String msg = "Unable to create new folder %s/%s in project %s!";
            msg = String.format(msg, provider.getRootFolder().getName(), path, project.getProjectDirectory().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            handle.finish();
        }
    }
}
