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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.data.api.DataEntryLoader;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.inport.ImportDataAction;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.localesettings.ScaleSpinner;
import org.jreserve.gui.localesettings.TableToClipboardRenderers;
import org.jreserve.gui.misc.utils.actions.deletable.Deletable;
import org.jreserve.gui.misc.utils.actions.deletable.DeleteAction;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.SwingCallback;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.actions.CopyAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.DataSourceTablePanel.ProgressTitle=Loading data from ''{0}''.",
    "# {0} - path",
    "MSG.DataSourceTablePanel.LoadError=Loading data from ''{0}'' failed!",
    "LBL.DataSourceTablePanel.ToolTip.Filter=Filter the table...",
    "LBL.DataSourceTablePanel.ToolTip.ClearFilter=Clear filter",
    "LBL.DataSourceTablePanel.ToolTip.Copy=Copy to clipboard"
})
class DataSourceTablePanel extends JPanel {
    
    private final static String LOADING_CARD = "loadingCard";  //NOI18
    private final static String TABLE_CARD = "tableCard";  //NOI18
    
    private CardLayout layout;
    private EditorTableModel tableModel;
    private JTable table;
    private JLabel loadLabel;
    private DataSource ds;
    private DataEntryRenderer renderer;
    private JToolBar toolBar;
    private JToggleButton filterButton;
    private ClearFilterAction clearFilterAction;
    private CopyAction copyAction;
    private Action importDataAction;
    private FilterPanel filterPanel;
    private InstanceContent ic = new InstanceContent();
    private Lookup lkp = new AbstractLookup(ic);
    private JPanel tablePanel;
    
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
        table.getSelectionModel().addListSelectionListener(new RowDeletable());
        
        renderer = new DataEntryRenderer();
        table.setDefaultRenderer(Double.class, renderer);
        table.setDefaultRenderer(MonthDate.class, renderer);
        
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, TABLE_CARD);
        
        filterPanel = new FilterPanel();
        filterPanel.addChangeListener(new FilterListener());
        
        loadLabel = new JLabel("Loading...");
        loadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(loadLabel, LOADING_CARD);
    }
    
    void loadEntries() {
        layout.show(this, LOADING_CARD);
        table.clearSelection();
        setActionsEnabled(false);
        
        String msg = Bundle.MSG_DataSourceTablePanel_ProgressTitle(ds.getPath());
        TaskUtil.execute(new DataEntryLoader(ds), new LoaderCallback(), msg);
    }
    
    private void setActionsEnabled(boolean enable) {
        filterButton.setEnabled(enable);
        clearFilterAction.setEnabled(enable);
        copyAction.setEnabled(enable);
        importDataAction.setEnabled(enable);
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
        filterButton = new JToggleButton(CommonIcons.filter());
        filterButton.setToolTipText(Bundle.LBL_DataSourceTablePanel_ToolTip_Filter());
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filterButton.isSelected())
                    tablePanel.add(filterPanel, BorderLayout.NORTH);
                else
                    tablePanel.remove(filterPanel);
                tablePanel.revalidate();
            }
        });
        toolBar.add(filterButton);
        toolBar.add(Box.createHorizontalStrut(5));
        
        clearFilterAction = new ClearFilterAction();
        toolBar.add(clearFilterAction);
        toolBar.add(Box.createHorizontalStrut(5));
        
        copyAction = SystemAction.get(CopyAction.class);
        getActionMap().put(copyAction.getActionMapKey(), new CopyTableAction());
        
        toolBar.add(copyAction);
        toolBar.add(Box.createHorizontalStrut(5));
        
        toolBar.add(DeleteAction.createSmall(lkp));
        toolBar.add(Box.createHorizontalStrut(5));
        
        importDataAction = ImportDataAction.createSmall(Lookups.singleton(ds));
        toolBar.add(importDataAction);

        toolBar.setBorderPainted(false);
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
    }
    
    JComponent getToolBar() {
        return toolBar;
    }
    
    Lookup getLookup() {
        return lkp;
    }
    
    private class LoaderCallback implements SwingCallback<List<DataEntry>> {

        @Override
        public void finnished(List<DataEntry> result) {
            filterPanel.setBounds(result);
            tableModel.setEntries(result);
            layout.show(DataSourceTablePanel.this, TABLE_CARD);
            setActionsEnabled(true);
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
    
    private class FilterListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            tableModel.setFilter(filterPanel.createFilter());
        }
    }
    
    private class ClearFilterAction extends AbstractAction {
        ClearFilterAction() {
            putValue(SHORT_DESCRIPTION, Bundle.LBL_DataSourceTablePanel_ToolTip_ClearFilter());
            putValue(SMALL_ICON, CommonIcons.clearFilter());
        }
        
        @Override
        public void actionPerformed(ActionEvent evt) {
            filterPanel.clearFilter();
        }
    }
    
    private class CopyTableAction extends AbstractAction {

        CopyTableAction() {
            putValue(SHORT_DESCRIPTION, Bundle.LBL_DataSourceTablePanel_ToolTip_Copy());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            TableToClipboardRenderers.writeToClipboard(tableModel);
        }
    }
    
    private class RowDeletable implements Deletable, ListSelectionListener {
        
        private Set<DataEntry> entries = new TreeSet<DataEntry>();

        @Override
        public void valueChanged(ListSelectionEvent e) {
            entries.clear();
            for(int row : table.getSelectedRows())
                entries.add(tableModel.getEntryAt(row));
            
            if(entries.isEmpty()) {
                if(lkp.lookup(RowDeletable.class) != null)
                    ic.remove(this);
            } else {
                if(lkp.lookup(RowDeletable.class) == null)
                    ic.add(this);
            }
        }
        
        @Override
        public void delete() throws Exception {
            ds.deleteEntries(entries);
        }

        @Override
        public Icon getIcon() {
            return EmptyIcon.EMPTY_16;
        }

        @Override
        public String getDisplayName() {
            String msg = "%d records from %s";
            return String.format(msg, entries.size(), ds.getPath());
        }

        @Override
        public Object getDeletedObject() {
            return entries;
        }
    }
}
