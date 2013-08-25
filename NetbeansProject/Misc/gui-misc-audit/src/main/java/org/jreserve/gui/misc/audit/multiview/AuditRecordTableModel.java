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

package org.jreserve.gui.misc.audit.multiview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.AuditRecordTable.Date=Date",
    "LBL.AuditRecordTable.User=User",
    "LBL.AuditRecordTable.Object=Object",
    "LBL.AuditRecordTable.Change=Change"
})
class AuditRecordTableModel extends AbstractTableModel {

    private final static int DATE_COLUMN = 0;
    private final static int USER_COLUMN = 1;
    private final static int OBJECT_COLUMN = 2;
    private final static int CHANGE_COLUMN = 3;
    private final static int COLUMN_COUNT = 4;
    
    private List<AuditRecord> records = new ArrayList<AuditRecord>();
    
    void setRecords(List<AuditRecord> records) {
        this.records.clear();
        if(records != null)
            this.records.addAll(records);
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return records == null? 0 : records.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case DATE_COLUMN: return Bundle.LBL_AuditRecordTable_Date();
            case USER_COLUMN: return Bundle.LBL_AuditRecordTable_User();
            case OBJECT_COLUMN: return Bundle.LBL_AuditRecordTable_Object();
            case CHANGE_COLUMN: return Bundle.LBL_AuditRecordTable_Change();
            default:
                throw new IllegalArgumentException("Unkown column id: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case DATE_COLUMN: return Date.class;
            case USER_COLUMN: return String.class;
            case OBJECT_COLUMN: return String.class;
            case CHANGE_COLUMN: return String.class;
            default:
                throw new IllegalArgumentException("Unkown column id: "+column);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        AuditRecord record = records.get(row);
        switch(column) {
            case DATE_COLUMN: return record.getChangeDate();
            case USER_COLUMN: return record.getUser();
            case OBJECT_COLUMN: return record.getComponent();
            case CHANGE_COLUMN: return record.getChange();
            default:
                throw new IllegalArgumentException("Unkown column id: "+column);
        }
    }
}
