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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.util.DataEntryLoader;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.localesettings.ScaleSpinner;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.SwingCallback;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.actions.CopyAction;
import org.openide.actions.DeleteAction;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;

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
    
    private final static String FILTER_IMG = "org/jreserve/gui/data/icons/filter.png";  //NOI18
    private final static String LOADING_CARD = "loadingCard";  //NOI18
    private final static String TABLE_CARD = "tableCard";  //NOI18
    
    private CardLayout layout;
    private EditorTableModel tableModel;
    private JTable table;
    private JLabel loadLabel;
    private DataSource ds;
    private DataEntryRenderer renderer;
    private JToolBar toolBar;
    
    DataSourceTablePanel(DataSource ds) {
        this.ds = ds;
        tableModel = new EditorTableModel(ds.getDataType());
        initComponents();
        initToolbar();
        loadEntries();
    }
    
    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);
        
        renderer = new DataEntryRenderer();
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
    
    private void initToolbar() {
        toolBar = new JToolBar();
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(new JLabel("Scale:"));
        final ScaleSpinner scale = new ScaleSpinner();
        scale.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                renderer.df.setDecimalCount((Integer)scale.getValue());
                table.repaint();
                table.revalidate();
            }
        });
        scale.setMaximumSize(new Dimension(scale.getMaximumSize().width, 20));
        scale.setPreferredSize(new Dimension(scale.getPreferredSize().width, 20));
        toolBar.add(scale);
        
        toolBar.addSeparator();
        toolBar.add(new FilterAction());
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(SystemAction.get(CopyAction.class));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(SystemAction.get(DeleteAction.class));
        
        toolBar.setBorderPainted(false);
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
    }
    
//    private void registerDeleteAction() {
//        KeyStroke stroke = KeyStroke.getKeyStroke(DELETE_KEY);
//        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DELETE_ACTION_KEY);
//        table.getInputMap().put(stroke, DELETE_ACTION_KEY);
//        getActionMap().put(DELETE_ACTION_KEY, new DeleteDataAction());
//    }
    
    
    JComponent getToolBar() {
        return toolBar;
    }
    
    private static class DataEntryRenderer extends DefaultTableCellRenderer {
        
        LocaleSettings.DecimalFormatter df =  LocaleSettings.createDecimalFormatter();
        
        private DataEntryRenderer() {
            super.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
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
    
    private class FilterAction extends AbstractAction {
        
        private FilterAction() {
            putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(FILTER_IMG, false));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    
    }
}
