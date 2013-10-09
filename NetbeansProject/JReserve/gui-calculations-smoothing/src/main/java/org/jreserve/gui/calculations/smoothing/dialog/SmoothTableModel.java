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
package org.jreserve.gui.calculations.smoothing.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SmoothTableModel.Column.Accident=Accident",
    "LBL.SmoothTableModel.Column.Development=Development",
    "LBL.SmoothTableModel.Column.Apply=Apply",
    "LBL.SmoothTableModel.Column.Original=Original",
    "LBL.SmoothTableModel.Column.Smoothed=Smoothed"
})
class SmoothTableModel extends AbstractTableModel {

    final static int COLUMN_ACCIDENT = 0;
    final static int COLUMN_DEVELOPMENT = 1;
    final static int COLUMN_APPLY = 2;
    final static int COLUMN_ORIGINAL = 3;
    final static int COLUMN_SMOOTHED = 4;
    final static int COLUMN_COUNT = 5;
    
    private final List<SmoothRecord> records;

    SmoothTableModel(List<SmoothRecord> records) {
        this.records = new ArrayList<SmoothRecord>(records);
    }
    
    @Override
    public int getRowCount() {
        return records==null? 0 : records.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case COLUMN_ACCIDENT:
                return Bundle.LBL_SmoothTableModel_Column_Accident();
            case COLUMN_DEVELOPMENT:
                return Bundle.LBL_SmoothTableModel_Column_Development();
            case COLUMN_APPLY:
                return Bundle.LBL_SmoothTableModel_Column_Apply();
            case COLUMN_ORIGINAL:
                return Bundle.LBL_SmoothTableModel_Column_Original();
            case COLUMN_SMOOTHED:
                return Bundle.LBL_SmoothTableModel_Column_Smoothed();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case COLUMN_ACCIDENT:
                return MonthDate.class;
            case COLUMN_DEVELOPMENT:
                return Integer.class;
            case COLUMN_APPLY:
                return Boolean.class;
            case COLUMN_ORIGINAL:
                return Double.class;
            case COLUMN_SMOOTHED:
                return Double.class;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        SmoothRecord record = records.get(row);
        switch(column) {
            case COLUMN_ACCIDENT:
                return record.getAccidentDate();
            case COLUMN_DEVELOPMENT:
                return record.getDevelopment()+1;
            case COLUMN_APPLY:
                return record.isUsed();
            case COLUMN_ORIGINAL:
                return record.getOriginal();
            case COLUMN_SMOOTHED:
                return record.getSmoothed();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column != COLUMN_APPLY)
            throw new IllegalArgumentException("Column not editable: "+column);
        records.get(row).setUsed((Boolean) value);
        super.fireTableCellUpdated(row, column);
    }
}
