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
package org.jreserve.gui.excel.template.dataimport.createwizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateModel.SourceType;
import org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateModel.TemplateRow;
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
    "LBL.CreateTemplateWizardPanel.ComponentName=Create Template",
    "MSG.CreateTemplateWizardPanel.Name.Empty=Name is not set!",
    "# {0} - name",
    "MSG.CreateTemplateWizardPanel.Name.Used=Name ''{0}'' is already exists!"
})
class CreateTemplateWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private CreateTempalteWizardVisualPanel component;
    private ExcelTemplateManager<ExcelTemplate> tm;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new CreateTempalteWizardVisualPanel();
            component.setName(Bundle.LBL_CreateTemplateWizardPanel_ComponentName());
            component.addPropertyChangeListener(new InputListener());
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        tm = (ExcelTemplateManager<ExcelTemplate>) wiz.getProperty(CreateTemplateWizardIterator.PROP_TEMPLATE_MANAGER);
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
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
    
    private void inputChanged() {
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
        return component != null &&
               tm != null &&
               isNameValid() &&
               isRowsValid();
    }
    
    private boolean isNameValid() {
        String name = component.getTemplateName();
        if(name == null || name.length()==0) {
            showError(Bundle.MSG_CreateTemplateWizardPanel_Name_Empty());
            return false;
        }
            
        for(ExcelTemplate template : tm.getTemplates()) {
            if(name.equalsIgnoreCase(template.getName())) {
                showError(Bundle.MSG_CreateTemplateWizardPanel_Name_Used(name));
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isRowsValid() {
        List<TemplateRow> rows = component.getTempalteRows();
        int size = rows.size();
        if(size == 0) {
            showError("no rows");
            return false;
        }
        
        for(int i=0; i<size; i++) {
            if(!isRowValid(rows.get(i), i))
                return false;
        }
        return true;
    }
    
    private boolean isRowValid(TemplateRow row, int index) {
        String ref = row.getReference();
        if(ref == null || ref.length()==0) {
            showError("Ref empty: "+(index+1));
            return false;
        }
        
        if(SourceType.TRIANGLE == row.getSourceType()) {
            if(row.getMonthDate() == null) {
                showError("Start date empty: "+(index+1));
                return false;
            }
            if(row.getAccidentLength() == null) {
                showError("Accident length empty: "+(index+1));
                return false;
            }
            if(row.getDevelopmentLength() == null) {
                showError("Development length empty: "+(index+1));
                return false;
            }
        }
        return true;
    }
    
    private class InputListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(shouldUpdate(evt.getPropertyName()))
                inputChanged();
        }
        
        private boolean shouldUpdate(String propName) {
            return CreateTempalteWizardVisualPanel.PROP_TEMPLATE_NAME.equals(propName) ||
                   CreateTempalteWizardVisualPanel.PROP_TEMPLATE_ROWS.equals(propName);
        }
    
    }
}
