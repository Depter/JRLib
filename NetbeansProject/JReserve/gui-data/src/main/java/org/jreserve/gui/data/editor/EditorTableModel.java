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
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
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
    "LBL.EditorTableModel.Value=Value",
    "# {0} - path",
    "MSG.EditorTableModel.LoadError=Unable to load claims from storage ''{0}''!"
})
class EditorTableModel extends AbstractTableModel {
    
    private List<DataEntry> entries = new ArrayList<DataEntry>();
    private DataSource ds;
    private boolean isTriangle;
    private DataEntryFilter filter = DataEntryFilter.ALL;
    
    public EditorTableModel(DataSource ds) {
        this.ds = ds;
        this.isTriangle = DataType.TRIANGLE == ds.getDataType();
    }
    
    void setFilter(DataEntryFilter filter) {
        this.filter = filter==null? DataEntryFilter.ALL : filter;
        refresh();
    }
    
    void refresh() {
        this.entries.clear();
        try {
            this.entries.addAll(ds.getDataProvider().getEntries(filter));
        } catch (Exception ex) {
            String msg = Bundle.MSG_EditorTableModel_LoadError(ds.getPath());
            BubbleUtil.showException(msg, ex);
        }
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return entries==null? 0 : entries.size();
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
        DataEntry entry = entries.get(row);
        if(column == 0)
            return entry.getAccidentDate();
        if(column == 2)
            return entry.getValue();
        return isTriangle? entry.getDevelopmentDate() : entry.getValue();
    }

}
