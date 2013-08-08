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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.settings.ImportSettings;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.gui.data.api.SaveType;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.localesettings.LocaleSettings.DecimalFormatter;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportDataWizardVisualPanelLast.Name=Select Data",
    "LBL.ImportDataWizardVisualPanelLast.Overview.Triangle=Triangle",
    "LBL.ImportDataWizardVisualPanelLast.Overview.Table=Table",
    "# {0} - path",
    "MSG.ImportDataWizardVisualPanelLast.LoadError=Unable to load data for ''{0}''!"
})
class ImportDataWizardVisualPanelLast extends javax.swing.JPanel {
    
    private final static Color COLOR_NEW = new Color(175, 246, 68);
    private final static Color COLOR_EXISTS = new Color(190, 190, 190);
    
    private static enum OverviewType implements Displayable {
        TRIANGLE(Bundle.LBL_ImportDataWizardVisualPanelLast_Overview_Triangle()),
        TABLE(Bundle.LBL_ImportDataWizardVisualPanelLast_Overview_Table());
        
        private final String displayName;
        
        private OverviewType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Icon getIcon() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private final ImportDataWizardPanelLast controller;
    private final ImportDataTableModel tableModel = new ImportDataTableModel();
    private DataSource ds;
    private List<DataEntry> entries;
    private EntryLoader loader;
    private List<DataEntry> existingEntries = Collections.EMPTY_LIST;
    private TableRenderer tableRenderer;
    private TriangleRenderer triangleRenderer = new TriangleRenderer();
    private TriangleUtil triangleUtil;
    
    private SaveType saveType = SaveType.SAVE_NEW;
    private DecimalFormatter df = LocaleSettings.createDecimalFormat();
    
    ImportDataWizardVisualPanelLast(ImportDataWizardPanelLast controller) {
        this.controller = controller;
        initComponents();
        CardLayout layout = (CardLayout) overviewPanel.getLayout();
        layout.show(overviewPanel, "triangle");
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ImportDataWizardVisualPanelLast_Name();
    }
    
    SaveType getSaveType() {
        return (SaveType) saveTypeCombo.getSelectedItem();
    }
    
    boolean isInputCummulated() {
        return cummulatedCheck.isSelected();
    }
    
    void setDataSource(DataSource ds) {
        if(this.ds != ds) {
            this.ds = ds;
            tableModel.setDataType(ds.getDataType());
            startLoader();
        }
    }
    
    private void startLoader() {
        if(loader != null && !loader.isDone())
            loader.cancel(false);
        loader = new EntryLoader(ds);
        setProgressRunning(true);
        loader.execute();
    }
    
    void setEntries(List<DataEntry> entries) {
        this.entries = entries;
        tableModel.setEntries(entries);
        
        triangleUtil = new TriangleUtil(entries);
        triangleUtil.setRenderer(triangleRenderer);
        triangleWidget.setModel(triangleUtil.getTriangleModel());
        triangleWidget.setLayers(triangleUtil.getLayers());
    }
    
