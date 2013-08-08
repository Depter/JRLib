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
package org.jreserve.gui.trianglewidget;

import javax.swing.DefaultComboBoxModel;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.trianglewidget.model.registration.TriangleModelAdapter;
import org.jreserve.gui.trianglewidget.model.registration.TriangleModelRegistry;

/**
 *
 * @author Peti
 */
public class TriangleWidgetPanel extends javax.swing.JPanel {

    public TriangleWidgetPanel() {
        initComponents();
    }
    
    public void setLayers(List<TriangleLayer> layers) {
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        toolBarPanel = new javax.swing.JPanel();
        toolBarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        cumulatedLabel = new javax.swing.JLabel();
        cummulatedCheck = new javax.swing.JCheckBox();
        scaleLabel = new javax.swing.JLabel();
        scaleSpinner = new org.jreserve.gui.localesettings.ScaleSpinner();
        layoutLabel = new javax.swing.JLabel();
        layoutCombo = new javax.swing.JComboBox();
        layerLabel = new javax.swing.JLabel();
        layerCombo = new javax.swing.JComboBox();
        widgetScroll = new javax.swing.JScrollPane();
        triangleWidget = new org.jreserve.gui.trianglewidget.TriangleWidget();

        setLayout(new java.awt.BorderLayout());

        toolBarPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(toolBarFiller, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cumulatedLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.cumulatedLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(cumulatedLabel, gridBagConstraints);

        cummulatedCheck.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cummulatedCheck, null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(cummulatedCheck, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(scaleLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.scaleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(scaleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(scaleSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(layoutLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.layoutLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(layoutLabel, gridBagConstraints);

        layoutCombo.setModel(new DefaultComboBoxModel(TriangleModelRegistry.getAdapters().toArray()));
        layoutCombo.setRenderer(WidgetUtils.displayableListRenderer());
        layoutCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                layoutComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(layoutCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(layerLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.layerLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(layerLabel, gridBagConstraints);

        layerCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        layerCombo.setRenderer(WidgetUtils.displayableListRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(layerCombo, gridBagConstraints);

        add(toolBarPanel, java.awt.BorderLayout.NORTH);

        widgetScroll.setViewportView(triangleWidget);

        add(widgetScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void layoutComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layoutComboActionPerformed
        TriangleModelAdapter adapter = (TriangleModelAdapter) layoutCombo.getSelectedItem();
        if(adapter != null)
            triangleWidget.setModel(adapter.getTriangleModel());
    }//GEN-LAST:event_layoutComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cummulatedCheck;
    private javax.swing.JLabel cumulatedLabel;
    private javax.swing.JComboBox layerCombo;
    private javax.swing.JLabel layerLabel;
    private javax.swing.JComboBox layoutCombo;
    private javax.swing.JLabel layoutLabel;
    private javax.swing.JLabel scaleLabel;
    private org.jreserve.gui.localesettings.ScaleSpinner scaleSpinner;
    private javax.swing.Box.Filler toolBarFiller;
    private javax.swing.JPanel toolBarPanel;
    private org.jreserve.gui.trianglewidget.TriangleWidget triangleWidget;
    private javax.swing.JScrollPane widgetScroll;
    // End of variables declaration//GEN-END:variables
}
