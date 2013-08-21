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
    private List<ExcelCell[]> rows = new ArrayList<ExcelCell[]>();
    
    void setVector(boolean vector) {
        this.isVector = vector;
        fireTableStructureChanged();
    }
    
    void setEntries(List<DataEntry> entries) {
        rows.clear();
        if(entries != null)
            fillRows(entries);
        fireTableStructureChanged();
    }
    
    private void fillRows(List<DataEntry> entries) {
        for(DataEntry entry : entries)
            rows.add(isVector? createVectorRow(entry) : createTriangleRow(entry));
    }
    
    private ExcelCell[] createVectorRow(DataEntry entry) {
        ExcelCell[] row = new ExcelCell[2];
        row[0] = new ExcelCell(ExcelCell.Type.DATE, entry.getAccidentDate());
        row[1] = new ExcelCell(ExcelCell.Type.DOUBLE, entry.getValue());
        return row;
    }
    
    private ExcelCell[] createTriangleRow(DataEntry entry) {
        ExcelCell[] row = new ExcelCell[3];
        row[0] = new ExcelCell(ExcelCell.Type.DATE, entry.getAccidentDate());
        row[1] = new ExcelCell(ExcelCell.Type.DATE, entry.getDevelopmentDate());
        row[2] = new ExcelCell(ExcelCell.Type.DOUBLE, entry.getValue());
        return row;
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