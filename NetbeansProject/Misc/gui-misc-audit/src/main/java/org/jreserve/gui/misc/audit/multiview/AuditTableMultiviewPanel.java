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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.jreserve.gui.misc.audit.api.Auditable;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.SwingCallback;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.TableToClipboard;
import org.openide.actions.CopyAction;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.AuditTableMultiviewPanel.Loading=Loading...",
    "MSG.AuditTableMultiviewPanel.Loading=Loading audit records...",
    "MSG.AuditTableMultiviewPanel.Loading.Error=Unable to load audit records!",
    "LBL.AuditTableMultiviewPanel.Sort.Date.Ascending=Date, Ascending",
    "LBL.AuditTableMultiviewPanel.Sort.Date.Descending=Date, Descending",
    "LBL.AuditTableMultiviewPanel.Sort.User.Ascending=User, Ascending",
    "LBL.AuditTableMultiviewPanel.Sort.User.Descending=User, Descending",
    "LBL.AuditTableMultiviewPanel.ToolTip.Filter=Filter table",
    "LBL.AuditTableMultiviewPanel.ToolTip.ClearFilter=Clear filter",
    "LBL.AuditTableMultiviewPanel.ToolTip.Copy=Copy table contents",
    "LBL.AuditTableMultiviewPanel.ToolTip.Refresh=Refresh table"
})
public class AuditTableMultiviewPanel extends JPanel {
    private final static String LOADING_CARD = "loadingCard";  //NOI18
    private final static String TABLE_CARD = "tableCard";  //NOI18

    private Auditable auditable;
    private CardLayout layout;
    private AuditRecordTableModel tableModel = new AuditRecordTableModel();
    private JTable table;
    private JLabel loadLabel;
    private AuditRecordRenderer renderer;
    private JToolBar toolBar;
    private CopyTableAction copyAction = new CopyTableAction();
    private ClearFilterAction clearFilterAction = new ClearFilterAction();
    private RefreshAction refreshAction = new RefreshAction();
    private CopyTableAction copyTableAction = new CopyTableAction();
    private FilterPanel filterPanel;
    private JToggleButton filterButton;
    private JPanel tablePanel;
    private List<AuditRecord> records = Collections.EMPTY_LIST;
    
    public AuditTableMultiviewPanel(Auditable auditable) {
        this.auditable = auditable;
        initComponents();
        initToolbar();
        loadRecords();
    }
    
    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        renderer = new AuditRecordRenderer();
        table.setDefaultRenderer(Date.class, renderer);
        table.setDefaultRenderer(String.class, renderer);
        
        RowSorter<AuditRecordTableModel> sorter = new TableRowSorter(tableModel);
        table.setRowSorter(sorter);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        filterPanel = new FilterPanel();
        filterPanel.addChangeListener(new FilterListener());
        
        add(tablePanel, TABLE_CARD);
        
        loadLabel = new JLabel(Bundle.LBL_AuditTableMultiviewPanel_Loading());
        loadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(loadLabel, LOADING_CARD);
    }
    private void initToolbar() {
        toolBar = new JToolBar();
        toolBar.add(Box.createHorizontalStrut(5));
        
        toolBar.addSeparator();
        
        filterButton = new JToggleButton(CommonIcons.filter());
        filterButton.setToolTipText(Bundle.LBL_AuditTableMultiviewPanel_ToolTip_Filter());
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filterButton.isSelected())
                    tablePanel.add(filterPanel, BorderLayout.NORTH);
                else
                    tablePanel.remove(filterPanel);
                tablePanel.validate();
            }
        });
        toolBar.add(filterButton);
        toolBar.add(Box.createHorizontalStrut(5));
        
        toolBar.add(clearFilterAction);
        toolBar.add(Box.createHorizontalStrut(5));
        
        CopyAction ca = SystemAction.get(CopyAction.class);
        getActionMap().put(ca.getActionMapKey(), copyTableAction);
        toolBar.add(ca);
        toolBar.add(Box.createHorizontalStrut(5));
        
        toolBar.add(refreshAction);
        toolBar.add(Box.createHorizontalStrut(5));
        
        toolBar.setBorderPainted(false);
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
    }
    
    private void loadRecords() {
        layout.show(this, LOADING_CARD);
        tableModel.setRecords(Collections.EMPTY_LIST);
        setActionsEnabled(false);
        
        String msg = Bundle.MSG_AuditTableMultiviewPanel_Loading();
        TaskUtil.execute(new AuditRecordLoader(auditable), new LoaderCallback(), msg);
    }
    
    private void setActionsEnabled(boolean enabled) {
        filterButton.setEnabled(enabled);
        clearFilterAction.setEnabled(enabled);
        copyAction.setEnabled(enabled);
        refreshAction.setEnabled(enabled);
    }
    
    public JComponent getToolBar() {
        return toolBar;
    }
    
    private class FilterListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            tableModel.setRecords(filterPanel.filterRecords(records));
        }
    }
    
    private class ClearFilterAction extends AbstractAction {
        ClearFilterAction() {
            putValue(SMALL_ICON, CommonIcons.clearFilter());
            putValue(SHORT_DESCRIPTION, Bundle.LBL_AuditTableMultiviewPanel_ToolTip_ClearFilter());
        }
        
        @Override
        public void actionPerformed(ActionEvent evt) {
            filterPanel.clearFilter();
        }
    }
    
    private class LoaderCallback implements SwingCallback<List<AuditRecord>> {

        @Override
        public void finnished(List<AuditRecord> result) {
            updateRecords(result);
            layout.show(AuditTableMultiviewPanel.this, TABLE_CARD);
            setActionsEnabled(true);
        }
        
        private void updateRecords(List<AuditRecord> newRecords) {
            records = newRecords;
            filterPanel.initFromRecords(records);
            tableModel.setRecords(filterPanel.filterRecords(records));
        }

        @Override
        public void finnishedWithException(Exception ex) {
            updateRecords(Collections.EMPTY_LIST);
            String msg = Bundle.MSG_AuditTableMultiviewPanel_Loading_Error();
            BubbleUtil.showException(msg, ex);
            layout.show(AuditTableMultiviewPanel.this, TABLE_CARD);
        }
    }
    
    private class CopyTableAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            TableToClipboard ttc = new TableToClipboard();
            ttc.setRenderer(Date.class, renderer);
            ttc.toClipboard(tableModel, true);
        }
    }
    
    private class AuditRecordRenderer extends DefaultTableCellRenderer implements TableToClipboard.Renderer {
        
        private DateFormat df;
        
        private AuditRecordRenderer() {
            String format = System.getProperty("default.date.format");
            if(format == null) {
                df = new SimpleDateFormat();
            } else {
                try {
                    df = new SimpleDateFormat(format);
                } catch (Exception ex) {
                    df = new SimpleDateFormat();
                }
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof Date)
                value = df.format((Date)value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        @Override
        public String toString(Object value) {
            if(value == null)
                return "";
            if(value instanceof Date)
                return df.format((Date)value);
            return value.toString();
        }
    }
    
    private class RefreshAction extends AbstractAction {
        private RefreshAction() {
            putValue(Action.SMALL_ICON, CommonIcons.refresh());
            putValue(SHORT_DESCRIPTION, Bundle.LBL_AuditTableMultiviewPanel_ToolTip_Refresh());
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadRecords();
        }
    }
}
