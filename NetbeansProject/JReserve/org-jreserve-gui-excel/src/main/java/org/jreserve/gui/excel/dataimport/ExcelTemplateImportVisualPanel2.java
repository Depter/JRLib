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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jreserve.gui.data.api.util.DataItemChooser;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.inport.ImportSettings;
import org.jreserve.gui.data.api.inport.SaveType;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.jrlib.gui.data.DataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExcelTemplateImportVisualPanel2.Name=Link Template"
})
class ExcelTemplateImportVisualPanel2 extends javax.swing.JPanel {
    
    private final static int MAX_IMG_WIDTH = 40;
    
    private final InputListener inputListener = new InputListener();
    private final DataSourceRenderer dsRenderer = new DataSourceRenderer();
    private final ExcelTemplateImportTableModel tableModel = new ExcelTemplateImportTableModel();
    private final ExcelTemplateImportWizardPanel2 controller;
    private DataManager dm;
    
    public ExcelTemplateImportVisualPanel2(ExcelTemplateImportWizardPanel2 controller) {
        this.controller = controller;
        initComponents();
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ExcelTemplateImportVisualPanel2_Name();
    }
    
    void setSuccess(DataImportTemplateItem item, Exception ex) {
        tableModel.setSuccess(item, ex);
    }
    
    void setItems(List<DataImportTemplateItem> items) {
        tableModel.setItems(items);
        setColumnWidths(ExcelTemplateImportTableModel.ITEM_USED_COLUMN);
        setColumnWidths(ExcelTemplateImportTableModel.SUCCESS_COLUMN);
    }
    
    private void setColumnWidths(int index) {
        TableColumn column = table.getColumnModel().getColumn(index);
        column.setMaxWidth(MAX_IMG_WIDTH);
        column.setMinWidth(MAX_IMG_WIDTH);
        column.setWidth(MAX_IMG_WIDTH);
    }

    void setDataManager(DataManager dm) {
        this.dm = dm;
    }
    
    SaveType getSaveType() {
        return (SaveType) saveTypeCombo.getSelectedItem();
    }
    
    List<DataImportTemplateItem> getSelectedItems() {
        return tableModel.getSelectedItems();
    }
    
    DataSource getDataSource(DataImportTemplateItem item) {
        return tableModel.getDataSource(item);
    }
    
