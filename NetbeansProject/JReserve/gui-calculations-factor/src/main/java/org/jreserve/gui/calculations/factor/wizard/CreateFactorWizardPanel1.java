/*
 * Copyright (C) 2013, Peter Decsi.
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jreserve.gui.calculations.factor.wizard;

import org.jreserve.gui.calculations.api.NamedCalculationProvider;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
import org.jreserve.gui.misc.utils.wizard.AbstractWizardPanel;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CreateFactorWizardPanel1.NoCOP=Project does not have calculations.",
    "MSG.CreateFactorWizardPanel1.Name.Empty=Field 'Name' is empty!",
    "MSG.CreateFactorWizardPanel1.File.Exists=File already exists!"
})
class CreateFactorWizardPanel1 extends AbstractWizardPanel<CreateFactorVisualPanel1> {

    @Override
    protected CreateFactorVisualPanel1 createComponent() {
        return new CreateFactorVisualPanel1(this);
    }

    @Override
    protected void initComponent() {
        Project project = (Project) wiz.getProperty(CreateFactorWizardIterator.PROP_PROJECT);
        panel.setProject(project);
        
        DataFolder folder = (DataFolder) wiz.getProperty(CreateFactorWizardIterator.PROP_INIT_FOLDER);
        panel.setFolder(folder);
    }
    
    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(CreateFactorWizardIterator.PROP_NAME, panel.getCalculationName());
        settings.putProperty(CreateFactorWizardIterator.PROP_FOLDER, panel.getCalculationFolder());
    }
    
    @Override
    protected boolean isInputValid() {
        return isCopValid() && isNameValid() && isPathValid();
    }
    
    private boolean isCopValid() {
        if(panel.getObjectProvider() == null) {
            showError(Bundle.MSG_CreateFactorWizardPanel1_NoCOP());
            return false;
        }
        return true;
    }
    
    private boolean isNameValid() {
        String name = panel.getCalculationName();
        if(name == null || name.length() == 0) {
            showError(Bundle.MSG_CreateFactorWizardPanel1_Name_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isPathValid() {
        NamedCalculationProvider cop = panel.getObjectProvider();
        FileObject root = cop.getRoot();
        if(root.getFileObject(getFilePath()) != null) {
            showError(Bundle.MSG_CreateFactorWizardPanel1_File_Exists());
            return false;
        }
        return true;
    }
    
    private String getFilePath() {
        String path = "." + FactorDataObject.EXTENSION;
        path = panel.getCalculationName() + path;
        String folder = panel.getCalculationFolder();
        if(!folder.endsWith("/"))
            folder += "/";
        return folder + path;
    }
}
