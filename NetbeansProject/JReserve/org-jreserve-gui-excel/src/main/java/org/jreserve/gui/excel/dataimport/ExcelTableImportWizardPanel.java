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
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.ImportDataProvider;
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
    "MSG.ExcelTableImportWizardPanel.Workbook.Empty=Workbook not selected!",
    "MSG.ExcelTableImportWizardPanel.Reference.Empty=reference is not set!",
    "# {0} - reference",
    "MSG.ExcelTableImportWizardPanel.Reference.Invalid=Reference ''{0}'' is invalid!"
})
class ExcelTableImportWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ExcelTableImportVisualPanel component;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ExcelTableImportVisualPanel(this);
            if(wiz != null)
                initPanel();
        }
        return component;
    }
    
    private void initPanel() {
        DataSource ds = (DataSource) wiz.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
        component.setVector(DataType.VECTOR == ds.getDataType());
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(component != null)
            initPanel();
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
        return component != null &&
               isWorkbookSelected() &&
               isValidReference();
    }
    
    private boolean isWorkbookSelected() {
        if(component.getWorkbook() == null) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Workbook_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isValidReference() {
        String ref = component.getReference();
        if(ref == null || ref.length()==0) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Reference_Empty());
            return false;
        }
        
        Workbook wb = component.getWorkbook();
        if(isName(wb, ref))
            return true;
        
        CellReference cRef = getFirstCell(ref);
        if(cRef == null || !isValidReference(wb, cRef)) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Reference_Invalid(ref));
            return false;
        }
        
        return true;
    }
    
    private boolean isName(Workbook wb, String name) {
        if(wb.getSheetIndex(name) >= 0)
            return false;
        Name n = wb.getName(name);
        return n!=null && !n.isDeleted() && !n.isFunctionName();
    }
    
    private CellReference getFirstCell(String ref) {
        if(ref.indexOf(':') > 0) {
            AreaReference aRef = new AreaReference(ref);
            return aRef.getFirstCell();
        }
        return new CellReference(ref);
    }
    
    private boolean isValidReference(Workbook wb, CellReference ref) {
        return wb.getSheetIndex(ref.getSheetName()) >= 0 &&
               ref.getRow() >= 0 &&
               ref.getCol() >= 0;
    }
}
