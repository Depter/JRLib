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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({ 
    "LBL.ExcelTemplateImportTableModel.Column.Item=Refference",
    "LBL.ExcelTemplateImportTableModel.Column.Used=Use",
    "LBL.ExcelTemplateImportTableModel.Column.Source=Storage",
    "LBL.ExcelTemplateImportTableModel.Column.Success="
})
class ExcelTemplateImportTableModel extends AbstractTableModel {
    final static int ITEM_COLUMN = 0;
    final static int ITEM_USED_COLUMN = 1;
    final static int DATA_SOURCE_COLUMN = 2;
    final static int SUCCESS_COLUMN = 3;
    final static int COLUMN_COUNT = 4;
    
    private List<DataImportTemplateItem> items = new ArrayList<DataImportTemplateItem>();
    private Map<DataImportTemplateItem, Boolean> used = new HashMap<DataImportTemplateItem, Boolean>();
    private Map<DataImportTemplateItem, DataSource> links = new HashMap<DataImportTemplateItem, DataSource>();
    private Map<DataImportTemplateItem, Exception> success = new HashMap<DataImportTemplateItem, Exception>();
    
    DataImportTemplateItem getItem(int row) {
        return items.get(row);
    }
    
    List<DataImportTemplateItem> getSelectedItems() {
        List<DataImportTemplateItem> result = new ArrayList<DataImportTemplateItem>();
        for(DataImportTemplateItem item : items)
            if(used.get(item))
                result.add(item);
        return result;
    }
    
    DataSource getDataSource(DataImportTemplateItem item) {
        return links.get(item);
    }
    
    void setItems(List<DataImportTemplateItem> items) {
        this.items.clear();
        links.clear();
        used.clear();
        if(items != null) {
            for(DataImportTemplateItem item : items) {
                this.items.add(item);
                used.put(item, Boolean.TRUE);
            }
        }
        fireTableStructureChanged();
    }
    
    void setUsed(int row, boolean used) {
        this.used.put(items.get(row), used);
        fireTableCellUpdated(row, ITEM_USED_COLUMN);
    }
    
    boolean isUsed(int row) {
        return used.get(items.get(row));
    }
    
    boolean hasSuccess(int row) {
        DataImportTemplateItem item = items.get(row);
        return success.containsKey(item);
    }
    
    void setSuccess(DataImportTemplateItem item, Exception ex) {
        int index = items.indexOf(item);
        if(index >= 0) {
            this.success.put(item, ex);
            fireTableCellUpdated(index, SUCCESS_COLUMN);
        }
    }
    
    void setDataSource(int row, DataSource ds) {
        DataImportTemplateItem item = items.get(row);
        if(ds == null)
            links.remove(item);
        else
            links.put(item, ds);
        fireTableCellUpdated(row, DATA_SOURCE_COLUMN);
    }
    
    @Override
    public int getRowCount() {
        return items == null? 0 : items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case ITEM_COLUMN: return Bundle.LBL_ExcelTemplateImportTableModel_Column_Item();
            case ITEM_USED_COLUMN: return Bundle.LBL_ExcelTemplateImportTableModel_Column_Used();
            case DATA_SOURCE_COLUMN: return Bundle.LBL_ExcelTemplateImportTableModel_Column_Source();
            case SUCCESS_COLUMN: return Bundle.LBL_ExcelTemplateImportTableModel_Column_Success();
            default:
                throw new IllegalArgumentException("Unkown column: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case ITEM_COLUMN: return DataImportTemplateItem.class;
            case ITEM_USED_COLUMN: return Boolean.class;
            case DATA_SOURCE_COLUMN: return DataSource.class;
            case SUCCESS_COLUMN: return Exception.class;
            default:
                throw new IllegalArgumentException("Unkown column: "+column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DataImportTemplateItem item = items.get(rowIndex);
        switch(columnIndex) {
            case ITEM_COLUMN: return item;
            case ITEM_USED_COLUMN: return used.get(item);
            case DATA_SOURCE_COLUMN: return links.get(item);
            case SUCCESS_COLUMN: return success.get(item);
            default:
                throw new IllegalArgumentException("Unkown column: "+columnIndex);
        }
    }
}
