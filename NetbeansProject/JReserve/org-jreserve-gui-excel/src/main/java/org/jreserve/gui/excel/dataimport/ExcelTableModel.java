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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.excel.ExcelUtil;
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
    
    private int firstRow;
    private int firstColumn;
    private List<ExcelCell[]> rows = new ArrayList<ExcelCell[]>();
    
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
        rows.clear();
        firstRow = 0;
        firstColumn = 0;
    }
    
    private void calculateSize(Workbook wb, String reference) {
        CellReference ref = ExcelUtil.getValidCellReference(wb, reference);
        if(ref == null)
            return;
        Sheet sheet = wb.getSheet(ref.getSheetName());
        firstRow = ref.getRow();
        firstColumn = ref.getCol();
        
        int rCount = 0;
        while(true) {
            Row row = sheet.getRow(firstRow + rCount++);
            if(row == null)
                break;
            
            ExcelCell[] r = isVector? getVectorRow(row) : getTriangleRow(row);
            if(r == null)
                break;
            rows.add(r);
        }
    }
    
    private ExcelCell[] getVectorRow(Row row) {
        Cell ac = row.getCell(firstColumn);
        Cell vc = row.getCell(firstColumn+1);
        if(ac==null && vc==null)
            return null;
        return new ExcelCell[]{
            ExcelCell.createCell(ExcelCell.Type.DATE, ac),
            ExcelCell.createCell(ExcelCell.Type.DOUBLE, vc)
        };
    }
    
    private ExcelCell[] getTriangleRow(Row row) {
        Cell ac = row.getCell(firstColumn);
        Cell dc = row.getCell(firstColumn+1);
        Cell vc = row.getCell(firstColumn+2);
        if(ac==null && dc==null && vc==null)
            return null;
        return new ExcelCell[]{
            ExcelCell.createCell(ExcelCell.Type.DATE, ac),
            ExcelCell.createCell(ExcelCell.Type.DATE, dc),
            ExcelCell.createCell(ExcelCell.Type.DOUBLE, vc)
        };
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
        return rows==null? 0 : rows.size();
    }

    @Override
    public int getColumnCount() {
        return isVector? 2 : 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ExcelCell.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex)[columnIndex];
    }
    
    List<ExcelCell[]> getRows() {
        return rows;
    }
 }
