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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.ImportDataWizardPanelGeometry;
import org.jreserve.gui.poi.ExcelUtil;
import org.jreserve.gui.poi.read.ReferenceUtil;
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
    "# {0} - addres",
    "MSG.ExcelTriangleImportWizardPanel.Read.NumberError=Unable to read number form cell ''{0}''!"
})
public class ExcelTriangleImportWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ExcelTriangleImportVisualPanel component;
    
    private final Object inputLock = new Object();
    private ValidationInput input;

    @Override
    public Component getComponent() {
        if(component == null)
            component = new ExcelTriangleImportVisualPanel(this);
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
            
            List<double[]> values = new ArrayList<double[]>();
            int rCount = 0;
            while(true) {
                Row row = sheet.getRow(firstRow + rCount++);
                if(row == null) break;
                
                double[] rowValues = readRow(row, firstColumn);
                if(rowValues == null)
                    break;
                
                values.add(rowValues);
            }
            
            if(values.isEmpty()) {
                String msg = "No data is imported!";
                String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Data_Empty();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            wiz.putProperty(ImportDataWizardPanelGeometry.PROP_TRIANGLE_ARRAY, toArray(values));
        }
    }
    
    private double[][] toArray(List<double[]> values) {
        int size = values.size();
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = values.get(i);
        return result;
    }
    
    private double[] readRow(Row row, int firstColumn) throws WizardValidationException {
        int cCount = 0;
        while(!ExcelUtil.isEmpty(row.getCell(firstColumn+cCount)))
            cCount++;
        
        if(cCount == 0) return null;
        
        double[] values = new double[cCount];
        for(int c=0; c<cCount; c++) {
            Cell cell = row.getCell(firstColumn+c);
            try {
                values[c] = cell.getNumericCellValue();
            } catch (Exception ex) {
                String sCell = ExcelUtil.toString(cell);
                String msg = "Unable to read number from: "+sCell;
                String lMsg = Bundle.MSG_ExcelTriangleImportWizardPanel_Read_NumberError(sCell);
                throw new WizardValidationException(component, msg, lMsg);
            }
        }
        return values;
    }
    
    private class ValidationInput {
        private final File file;
        private final CellReference reference;
    
        private ValidationInput() {
            file = new File(component.getFilePath());
            
            String rString = component.getReference();
            ReferenceUtil util = component.getReferenceUtil();
            reference = util.toCellReference(rString);
        }
    }
}
