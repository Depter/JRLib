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

package org.jreserve.gui.data.inport;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.api.DataEntry;
import org.jreserve.gui.data.spi.MonthDate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportDataModel.Column.Accident=Accident",
    "LBL.ImportDataModel.Column.Development=Development",
    "LBL.ImportDataModel.Column.Value=Value"
})
class ImportDataTableModel extends AbstractTableModel {

    private List<DataEntry> entries;
    private DataType dt;

    ImportDataTableModel() {
    }
    
    DataEntry getEntry(int row) {
        if(entries==null)
            return null;
        return entries.get(row);
    }
    
    void setEntries(List<DataEntry> entries) {
        this.entries = entries;
        fireTableDataChanged();
    }
    
    void setDataType(DataType dt) {
        this.dt = dt;
        fireTableStructureChanged();
    }
    
    @Override
    public int getRowCount() {
        return entries==null? 0 : entries.size();
    }

    @Override
    public String getColumnName(int column) {
        if(column==2)
            return Bundle.LBL_ImportDataModel_Column_Value();
        if(column==0)
            return Bundle.LBL_ImportDataModel_Column_Accident();
        if(DataType.VECTOR == dt)
            return Bundle.LBL_ImportDataModel_Column_Value();
        return Bundle.LBL_ImportDataModel_Column_Development();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex==2)
            return Double.class;
        if(columnIndex==0)
            return MonthDate.class;
        if(DataType.VECTOR == dt)
            return Double.class;
        return MonthDate.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public int getColumnCount() {
        if(dt==null)
            return 0;
        else if(DataType.VECTOR == dt)
            return 2;
        else
            return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DataEntry entry = entries.get(rowIndex);
        if(columnIndex == 0)
            return entry.getAccidentDate();
        if(columnIndex == 2)
            return entry.getValue();
        if(DataType.VECTOR == dt)
            return entry.getValue();
        return entry.getDevelopmentDate();
    }
}
