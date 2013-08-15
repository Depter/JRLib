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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.jreserve.gui.excel.ExcelUtil;
import org.jreserve.gui.excel.poiutil.ReferenceUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
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
    "MSG.ExcelTableImportWizardPanel.Reference.Invalid=Reference ''{0}'' is invalid!",
    "MSG.ExcelTableImportWizardPanel.Data.Empty=The specified range is empty!",
    "# {0} - row",
    "# {1} - column",
    "MSG.ExcelTableImportWizardPanel.Data.WrongCell=Illegal cell value in [{0}, {1}]!",
    "# {0} - addres",
    "MSG.ExcelTableImportWizardPanel.Read.DateError=Unable to read date from cell ''{0}''!",
    "# {0} - addres",
    "MSG.ExcelTableImportWizardPanel.Read.NumberError=Unable to read number from cell ''{0}''!",
    "# {0} - r1",
    "# {1} - r0",
    "MSG.ExcelTableImportWizardPanel.Read.Duplicate=Entry in row ''{0}'' is duplicate of row ''{1}''!"
})
class ExcelTableImportWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ExcelTableImportVisualPanel component;
    
    private final Object inputLock = new Object();
    private ValidationInput input;
    
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
        if(component.getReferenceUtil() == null) {
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
        
        ReferenceUtil refUtil = component.getReferenceUtil();
        if(!refUtil.isReferenceValid(ref)) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Reference_Invalid(ref));
            return false;
        }
        
        return true;
    }

    @Override
    public void prepareValidation() {
        synchronized(inputLock) {
            input = new ValidationInput();
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        synchronized(inputLock) {
            CellReference ref = ExcelUtil.getValidCellReference(input.wb, input.reference);
            if(ref == null)
                throw new WizardValidationException(component, "Invalid reference: "+input.reference, Bundle.MSG_ExcelTableImportWizardPanel_Reference_Invalid(input.reference));
            
            Sheet sheet = input.wb.getSheet(ref.getSheetName());
            int firstRow = ref.getRow();
            int firstColumn = ref.getCol();
            
            List<DataEntry> entries = new ArrayList<DataEntry>();
            int rCount = 0;
            while(true) {
                Row row = sheet.getRow(firstRow + rCount++);
                if(row == null) break;
                
                DataEntry entry = input.isVector? getVector(row, firstColumn) : getTriangle(row, firstColumn);
                if(entry == null) break;
                
                int index = entries.indexOf(entry);
                if(index >= 0) {
                    int r0 = firstRow + index + 1;
                    int r1 = firstRow + rCount;
                    String msg = "Duplicate entry in rows: "+r0 + ", "+r1;
                    String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Read_Duplicate(r1, r0);
                    throw new WizardValidationException(component, msg, lMsg);
                }
                entries.add(entry);
            }
            
            if(entries.isEmpty()) {
                String msg = "No data is imported!";
                String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Data_Empty();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            wiz.putProperty(ImportDataProvider.PROP_IMPORT_DATA, entries);
        }
    }
    
    private DataEntry getTriangle(Row row, int firstColumn) throws WizardValidationException {
        Cell ac = row.getCell(firstColumn);
        Cell dc = row.getCell(firstColumn+1);
        Cell vc = row.getCell(firstColumn+2);
        if(isEmpty(ac) && isEmpty(dc) && isEmpty(vc))
            return null;
        return new DataEntry(getDate(ac), getDate(dc), getDouble(vc));
    }
    
    private DataEntry getVector(Row row, int firstColumn) throws WizardValidationException {
        Cell ac = row.getCell(firstColumn);
        Cell vc = row.getCell(firstColumn+1);
        if(isEmpty(ac) && isEmpty(vc))
            return null;
        return new DataEntry(getDate(ac), getDouble(vc));
    }
    
    private boolean isEmpty(Cell cell) {
        return cell == null || Cell.CELL_TYPE_BLANK == cell.getCellType();
    }
    
    private MonthDate getDate(Cell cell) throws WizardValidationException {
        try {
            return input.mdf.toMonthDate(cell.getDateCellValue());
        } catch (Exception ex) {
            String str = ExcelUtil.toString(cell);
            throw new WizardValidationException(component, "Unable to read date from cell: "+str, Bundle.MSG_ExcelTableImportWizardPanel_Read_DateError(str));
        }
    }
    
    private double getDouble(Cell cell) throws WizardValidationException {
        try {
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            String str = ExcelUtil.toString(cell);
            throw new WizardValidationException(component, "Unable to read number from cell: "+str, Bundle.MSG_ExcelTableImportWizardPanel_Read_NumberError(str));
        }
    }
    
    private class ValidationInput {
        private final Workbook wb = null;//component.getWorkbook();
        private final boolean isVector = component.isVector();
        private final String reference = component.getReference();
        private final MonthDate.Factory mdf = new MonthDate.Factory();
    }
}
