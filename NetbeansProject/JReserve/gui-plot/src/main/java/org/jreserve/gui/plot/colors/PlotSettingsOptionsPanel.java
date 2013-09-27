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
package org.jreserve.gui.plot.colors;

import java.awt.Color;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.plot.settings.PlotSettings;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PlotSettingsOptionsPanel extends javax.swing.JPanel {

    private final static String COLOR_GENERATOR_ID = "org.jreserve.gui.plot.colors.DefaultColorGeneratorFactory";
    private final PlotSettingsOptionsPanelController controller;
    
    public PlotSettingsOptionsPanel(PlotSettingsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    void load() {
        bgColorLabel.setBackground(PlotSettings.getBackground());
        fgColorLabel.setBackground(PlotSettings.getForeground());
        gridColorLabel.setBackground(PlotSettings.getGridColor());
    
        List<AbstractColorGeneratorAdapter> cgs = ColorGeneratorRegistry.getAdapters();
        colorGeneratorCombo.setModel(new DefaultComboBoxModel(cgs.toArray()));
        String id = PlotSettings.getColorGeneratorId();
        if(id == null)
            id = COLOR_GENERATOR_ID;
        int index = -1;
        if(id != null) {
            int size = cgs.size();
            for(int i=0; i<size; i++)
                if(id.equals(cgs.get(i).getId()))
                    index = i;
        }
        colorGeneratorCombo.setSelectedIndex(index);
    }
    
    void store() {
        PlotSettings.setBackground(bgColorLabel.getBackground());
        PlotSettings.setBackground(fgColorLabel.getBackground());
        PlotSettings.setBackground(gridColorLabel.getBackground());
        
        AbstractColorGeneratorAdapter cg = (AbstractColorGeneratorAdapter) colorGeneratorCombo.getSelectedItem();
        PlotSettings.setColorGeneratorId(cg==null? null : cg.getId());
    }
    
    boolean valid() {
        return true;
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

        bgLabel = new javax.swing.JLabel();
        fgLabel = new javax.swing.JLabel();
        gridLabel = new javax.swing.JLabel();
        paletteLabel = new javax.swing.JLabel();
        bgColorLabel = new javax.swing.JLabel();
        fgColorLabel = new javax.swing.JLabel();
        gridColorLabel = new javax.swing.JLabel();
        colorGeneratorCombo = new javax.swing.JComboBox();
        browseBgButton = new javax.swing.JButton();
        browseFgButton = new javax.swing.JButton();
        browseGridButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(bgLabel, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.bgLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(bgLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(fgLabel, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.fgLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(fgLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(gridLabel, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.gridLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(gridLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(paletteLabel, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.paletteLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(paletteLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(bgColorLabel, " ");
        bgColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bgColorLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(bgColorLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(fgColorLabel, " ");
        fgColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fgColorLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(fgColorLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(gridColorLabel, " ");
        gridColorLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridColorLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(gridColorLabel, gridBagConstraints);

        colorGeneratorCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        colorGeneratorCombo.setMinimumSize(new java.awt.Dimension(100, 20));
        colorGeneratorCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        colorGeneratorCombo.setRenderer(WidgetUtils.displayableListRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(colorGeneratorCombo, gridBagConstraints);

        browseBgButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseBgButton, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.browseBgButton.text")); // NOI18N
        browseBgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBgButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(browseBgButton, gridBagConstraints);

        browseFgButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseFgButton, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.browseBgButton.text")); // NOI18N
        browseFgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseFgButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(browseFgButton, gridBagConstraints);

        browseGridButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseGridButton, org.openide.util.NbBundle.getMessage(PlotSettingsOptionsPanel.class, "PlotSettingsOptionsPanel.browseBgButton.text")); // NOI18N
        browseGridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseGridButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(browseGridButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void browseGridButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseGridButtonActionPerformed
        browseColor(gridColorLabel);
    }//GEN-LAST:event_browseGridButtonActionPerformed

    private void browseFgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFgButtonActionPerformed
        browseColor(fgColorLabel);
    }//GEN-LAST:event_browseFgButtonActionPerformed

    private void browseBgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBgButtonActionPerformed
        browseColor(bgColorLabel);
    }//GEN-LAST:event_browseBgButtonActionPerformed

    private void browseColor(JLabel label) {
        Color color = label.getBackground();
        color = ColorChooserDialog.selectColor(color);
        if(color != null) {
            label.setBackground(color);
            controller.changed();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgColorLabel;
    private javax.swing.JLabel bgLabel;
    private javax.swing.JButton browseBgButton;
    private javax.swing.JButton browseFgButton;
    private javax.swing.JButton browseGridButton;
    private javax.swing.JComboBox colorGeneratorCombo;
    private javax.swing.JLabel fgColorLabel;
    private javax.swing.JLabel fgLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel gridColorLabel;
    private javax.swing.JLabel gridLabel;
    private javax.swing.JLabel paletteLabel;
    // End of variables declaration//GEN-END:variables
}
