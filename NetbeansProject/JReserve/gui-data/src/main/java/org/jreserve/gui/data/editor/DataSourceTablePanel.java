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

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.util.DataEntryLoader;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.SwingCallback;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.DataSourceTablePanel.ProgressTitle=Loading data from ''{0}''.",
    "# {0} - path",
    "MSG.DataSourceTablePanel.LoadError=Loading data from ''{0}'' failed!"
})
class DataSourceTablePanel extends JPanel {
    
    private final static String LOADING_CARD = "loadingCard";
    private final static String TABLE_CARD = "tableCard";
    
    private CardLayout layout;
    private EditorTableModel tableModel;
    private JLabel loadLabel;
    private DataSource ds;
    
    DataSourceTablePanel(DataSource ds) {
        this.ds = ds;
        tableModel = new EditorTableModel(ds.getDataType());
        initComponents();
        loadEntries();
    }
    
    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);
        JTable table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);
        
        DataEntryRenderer renderer = new DataEntryRenderer();
        table.setDefaultRenderer(Double.class, renderer);
        table.setDefaultRenderer(MonthDate.class, renderer);
        
        
        add(new JScrollPane(table), TABLE_CARD);
        
        loadLabel = new JLabel("Loading...");
        loadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(loadLabel, LOADING_CARD);
    }
    
    private void loadEntries() {
        layout.show(this, LOADING_CARD);
        String msg = Bundle.MSG_DataSourceTablePanel_ProgressTitle(ds.getPath());
        TaskUtil.execute(new DataEntryLoader(ds), new LoaderCallback(), msg);
    }
    
    private class LoaderCallback implements SwingCallback<List<DataEntry>> {

        @Override
        public void finnished(List<DataEntry> result) {
            tableModel.setEntries(result);
            layout.show(DataSourceTablePanel.this, TABLE_CARD);
        }

        @Override
        public void finnishedWithException(Exception ex) {
            String msg = Bundle.MSG_DataSourceTablePanel_LoadError(ds.getPath());
            BubbleUtil.showException(msg, ex);
            layout.show(DataSourceTablePanel.this, TABLE_CARD);
        }
    
    }
    
    private static class DataEntryRenderer extends DefaultTableCellRenderer {
        
        LocaleSettings.DecimalFormatter df =  LocaleSettings.createDecimalFormatter();
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean seleced, boolean hasFocus, int row, int column) {
            value = getValueAsString(value);
            return super.getTableCellRendererComponent(table, value, seleced, hasFocus, row, column);
        }
        
        private String getValueAsString(Object value) {
            if(value == null)
                return null;
            if(value instanceof Double)
                return df.format((Double)value);
            return value.toString();
        }
    }
}
