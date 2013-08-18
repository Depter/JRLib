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

import org.jreserve.gui.data.api.DataSource;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataEditorTopComponent extends TopComponent {

    private final Lookup lkp;
    
    public DataEditorTopComponent(DataSource dataSource) {
        lkp = Lookups.singleton(dataSource);
        initComponents();
    }

    @Override
    public Lookup getLookup() {
        return lkp;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        split = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        tableLabel = new javax.swing.JLabel();
        tableFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        deleteButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        rightPanel = new javax.swing.JPanel();
        accidentLabel = new javax.swing.JLabel();
        accidentFromCheck = new javax.swing.JCheckBox();
        accidentFromSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        accidentToCheck = new javax.swing.JCheckBox();
        accidentToSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        developmentLabel = new javax.swing.JLabel();
        developmentFromCheck = new javax.swing.JCheckBox();
        developmentFromSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        developmentToCheck = new javax.swing.JCheckBox();
        developmentToSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        rightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setLayout(new java.awt.BorderLayout());

        split.setDividerLocation(200);
        split.setResizeWeight(1.0);

        leftPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(tableLabel, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.tableLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        leftPanel.add(tableLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        leftPanel.add(tableFiller, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.deleteButton.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        leftPanel.add(deleteButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(refreshButton, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.refreshButton.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        leftPanel.add(refreshButton, gridBagConstraints);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableScroll.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        leftPanel.add(tableScroll, gridBagConstraints);

        split.setLeftComponent(leftPanel);

        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        rightPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(accidentLabel, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.accidentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        rightPanel.add(accidentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentFromCheck, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.accidentFromCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 5);
        rightPanel.add(accidentFromCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        rightPanel.add(accidentFromSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentToCheck, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.accidentToCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 20, 5);
        rightPanel.add(accidentToCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        rightPanel.add(accidentToSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentLabel, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.developmentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        rightPanel.add(developmentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentFromCheck, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.developmentFromCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 5);
        rightPanel.add(developmentFromCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        rightPanel.add(developmentFromSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentToCheck, org.openide.util.NbBundle.getMessage(DataEditorTopComponent.class, "DataEditorTopComponent.developmentToCheck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        rightPanel.add(developmentToCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        rightPanel.add(developmentToSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightPanel.add(rightFiller, gridBagConstraints);

        split.setRightComponent(rightPanel);

        add(split, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox accidentFromCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentFromSpinner;
    private javax.swing.JLabel accidentLabel;
    private javax.swing.JCheckBox accidentToCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentToSpinner;
    private javax.swing.JButton deleteButton;
    private javax.swing.JCheckBox developmentFromCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentFromSpinner;
    private javax.swing.JLabel developmentLabel;
    private javax.swing.JCheckBox developmentToCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentToSpinner;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.Box.Filler rightFiller;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JSplitPane split;
    private javax.swing.JTable table;
    private javax.swing.Box.Filler tableFiller;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }
    
}
