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

package org.jreserve.gui.excel.dataimport;

import java.awt.Component;
import java.io.File;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplate;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ExcelTemplateImportWizardPanel1.Path.Empty=File not selected!",
    "MSG.ExcelTemplateImportWizardPanel1.Path.NotFound=File not found!",
    "MSG.ExcelTemplateImportWizardPanel1.Template.Empty=Template not selected!"
})
class ExcelTemplateImportWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    private final ChangeSupport cs = new ChangeSupport(this);
    private ExcelTemplateImportVisualPanel1 component;
    private boolean valid = false;
    private WizardDescriptor wiz;
    
    @Override
    public Component getComponent() {
        if(component == null)
            component = new ExcelTemplateImportVisualPanel1(this);
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(ExcelTemplateImportDataProvider.PROP_IMPORT_FILE, new File(component.getPath()));
        settings.putProperty(ExcelTemplateImportDataProvider.PROP_TEMPLATE, component.getTemplate());
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }

    void changed() {
        valid = isInputValid();
        if(valid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return isFileValid() && isTemplateValid();
    }
    
    private boolean isFileValid() {
        String path = component.getPath();
        if(path == null || path.length() == 0) {
            showError(Bundle.MSG_ExcelTemplateImportWizardPanel1_Path_Empty());
            return false;
        }
        
        File file = new File(path);
        if(!(file.exists() && file.isFile())) {
            showError(Bundle.MSG_ExcelTemplateImportWizardPanel1_Path_NotFound());
            return false;
        }
        
        return true;
    }
    
    private boolean isTemplateValid() {
        DataImportTemplate template = component.getTemplate();
        if(template == null) {
            showError(Bundle.MSG_ExcelTemplateImportWizardPanel1_Template_Empty());
            return false;
        }
        
        return true;
    }
}