    private void decimalSpinnerChanged() {
        df.setDecimalCount((Integer) decimalSpinner.getValue());
        table.repaint();
        triangleWidget.repaint();
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
        overviewTypeLabel = new javax.swing.JLabel();
        overviewTypeCombo = new javax.swing.JComboBox();
        cummulatedLabel = new javax.swing.JLabel();
        cummulatedCheck = new javax.swing.JCheckBox();
        decimalLabel = new javax.swing.JLabel();
        decimalSpinner = new org.jreserve.gui.localesettings.ScaleSpinner();
        overviewPanel = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        triangleScroll = new javax.swing.JScrollPane();
        triangleWidget = new org.jreserve.gui.trianglewidget.TriangleWidget();
        pBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(saveTypeLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanelLast.class, "ImportDataWizardVisualPanelLast.saveTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(saveTypeLabel, gridBagConstraints);

        saveTypeCombo.setModel(new DefaultComboBoxModel(SaveType.values()));
        saveTypeCombo.setSelectedItem(ImportSettings.getSaveType());
        saveTypeCombo.setRenderer(WidgetUtils.displayableListRenderer());
        saveTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTypeComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(saveTypeCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(overviewTypeLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanelLast.class, "ImportDataWizardVisualPanelLast.overviewTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(overviewTypeLabel, gridBagConstraints);

        overviewTypeCombo.setModel(new DefaultComboBoxModel(OverviewType.values()));
        overviewTypeCombo.setSelectedItem(OverviewType.TRIANGLE);
        overviewTypeCombo.setRenderer(WidgetUtils.displayableListRenderer());
        overviewTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overviewTypeComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(overviewTypeCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cummulatedLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanelLast.class, "ImportDataWizardVisualPanelLast.cummulatedLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(cummulatedLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cummulatedCheck, null);
        cummulatedCheck.setSelected(ImportSettings.isImportCummulated());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(cummulatedCheck, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(decimalLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanelLast.class, "ImportDataWizardVisualPanelLast.decimalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalLabel, gridBagConstraints);

        decimalSpinner.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                decimalSpinnerChanged();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalSpinner, gridBagConstraints);

        overviewPanel.setPreferredSize(new java.awt.Dimension(450, 250));
        overviewPanel.setLayout(new java.awt.CardLayout());

        table.setModel(tableModel);
        tableRenderer = new TableRenderer();
        table.setDefaultRenderer(MonthDate.class, tableRenderer);
        table.setDefaultRenderer(Double.class, tableRenderer);
        tableScroll.setViewportView(table);

        overviewPanel.add(tableScroll, "table");

        triangleScroll.setViewportView(triangleWidget);

        overviewPanel.add(triangleScroll, "triangle");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(overviewPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(pBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void overviewTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overviewTypeComboActionPerformed
        CardLayout layout = (CardLayout) overviewPanel.getLayout();
        if(OverviewType.TABLE == overviewTypeCombo.getSelectedItem())
            layout.show(overviewPanel, "table");
        else
            layout.show(overviewPanel, "triangle");
    }//GEN-LAST:event_overviewTypeComboActionPerformed

    private void saveTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTypeComboActionPerformed
        saveType = (SaveType) saveTypeCombo.getSelectedItem();
        
        table.repaint();
        triangleWidget.repaint();
        
        controller.changed();
    }//GEN-LAST:event_saveTypeComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cummulatedCheck;
    private javax.swing.JLabel cummulatedLabel;
    private javax.swing.JLabel decimalLabel;
    private org.jreserve.gui.localesettings.ScaleSpinner decimalSpinner;
    private javax.swing.JPanel overviewPanel;
    private javax.swing.JComboBox overviewTypeCombo;
    private javax.swing.JLabel overviewTypeLabel;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JComboBox saveTypeCombo;
    private javax.swing.JLabel saveTypeLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JScrollPane triangleScroll;
    private org.jreserve.gui.trianglewidget.TriangleWidget triangleWidget;
    // End of variables declaration//GEN-END:variables
    
    private void setProgressRunning(boolean running) {
        saveTypeCombo.setEnabled(!running);
        overviewTypeCombo.setEnabled(!running);
        cummulatedCheck.setEnabled(!running);
        decimalSpinner.setEnabled(!running);
        pBar.setIndeterminate(running);
        pBar.setVisible(running);
    }
    
    boolean hasEntriesToSave() {
        if(entries==null || entries.isEmpty())
            return false;
        if(SaveType.OVERRIDE_EXISTING == saveTypeCombo.getSelectedItem())
            return true;
        if(existingEntries==null || existingEntries.isEmpty())
            return true;
        
        for(DataEntry entry : entries)
            if(!existingEntries.contains(entry))
                return true;
        return false;
    }
    
    private class EntryLoader extends SwingWorker<List<DataEntry>, Void> {
        
        private final DataSource source;
        
        private EntryLoader(DataSource ds) {
            this.source = ds;
        }
        
        @Override
        protected List<DataEntry> doInBackground() throws Exception {
            if(source == null)
                return Collections.EMPTY_LIST;
            return source.getEntries(DataEntryFilter.ALL);
        }

        @Override
        protected void done() {
            try {
                existingEntries = get();
            } catch (Exception ex) {
                existingEntries = Collections.EMPTY_LIST;
                BubbleUtil.showException(Bundle.MSG_ImportDataWizardVisualPanelLast_LoadError(ds.getPath()), ex);
            } finally {
                setProgressRunning(false);
                tableModel.fireTableDataChanged();
                controller.changed();
            }
        }
    }
    
    private class TableRenderer extends DefaultTableCellRenderer {
        
        private final Color alternate = UIManager.getColor("Table.alternateRowColor");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.RIGHT);
            
            if(value instanceof Double)
                setText(df.format(((Double)value).doubleValue()));
            
            if(!isSelected)
                setBgColor(row);
            
            return this;
        }
        
        private void setBgColor(int row) {
            Color c = null;
            if(SaveType.SAVE_NEW == saveType)
                c = getColor(row);
            if(c==null && row%2 == 1)
                c = alternate;
            if(c == null)
                c = Color.WHITE;
            setBackground(c);
        }
        
        private Color getColor(int row) {
            DataEntry entry = tableModel.getEntry(row);
            if(existingEntries.contains(entry)) {
                return COLOR_EXISTS;
            } else {
                return COLOR_NEW;
            }
        }
    }
    
    private class TriangleRenderer extends DefaultTriangleWidgetRenderer {
        
        @Override
        public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected) {
            super.getComponent(widget, value, row, column, selected);
            setText(df.format(value));
            setBackground(getBgColor(row, column));
            return this;
        }
        
        private Color getBgColor(int row, int column) {
            if(triangleUtil == null || SaveType.SAVE_NEW != saveType)
                return Color.WHITE;
            DataEntry entry = triangleUtil.getEntry(row, column);
            
            if(existingEntries.contains(entry)) {
                return COLOR_EXISTS;
            } else {
                return COLOR_NEW;
            }
        }
    }
}
