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

package org.jreserve.gui.data.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.EditorTableModel.Accident=Accident",
    "LBL.EditorTableModel.Development=Accident",
    "LBL.EditorTableModel.Value=Value"
})
class EditorTableModel extends AbstractTableModel {
    
    private Set<DataEntry> entries = new TreeSet<DataEntry>();
    private List<DataEntry> filteredEntries = new ArrayList<DataEntry>();
    private final boolean isTriangle;
    private DataEntryFilter filter = DataEntryFilter.ALL;
    
    public EditorTableModel(DataType dt) {
        this.isTriangle = DataType.TRIANGLE == dt;
    }
    
    void setEntries(List<DataEntry> entries) {
        this.entries.clear();
        if(entries != null)
            this.entries.addAll(entries);
        refresh();
    }
    
    void setFilter(DataEntryFilter filter) {
        this.filter = filter==null? DataEntryFilter.ALL : filter;
        refresh();
    }
    
    DataEntry getEntryAt(int row) {
        return filteredEntries.get(row);
    }
    
    private void refresh() {
        this.filteredEntries.clear();
        filterEntries();
        fireTableDataChanged();
    }
    
    private void filterEntries() {
        for(DataEntry entry : entries)
            if(filter.acceptsEntry(entry))
                filteredEntries.add(entry);
    }
    
    @Override
    public int getRowCount() {
        return filteredEntries==null? 0 : filteredEntries.size();
    }

    @Override
    public int getColumnCount() {
        return isTriangle? 3 : 2;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_EditorTableModel_Accident();
        if(column == 2)
            return Bundle.LBL_EditorTableModel_Value();
        return isTriangle? 
                Bundle.LBL_EditorTableModel_Development() :
                Bundle.LBL_EditorTableModel_Value();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return MonthDate.class;
        if(column == 2)
            return Double.class;
        return isTriangle? MonthDate.class : Double.class;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        DataEntry entry = filteredEntries.get(row);
        if(column == 0)
            return entry.getAccidentDate();
        if(column == 2)
            return entry.getValue();
        return isTriangle? entry.getDevelopmentDate() : entry.getValue();
    }

}