    void setProcessRunning(boolean running) {
        saveTypeCombo.setEnabled(!running);
        table.setEnabled(!running);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        saveTypeLabel = new javax.swing.JLabel();
        saveTypeCombo = new javax.swing.JComboBox();
        tableLabel = new javax.swing.JLabel();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        pBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(saveTypeLabel, org.openide.util.NbBundle.getMessage(ExcelTemplateImportVisualPanel2.class, "ExcelTemplateImportVisualPanel2.saveTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 5);
        add(saveTypeLabel, gridBagConstraints);

        saveTypeCombo.setModel(new DefaultComboBoxModel(SaveType.values()));
        saveTypeCombo.setSelectedItem(ImportSettings.getSaveType());
        saveTypeCombo.setRenderer(WidgetUtils.displayableListRenderer());
        saveTypeCombo.addActionListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        add(saveTypeCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(tableLabel, org.openide.util.NbBundle.getMessage(ExcelTemplateImportVisualPanel2.class, "ExcelTemplateImportVisualPanel2.tableLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(tableLabel, gridBagConstraints);

        tableScroll.setPreferredSize(new java.awt.Dimension(450, 250));

        table.setModel(tableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.addMouseListener(new TableMouseListener());
        table.addKeyListener(new TableKeyListener());
        table.getModel().addTableModelListener(inputListener);
        table.setDefaultRenderer(DataImportTemplateItem.class, new ItemRenderer());
        table.setDefaultRenderer(Boolean.class, new UsedRenderer());
        table.setDefaultRenderer(DataSource.class, dsRenderer);
        table.setDefaultRenderer (Exception.class, new SuccessRenderer());
        tableScroll.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        add(tableScroll, gridBagConstraints);

        pBar.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar pBar;
    private javax.swing.JComboBox saveTypeCombo;
    private javax.swing.JLabel saveTypeLabel;
    private javax.swing.JTable table;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
    
    private class InputListener implements ActionListener, TableModelListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.changed();
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            controller.changed();
        }
    }
    
    private static class ItemRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(value instanceof DataImportTemplateItem)
                render((DataImportTemplateItem)value);
            else
                render();
            return this;
        }
        
        private void render(DataImportTemplateItem item) {
            setText(item.getReference());
            setIcon(item.getDataType().getIcon());
        }
        
        private void render() {
            setText(null);
            setIcon(EmptyIcon.EMPTY_16);
        }
    }

    private class SuccessRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(null);
            
            if(!tableModel.hasSuccess(row)) {
                setIcon(EmptyIcon.EMPTY_16);
                setToolTipText(null);
            } else {
                if(value instanceof Exception) {
                    setIcon(CommonIcons.cancel());
                    setToolTipText(((Exception)value).getLocalizedMessage());
                } else {
                    setIcon(CommonIcons.ok());
                    setToolTipText(null);
                }
            }
            return this;
        }
    }
    
    private static class UsedRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(null);
            if(value == null) {
                setIcon(EmptyIcon.EMPTY_16);
            } else if(Boolean.TRUE == value) {
                setIcon(CommonIcons.ok());
            } else {
                setIcon(CommonIcons.cancel());
            }
            return this;
        }
    }

    private static class DataSourceRenderer extends JPanel implements TableCellRenderer {
        
        private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        private final JButton button = new JButton("...");
        private DataSourceRenderer() {
            setLayout(new BorderLayout());
            add(renderer, BorderLayout.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            add(button, BorderLayout.EAST);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if(value instanceof DataSource) {
                render((DataSource) value);
            } else {
                renderEmpty();
            }
            
            if(!isSelected)
                setBackground(Color.WHITE);
            return this;
        }
        
        private void render(DataSource ds) {
            renderer.setText(ds.getName());
            this.setToolTipText(ds.getPath());
            renderer.setIcon(ds.getDataType().getIcon());
        }
        
        private void renderEmpty() {
            this.setToolTipText(null);
            renderer.setIcon(EmptyIcon.EMPTY_16);
        }
    }
    
    private class TableMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            int row = table.rowAtPoint(e.getPoint());
            if(row < 0)
                return;
            
            int column = table.columnAtPoint(point);
            if(column == ExcelTemplateImportTableModel.ITEM_USED_COLUMN) {
                switchUsed(row);
                return;
            } else if(column != ExcelTemplateImportTableModel.DATA_SOURCE_COLUMN) {
                return;
            }
            
            DataSourceRenderer renderer = (DataSourceRenderer)table.prepareRenderer(dsRenderer, row, column);
            Point cellLoc = table.getCellRect(row, column, false).getLocation();
            point = new Point(point.x-cellLoc.x, point.y - cellLoc.y);
            point = getPointWithin(point, renderer.button);
            if(renderer.button.contains(point) && dm != null)
                selectDataSource(row);
        }
        
        private Point getPointWithin(Point point, Component component) {
            Point cLocation = component.getLocation();
            return new Point(point.x-cLocation.x, point.y - cLocation.y);
        }
        
        private void selectDataSource(int row) {
            DataType dt = tableModel.getItem(row).getDataType();
            DataSource ds = DataItemChooser.chooseSource(dm, dt);
            if(ds != null)
                tableModel.setDataSource(row, ds);
        }
        
        private void switchUsed(int row) {
            tableModel.setUsed(row, !tableModel.isUsed(row));
        }
    }
    
    private class TableKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if(KeyEvent.VK_SPACE != e.getKeyCode())
                return;
            
            int row = table.getSelectedRow();
            if(row >= 0)
                tableModel.setUsed(row, !tableModel.isUsed(row));
        }
    }
}