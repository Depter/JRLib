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

package org.jreserve.gui.data.clipboard;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClipboardTableModel extends AbstractTableModel {

    private final static String LINE_SPLIT = "\\r?\\n|\\r";
    private final static String CELL_SPLIT = "\\t";
    
    private List<List<String>> values = new ArrayList<List<String>>();
    private int columnCount;
    
    void setText(String text) {
        clearData();
        if(text != null && text.length() > 0)
            readText(text);
        fireTableStructureChanged();
    }
    
    private void clearData() {
        values.clear();
        columnCount = 0;
    }
    
    private void readText(String text) {
        for(String line : text.split(LINE_SPLIT)) {
            List<String> row = new ArrayList<String>();
            for(String cell : line.split(CELL_SPLIT)) {
                if(cell!=null && cell.trim().length()==0)
                    cell = null;
                row.add(cell);
            }
            values.add(row);
            columnCount = Math.max(columnCount, row.size());
        }
    }
    
    String[][] getValues() {
        int rCount = values.size();
        String[][] result = new String[rCount][columnCount];
        for(int r=0; r<rCount; r++)
            for(int c=0; c<columnCount; c++)
                result[r][c] = (String) getValueAt(r, c);
        return result;
    }
    
    @Override
    public int getRowCount() {
        return values==null? 0 : values.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return ""+(columnIndex+1);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(values == null) return null;
        if(rowIndex<0 || rowIndex>=values.size()) return null;
        
        List<String> row = values.get(rowIndex);
        if(row == null) return null;
        if(columnIndex<0 || columnIndex>=row.size()) return null;
        return row.get(columnIndex);
    }

}
