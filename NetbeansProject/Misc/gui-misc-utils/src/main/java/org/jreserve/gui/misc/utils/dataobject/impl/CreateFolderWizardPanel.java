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
import java.util.Collections;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CreateFolderWizardPanel.ProjectEmpty=Project not selected!",
    "MSG.CreateFolderWizardPanel.ProviderEmpty=Location not selected!",
    "MSG.CreateFolderWizardPanel.NameEmpty=Name is not selected!",
    "MSG.CreateFolderWizardPanel.FolderExists=Folder already exists!"
})
class CreateFolderWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private final static String PROP_PROJECT = "project";
    
    private WizardDescriptor wiz;
    private ChangeSupport cs = new ChangeSupport(this);
    private boolean isValid;
    private CreateFolderVisualPanel panel;
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new CreateFolderVisualPanel(this);
            if(wiz != null)
                initPanel();
        }
        return panel;
    }
    
    private void initPanel() {
        Project project = (Project) wiz.getProperty(PROP_PROJECT);
        panel.setProviders(project==null? Collections.EMPTY_LIST :
                project.getLookup().lookupAll(DataObjectProvider.class));
        
        DataObjectProvider provider = Utilities.actionsGlobalContext().lookup(DataObjectProvider.class);
        panel.setSelectedProvider(provider);
    }
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(panel != null)
            initPanel();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(CreateDataFolderIterator.PROP_PATH, panel.getPath());
        settings.putProperty(CreateDataFolderIterator.PROP_PROVIDER, panel.getProvider());
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }
    
    void panelChanged() {
        isValid = isInputValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return isProviderValid() && isNameValid() && isPathValid();
    }
    
    private boolean isProviderValid() {
        if(panel.getProvider() == null) {
            showError(Bundle.MSG_CreateFolderWizardPanel_ProviderEmpty());
            return false;
        }
        return true;
    }
    
    private boolean isNameValid() {
        String name = panel.getFolderName();
        if(name == null || name.length()==0) {
            showError(Bundle.MSG_CreateFolderWizardPanel_NameEmpty());
            return false;
        }
        return true;
    }
    
    private boolean isPathValid() {
        String path = panel.getPath();
        DataObjectProvider provider = panel.getProvider();
        FileObject root = provider.getRootFolder().getPrimaryFile();
        FileObject child = root.getFileObject(path);
        if(child != null && child.isFolder()) {
            showError(Bundle.MSG_CreateFolderWizardPanel_FolderExists());
            return false;
        }
        return true;
    }
}

