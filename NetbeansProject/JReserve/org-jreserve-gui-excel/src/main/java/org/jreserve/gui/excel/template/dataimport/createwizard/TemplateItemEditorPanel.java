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
package org.jreserve.gui.excel.template.dataimport.createwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateModel.SourceType;
import org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateModel.TemplateRow;
import org.jreserve.gui.misc.utils.widgets.EnumComboBox;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TemplateItemEditorPanel.Title=Edit Template Item",
    "CTL.TemplateItemEditorPanel.Ok=Ok",
    "CTL.TemplateItemEditorPanel.Cancel=Cancel"
})
class TemplateItemEditorPanel extends javax.swing.JPanel {
    private final static int OPTION_ALIGN = DialogDescriptor.DEFAULT_ALIGN;
    private final static boolean MODAL = true;
    
    private static boolean lastCummulated = false;
    private static MonthDate lastDate = new MonthDate();
    private static int lastAccidentLength = 1;
    private static int lastDevelopmentLength = 1;
    
    static boolean editTemplateRow(TemplateRow row, List<String> names) {
        TemplateItemEditorPanel panel = new TemplateItemEditorPanel(row, names);
        DialogDescriptor dd = createDescriptor(panel);
        
        if(DialogDisplayer.getDefault().notify(dd) == panel.okButton) {
            panel.updateRow();
            panel.updateTempValues();
            return true;
        } else {
            return false;
        }
    }
    
    private static DialogDescriptor createDescriptor(TemplateItemEditorPanel panel) {
        Object[] options = new Object[]{panel.okButton, panel.cancelButton};
        DialogDescriptor dd = new DialogDescriptor(
                panel, Bundle.LBL_TemplateItemEditorPanel_Title(), 
                MODAL, options, options[0], OPTION_ALIGN, 
                HelpCtx.DEFAULT_HELP, null);
        dd.setClosingOptions(options);
        return dd;
    }
    
    private final TemplateRow row;
    private final List<String> names;
    private final JButton okButton = new JButton(Bundle.CTL_TemplateItemEditorPanel_Ok());
    private final JButton cancelButton = new JButton(Bundle.CTL_TemplateItemEditorPanel_Cancel());
    private final JTextField referenceText;
    
    private TemplateItemEditorPanel(TemplateRow row, List<String> names) {
        this.row = row;
        this.names = names==null? Collections.EMPTY_LIST : names;
        initComponents();
        referenceText = (JTextField) referenceCombo.getEditor().getEditorComponent();
    }
    
    private void updateRow() {
        row.setReference(referenceText.getText());
        
        DataType dt = (DataType) dataTypeCombo.getSelectedItem();
        row.setDataType(dt);
        row.setCummulated(DataType.TRIANGLE == dt? cummulatedCheck.isSelected() : false);
        
        SourceType st = (SourceType) sourceTypeCombo.getSelectedItem();
        row.setSourceType(st);
        if(SourceType.TRIANGLE == st) {
            row.setMonthDate(startDateSpinner.getMonthDate());
            row.setAccidentLength(accidentLengthSpinner.getMonthCount());
            row.setDevelopmentLength(developmentLengthSpinner.getMonthCount());
        } else {
            row.setMonthDate(null);
            row.setAccidentLength(null);
            row.setDevelopmentLength(null);
        }
    }
    
