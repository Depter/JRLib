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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem.Table;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem.Triangle;
import org.jreserve.jrlib.gui.data.DataType;
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
    "MSG.CreateTemplateWizardPanel.Name.Used=Name ''{0}'' is already exists!",
    "MSG.CreateTemplateWizardPanel.Data.Empty=Define at least one item!",
    "# {0} - row",
    "MSG.CreateTemplateWizardPanel.Data.Empty.Reference=Reference is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.CreateTemplateWizardPanel.Data.Empty.StartDate=Start date is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.CreateTemplateWizardPanel.Data.Empty.ALength=Accident length is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.CreateTemplateWizardPanel.Data.Empty.DLength=Development length is not set in row ''{0}''!"
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
            component.addChangeListener(new InputListener());
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
        settings.putProperty(CreateTemplateWizardIterator.PROP_TEMPLATE_NAME, component.getTemplateName());
        settings.putProperty(CreateTemplateWizardIterator.PROP_TEMPLATE_ITEMS, createTemplateItems());
    }
    
    private List<DataImportTemplateItem> createTemplateItems() {
        List<DataImportTemplateItem> items = new ArrayList<DataImportTemplateItem>();
        for(TemplateRow row : component.getTempalteRows())
            items.add(createItem(row));
        return items;
    }
    
    private DataImportTemplateItem createItem(TemplateRow row) {
        String ref = row.getReference();
        DataType dt = row.getDataType();
        boolean cummulated = row.isCummulated();
        if(SourceType.TABLE == row.getSourceType()) {
            return new Table(ref, dt, cummulated);
        } else {
            return new Triangle(ref, dt, cummulated,
                    row.getMonthDate(), 
                    row.getAccidentLength(), row.getDevelopmentLength());
        }
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
            showError(Bundle.MSG_CreateTemplateWizardPanel_Data_Empty());
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
            showError(Bundle.MSG_CreateTemplateWizardPanel_Data_Empty_Reference(index+1));
            return false;
        }
        
        if(SourceType.TRIANGLE == row.getSourceType()) {
            if(row.getMonthDate() == null) {
                showError(Bundle.MSG_CreateTemplateWizardPanel_Data_Empty_StartDate(index+1));
                return false;
            }
            if(row.getAccidentLength() == null) {
                showError(Bundle.MSG_CreateTemplateWizardPanel_Data_Empty_ALength(index+1));
                return false;
            }
            if(row.getDevelopmentLength() == null) {
                showError(Bundle.MSG_CreateTemplateWizardPanel_Data_Empty_DLength(index+1));
                return false;
            }
        }
        return true;
    }
    
    private class InputListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            inputChanged();
        }
    }
}
