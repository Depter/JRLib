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

import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

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
    
    final static String PROP_PATH = "newFolderPath";
    final static String PROP_PROVIDER = "objectProvider";
    
    private final static Logger logger = Logger.getLogger(CreateDataFolderIterator.class.getName());
    
    private CreateFolderWizardPanel panel;
    private WizardDescriptor wizardDesc;
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        panel = new CreateFolderWizardPanel();
        this.wizardDesc = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panel = null;
        this.wizardDesc = null;
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
        
        String path = (String) wizardDesc.getProperty(PROP_PATH);
        DataObjectProvider provider = (DataObjectProvider) wizardDesc.getProperty(PROP_PROVIDER);
        Project project = (Project) wizardDesc.getProperty("project");  //NOI18
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
