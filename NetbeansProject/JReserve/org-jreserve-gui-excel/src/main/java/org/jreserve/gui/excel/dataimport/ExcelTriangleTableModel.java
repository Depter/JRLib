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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelTriangleTableModel extends AbstractTableModel {
    private List<ExcelCell[]> rows = new ArrayList<ExcelCell[]>();
    private int columnCount;
    
    void setValues(double[][] values) {
        rows.clear();
        columnCount = 0;
        if(values != null)
            calculateTriangle(values);
        fireTableStructureChanged();
    }
    
    private void calculateTriangle(double[][] values) {
        for(double[] row : values) {
            ExcelCell[] cells = createRow(row);
            if(cells.length > columnCount)
                columnCount = cells.length;
            rows.add(cells);
        }
    }
    
    private ExcelCell[] createRow(double[] row) {
        int size = row.length;
        ExcelCell[] result = new ExcelCell[size];
        for(int c=0; c<size; c++)
            result[c] = new ExcelCell(ExcelCell.Type.DOUBLE, row[c]);
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
        ExcelCell[] row = rows.get(rowIndex);
        if(columnIndex >= row.length)
            return null;
        return row[columnIndex];
    }
}
