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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExcelTableModel.Accident=Accident",
    "LBL.ExcelTableModel.Development=Development",
    "LBL.ExcelTableModel.Value=Value"
})
class ExcelTableModel extends AbstractTableModel {
    
    private boolean isVector = false;
    private int rCount = 0;
    private int cCount = 3;
    
    private Sheet sheet;
    private int firstRow;
    private int firstColumn;
    
    void setVector(boolean vector) {
        this.isVector = vector;
        fireTableStructureChanged();
    }
    
    void readData(Workbook wb, String reference) {
        releaseWorkbook();
        calculateSize(wb, reference);
        fireTableStructureChanged();
    }
    
    private void releaseWorkbook() {
        rCount = 0;
        firstRow = 0;
        firstColumn = 0;
        sheet = null;
    }
    
    private void calculateSize(Workbook wb, String reference) {
        CellReference ref = getCellReference(wb, reference);
        if(ref == null)
            return;
        String shName = ref.getSheetName();
        sheet = shName==null? null : wb.getSheet(shName);
        if(sheet == null)
            return;
        
        firstRow = ref.getRow();
        Row row = sheet.getRow(firstRow);
        if(row == null)
            return;
        
        firstColumn = ref.getCol();
        if(firstColumn < 0) 
            return;
        
        Cell cell;
        while(true) {
            row = sheet.getRow(firstRow + rCount);
            if(row == null)
                break;
            cell = row.getCell(firstColumn);
            if(cell == null)
                break;
            if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
                break;
            rCount++;
        }
    }
    
    private CellReference getCellReference(Workbook wb, String ref) {
        Name name = wb.getName(ref);
        if(name != null)
            ref = name.getRefersToFormula();
        
        if(ref.indexOf(':') > 0) {
            AreaReference aRef = new AreaReference(ref);
            return aRef.getFirstCell();
        } else {
            return new CellReference(ref);
        }
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ExcelTableModel_Accident();
        if(column == 1)
            return isVector? Bundle.LBL_ExcelTableModel_Value() : 
                    Bundle.LBL_ExcelTableModel_Value();
        if(column == 2)
            return Bundle.LBL_ExcelTableModel_Value();
        return super.getColumnName(column);
    }
    
    @Override
    public int getRowCount() {
        return rCount;
    }

    @Override
    public int getColumnCount() {
        return cCount;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Cell.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = sheet.getRow(firstRow + rowIndex);
        if(row == null)
            return null;
        return row.getCell(firstColumn + columnIndex);
    }
    
    List<DataEntry> getEntries(boolean isVector) {
        List<DataEntry> entries = new ArrayList<DataEntry>();
        
    }
}