    private void updateTempValues() {
        lastDate = startDateSpinner.getMonthDate();
        lastAccidentLength = accidentLengthSpinner.getMonthCount();
        lastDevelopmentLength = developmentLengthSpinner.getMonthCount();
        lastCummulated = cummulatedCheck.isSelected();
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

        referenceLabel = new javax.swing.JLabel();
        referenceCombo = new javax.swing.JComboBox();
        sourceTypeLabel = new javax.swing.JLabel();
        sourceTypeCombo = new EnumComboBox(SourceType.class);
        dataTypeLabel = new javax.swing.JLabel();
        dataTypeCombo = new EnumComboBox(DataType.class);
        cummulatedLabel = new javax.swing.JLabel();
        cummulatedCheck = new javax.swing.JCheckBox();
        startDateLabel = new javax.swing.JLabel();
        startDateSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        accidentLengthLabel = new javax.swing.JLabel();
        accidentLengthSpinner = new org.jreserve.gui.trianglewidget.geometry.PeriodStepSpinner();
        developmentLengthLabel = new javax.swing.JLabel();
        developmentLengthSpinner = new org.jreserve.gui.trianglewidget.geometry.PeriodStepSpinner();
        rightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        bottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 0, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(referenceLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.referenceLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(referenceLabel, gridBagConstraints);

        referenceCombo.setEditable(true);
        referenceCombo.setModel(new DefaultComboBoxModel(names.toArray()));
        referenceCombo.setSelectedItem(row.getReference());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(referenceCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(sourceTypeLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.sourceTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(sourceTypeLabel, gridBagConstraints);

        sourceTypeCombo.addActionListener(new SourceTypeListener());
        sourceTypeCombo.setSelectedItem (row.getSourceType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(sourceTypeCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dataTypeLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.dataTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dataTypeLabel, gridBagConstraints);

        dataTypeCombo.addActionListener(new DataTypeListener());
        dataTypeCombo.setSelectedItem (row.getDataType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(dataTypeCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cummulatedLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.cummulatedLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(cummulatedLabel, gridBagConstraints);

        Boolean ic = row.isCummulated();
        cummulatedCheck.setSelected(ic==null? lastCummulated : ic);
        org.openide.awt.Mnemonics.setLocalizedText(cummulatedCheck, null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(cummulatedCheck, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(startDateLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.startDateLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(startDateLabel, gridBagConstraints);

        MonthDate md = row.getMonthDate();
        startDateSpinner.setMonthDate(md==null? lastDate : md);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(startDateSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentLengthLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.accidentLengthLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(accidentLengthLabel, gridBagConstraints);

        Integer al = row.getAccidentLength();
        accidentLengthSpinner.setMonthCount(al==null? lastAccidentLength : al);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(accidentLengthSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentLengthLabel, org.openide.util.NbBundle.getMessage(TemplateItemEditorPanel.class, "TemplateItemEditorPanel.developmentLengthLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(developmentLengthLabel, gridBagConstraints);

        developmentLengthSpinner.setMonthCount(row.getDevelopmentLength());
        Integer dl = row.getDevelopmentLength();
        developmentLengthSpinner.setMonthCount(dl==null? lastDevelopmentLength : dl);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(developmentLengthSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(rightFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(bottomFiller, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accidentLengthLabel;
    private org.jreserve.gui.trianglewidget.geometry.PeriodStepSpinner accidentLengthSpinner;
    private javax.swing.Box.Filler bottomFiller;
    private javax.swing.JCheckBox cummulatedCheck;
    private javax.swing.JLabel cummulatedLabel;
    private javax.swing.JComboBox dataTypeCombo;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JLabel developmentLengthLabel;
    private org.jreserve.gui.trianglewidget.geometry.PeriodStepSpinner developmentLengthSpinner;
    private javax.swing.JComboBox referenceCombo;
    private javax.swing.JLabel referenceLabel;
    private javax.swing.Box.Filler rightFiller;
    private javax.swing.JComboBox sourceTypeCombo;
    private javax.swing.JLabel sourceTypeLabel;
    private javax.swing.JLabel startDateLabel;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner startDateSpinner;
    // End of variables declaration//GEN-END:variables

    private class DataTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean enabled = DataType.TRIANGLE == dataTypeCombo.getSelectedItem();
            cummulatedCheck.setEnabled(enabled);
        }
    }
    
    private class SourceTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean enabled = SourceType.TRIANGLE == sourceTypeCombo.getSelectedItem();
            startDateSpinner.setEnabled(enabled);
            accidentLengthSpinner.setEnabled(enabled);
            developmentLengthSpinner.setEnabled(enabled);
        }
    }
}
