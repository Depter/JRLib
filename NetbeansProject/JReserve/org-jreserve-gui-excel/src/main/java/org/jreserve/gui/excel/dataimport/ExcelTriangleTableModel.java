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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.poi.ExcelUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelTriangleTableModel extends AbstractTableModel {
    private List<ExcelCell[]> rows = new ArrayList<ExcelCell[]>();
    private int columnCount;
    
    void readData(Workbook wb, String reference) {
        rows.clear();
        calculateTriangle(wb, reference);
        fireTableStructureChanged();
    }

    private void calculateTriangle(Workbook wb, String reference) {
        if(wb == null || reference==null || reference.length()==0) return;
        CellReference ref = ExcelUtil.getValidCellReference(wb, reference);
        if(ref == null) return;
        
        Sheet sheet = wb.getSheet(ref.getSheetName());
        int firstRow = ref.getRow();
        int firstColumn = ref.getCol();
        
        Row row = sheet.getRow(firstRow);
        if(row == null) return;
        columnCount = 0;
        while(!ExcelUtil.isEmpty(row.getCell(firstColumn+columnCount)))
            columnCount++;
        
        int rCount = 0;
        while(true) {
            row = sheet.getRow(firstRow + rCount++);
            if(row == null || isEmptyRow(row, firstColumn))
                break;
            rows.add(readRow(row, firstColumn));
        }
        
    }
    
    private boolean isEmptyRow(Row row, int firstColumn) {
        for(int c=0; c<columnCount; c++)
            if(row.getCell(firstColumn+c) != null)
                return false;
        return true;
    }
    
    private ExcelCell[] readRow(Row row, int firstColumn) {
        ExcelCell[] result = new ExcelCell[columnCount];
        for(int c=0; c<columnCount; c++)
            result[c] = ExcelCell.createCell(ExcelCell.Type.DOUBLE, row.getCell(firstColumn+c));
        return result;
    }

    @Override
    public String getColumnName(int column) {
        return ""+(column+1);
    }
    
    @Override
    public int getRowCount() {
        return rows==null? 0 : rows.size();
    }

    @Override
    public int getColumnCount() {
        return getRowCount()==0? 0 : columnCount;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ExcelCell.class;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex)[columnIndex];
    }
}
