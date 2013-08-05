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
package org.jreserve.gui.data.csv.input;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PreviewTableModel extends AbstractTableModel {
    
    private String[] lines;
    private String cellSeparator;
    private boolean hasColumnTitles;

    private List<String[]> rows;
    
    void setCellSeparator(String cellSeparator) {
        this.cellSeparator = cellSeparator;
        initRows();
    }
    
    void setLines(String[] lines) {
        this.lines = lines;
        initRows();
    }
    
    void setHasColumnTitles(boolean hasColumnTitles) {
        this.hasColumnTitles = hasColumnTitles;
        initRows();
    }
    
    private void initRows() {
        rows = new ArrayList<String[]>();
        if(lines != null) {
            for(String line : lines)
                rows.add(splitLine(line));
        }
        super.fireTableStructureChanged();
    }
    
    private String[] splitLine(String line) {
        if(cellSeparator == null || cellSeparator.length()==0 || line == null)
            return new String[]{line};
        return line.split(cellSeparator);
    }
    
    @Override
    public int getRowCount() {
        if(rows == null || rows.isEmpty())
            return 0;
        int count = rows.size();
        return hasColumnTitles? count-1 : count;
    }

    @Override
    public int getColumnCount() {
        if(rows==null || rows.isEmpty())
            return 0;
        String[] cells = rows.get(0);
        return cells==null? 0 : cells.length;
    }

    @Override
    public String getColumnName(int column) {
        if(hasColumnTitles && rows!=null && !rows.isEmpty()) {
            String[] names = rows.get(0);
            if(names.length > column)
                return names[column];
        }
        return super.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rows==null || rows.isEmpty())
            return null;
        if(hasColumnTitles) rowIndex++;
        if(rows.size() <= rowIndex)
            return null;
        
        String[] cells = rows.get(rowIndex);
        if(cells==null || cells.length<=columnIndex)
            return null;
        return cells[columnIndex];
    }
    
}
