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
package org.jreserve.gui.misc.logging.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jreserve.gui.misc.logging.LoggerProperties;
import org.jreserve.gui.misc.logging.LoggingSetting;
import org.jreserve.gui.misc.logging.LoggingUtil;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.LoggingPanel.level=Level:",
    "LBL.LoggingPanel.showLog=Show log:",
    "CTL.LoggingPanel.add=Add",
    "CTL.LoggingPanel.delete=Delete",
    "CTL.LoggingPanel.default=Reset default"
})
class LoggingPanel extends javax.swing.JPanel 
    implements ActionListener, ListSelectionListener, TableModelListener {
    
    private final static Level DEFAULT_LEVEL = Level.SEVERE;
    private final LoggingOptionsPanelController controller;
    private LogLevelTableModel levelModel;
    private int selectedRow;

    LoggingPanel(LoggingOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }
    
    void store() {
        Properties props = LoggerProperties.getProperties();
        props.clear();
        props.put(LoggerProperties.SHOW_GUI, showLogCheck.isSelected()? "true" : "false");
        props.put(LoggerProperties.MAIN_LEVEL, getSelectedLevel().getName());
        levelModel.storeLevels(props);
        LoggerProperties.save();
        LoggingSetting.initialize();
    }
    
    private Level getSelectedLevel() {
        Level level = (Level) levelCombo.getSelectedItem();
        return level != null? level : DEFAULT_LEVEL;
    }
    
    boolean valid() {
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(addButton == source) {
            AddLoggerDialog.showDialog(levelModel);
        } else if(defaultButton == source) {
            resetDefault();
        } else if(deleteButton == source) {
            int row = levelTable.getSelectedRow();
            if(row >= 0)
                delete(row);
        } else if (source instanceof JMenuItem) {
            actionPerformed(((JMenuItem)source));
        }
    }
    
    private void resetDefault() {
        Properties props = LoggerProperties.getProperties();
        props.setProperty(LoggerProperties.SHOW_GUI, "false");
        props.setProperty(LoggerProperties.MAIN_LEVEL, Level.SEVERE.getName());
        props.setProperty("org.netbeans.level", Level.WARNING.getName());
        props.setProperty("org.openide.level", Level.WARNING.getName());
        props.setProperty("org.jreserve.level", Level.WARNING.getName());
        LoggerProperties.save();
        load();
    }

    void load() {
        Properties props = LoggerProperties.getProperties();
        setLevelCombo(props.getProperty(LoggerProperties.MAIN_LEVEL));
        setShowGui(props.getProperty(LoggerProperties.SHOW_GUI));
        levelModel.loadProperties(props);
    }
    
    private void setLevelCombo(String strLevel) {
        Level level = strLevel==null? null : Level.parse(strLevel);
        levelCombo.setSelectedItem(level);
    }

    private void setShowGui(String show) {
        boolean checked = show==null? false : "true".equalsIgnoreCase(show);
        showLogCheck.setSelected(checked);
    }
    
    private void delete(int row) {
        levelModel.deleteRow(row);
    }
    
    private void actionPerformed(JMenuItem item) {
        String text = item.getText();
        if(text.equals(Bundle.CTL_LoggingPanel_delete())) {
            delete(selectedRow);
        } else {
            levelModel.setValueAt(item.getActionCommand(), selectedRow, LogLevelTableModel.COLUMN_LEVEL);
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent model) {
        int row = levelTable.getSelectedRow();
        deleteButton.setEnabled(row >= 0);
    }
    
    @Override
    public void tableChanged(TableModelEvent evt) {
        controller.changed();
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

        northPanel = new javax.swing.JPanel();
        levelLabel = new javax.swing.JLabel();
        levelCombo = new javax.swing.JComboBox();
        showLogLabel = new javax.swing.JLabel();
        showLogCheck = new javax.swing.JCheckBox();
        northFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        levelScroll = new javax.swing.JScrollPane();
        levelTable = new javax.swing.JTable();
        southPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        deleteButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        defaultButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.BorderLayout(15, 15));

        northPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(levelLabel, org.openide.util.NbBundle.getMessage(LoggingPanel.class, "LoggingPanel.levelLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        northPanel.add(levelLabel, gridBagConstraints);

        levelCombo.setModel(new DefaultComboBoxModel(new Level[]{Level.SEVERE, Level.WARNING, Level.INFO, Level.FINE, Level.FINER, Level.FINEST, Level.ALL}));
        levelCombo.setPreferredSize(new java.awt.Dimension(100, 22));
        levelCombo.setRenderer(new LevelComboRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        northPanel.add(levelCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(showLogLabel, org.openide.util.NbBundle.getMessage(LoggingPanel.class, "LoggingPanel.showLogLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        northPanel.add(showLogLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(showLogCheck, null);
        showLogCheck.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        northPanel.add(showLogCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        northPanel.add(northFiller, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.PAGE_START);

        levelScroll.setPreferredSize(new java.awt.Dimension(300, 200));

        levelModel = new LogLevelTableModel();
        levelModel.addTableModelListener(this);
        levelTable.setModel(levelModel);
        levelTable.setFillsViewportHeight(true);
        levelTable.addMouseListener(new LoggerPopUp());
        levelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        levelTable.setColumnSelectionAllowed(false);
        levelTable.getSelectionModel().addListSelectionListener(this);
        levelTable.setDefaultRenderer(Level.class, new LogLevelTableRenderer());
        levelTable.setDefaultEditor(Level.class, new LogLevelTableSelectEditor());
        levelScroll.setViewportView(levelTable);

        add(levelScroll, java.awt.BorderLayout.CENTER);

        southPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(LoggingPanel.class, "LoggingPanel.addButton.text")); // NOI18N
        addButton.addActionListener(this);
        southPanel.add(addButton);
        southPanel.add(filler2);

        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(LoggingPanel.class, "LoggingPanel.deleteButton.text")); // NOI18N
        deleteButton.addActionListener(this);
        southPanel.add(deleteButton);
        southPanel.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(defaultButton, org.openide.util.NbBundle.getMessage(LoggingPanel.class, "LoggingPanel.defaultButton.text")); // NOI18N
        defaultButton.addActionListener(this);
        southPanel.add(defaultButton);

        add(southPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton defaultButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JComboBox levelCombo;
    private javax.swing.JLabel levelLabel;
    private javax.swing.JScrollPane levelScroll;
    private javax.swing.JTable levelTable;
    private javax.swing.Box.Filler northFiller;
    private javax.swing.JPanel northPanel;
    private javax.swing.JCheckBox showLogCheck;
    private javax.swing.JLabel showLogLabel;
    private javax.swing.JPanel southPanel;
    // End of variables declaration//GEN-END:variables

    private class LoggerPopUp extends MouseAdapter {

        private JPopupMenu popUp;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }
        
        private void popUp(MouseEvent e) {
            selectedRow = levelTable.rowAtPoint(e.getPoint());
            if(selectedRow < 0)
                return;
            if(popUp == null)
                createPopUp();
            popUp.show(levelTable, e.getX(), e.getY());
        }
        
        private void createPopUp() {
            popUp = new JPopupMenu();
            JMenuItem delete = new JMenuItem(Bundle.CTL_LoggingPanel_delete());
            delete.addActionListener(LoggingPanel.this);
            popUp.add(delete);
            popUp.add(new JSeparator());
            popUp.add(getMenuItem(Level.OFF));
            popUp.add(getMenuItem(Level.SEVERE));
            popUp.add(getMenuItem(Level.WARNING));
            popUp.add(getMenuItem(Level.INFO));
            popUp.add(getMenuItem(Level.CONFIG));
            popUp.add(getMenuItem(Level.FINE));
            popUp.add(getMenuItem(Level.FINER));
            popUp.add(getMenuItem(Level.ALL));
        }
        
        private JMenuItem getMenuItem(Level level) {
            String text = LoggingUtil.getUserName(level);
            JMenuItem item = new JMenuItem(text);
            item.addActionListener(LoggingPanel.this);
            item.setActionCommand(level.getName());
            return item;
        }
    }
}
