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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
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
    "LBL.FilterPanel.Title=Select Filter",
    "LBL.FilterPanel.Ok=Ok",
    "LBL.FilterPanel.Cancel=Cancel"
})
class FilterPanel extends javax.swing.JPanel {
    
    private final static boolean MODAL = true;
    
    static DataEntryFilter createFilter(FilterPanel panel) {
        JButton[] options = new JButton[]{panel.okButton, panel.cancelButton};
        DialogDescriptor dd = new DialogDescriptor(
                panel, Bundle.LBL_FilterPanel_Title(), MODAL, 
                options, panel.cancelButton, DialogDescriptor.DEFAULT_ALIGN, 
                HelpCtx.DEFAULT_HELP, null);
        if(DialogDisplayer.getDefault().notify(dd) == panel.okButton)
            return panel.createFilter();
        return null;
    }
    
    private JButton okButton = new JButton(Bundle.LBL_FilterPanel_Ok(), CommonIcons.ok());
    private JButton cancelButton = new JButton(Bundle.LBL_FilterPanel_Cancel(), CommonIcons.cancel());
    private CheckListener checkListener = new CheckListener();
    private MonthDate accidentMin;
    private MonthDate accidentMax;
    private MonthDate developmentMin;
    private MonthDate developmentMax;
    
    public FilterPanel() {
        initComponents();
    }
    
    void setBounds(List<DataEntry> entries) {
        accidentMax = accidentMin = developmentMax = developmentMin = null;
        
        for(DataEntry entry : entries) {
            MonthDate accident = entry.getAccidentDate();
            MonthDate development = entry.getDevelopmentDate();
            if(accidentMin==null || accident.before(accidentMin))
                accidentMin = accident;
            if(accidentMax==null || accident.after(accidentMax))
                accidentMax = accident;
            if(developmentMin==null || development.before(developmentMin))
                developmentMin = development;
            if(developmentMax==null || development.after(developmentMax))
                developmentMax = development;
        }
        
        setBounds(accidentMin, accidentMax, accidentFromSpinner, accidentToSpinner);
        setBounds(developmentMin, developmentMax, developmentFromSpinner, developmentToSpinner);
    }

    private void setBounds(MonthDate min, MonthDate max, MonthDateSpinner from, MonthDateSpinner to) {
        if(min != null) {
            from.setStartMonthDate(min);
            from.setMonthDate(min);
            to.setStartMonthDate(min);
        }
        if(max != null) {
            from.setEndMonthDate(max);
            to.setEndMonthDate(max);
            to.setMonthDate(max);
        }
    }
    
    private DataEntryFilter createFilter() {
        MonthDate aStart = getMonthDate(accidentFromCheck, accidentFromSpinner);
        MonthDate aEnd = getMonthDate(accidentToCheck, accidentToSpinner);
        MonthDate dStart = getMonthDate(developmentFromCheck, developmentFromSpinner);
        MonthDate dEnd = getMonthDate(developmentToCheck, developmentToSpinner);
        return new Filter(aStart, aEnd, dStart, dEnd);
    }
    
    private MonthDate getMonthDate(JCheckBox check, MonthDateSpinner spinner) {
        return check.isSelected()? spinner.getMonthDate() : null;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

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
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(accidentLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.accidentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(accidentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentFromCheck, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.accidentFromCheck.text")); // NOI18N
        accidentFromCheck.addActionListener(checkListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 5);
        add(accidentFromCheck, gridBagConstraints);

        accidentFromSpinner.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(accidentFromSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentToCheck, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.accidentToCheck.text")); // NOI18N
        accidentToCheck.addActionListener(checkListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 5);
        add(accidentToCheck, gridBagConstraints);

        accidentToSpinner.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(accidentToSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(developmentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentFromCheck, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentFromCheck.text")); // NOI18N
        developmentFromCheck.addActionListener(checkListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 5);
        add(developmentFromCheck, gridBagConstraints);

        developmentFromSpinner.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(developmentFromSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentToCheck, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentToCheck.text")); // NOI18N
        developmentToCheck.addActionListener(checkListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 5);
        add(developmentToCheck, gridBagConstraints);

        developmentToSpinner.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(developmentToSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox accidentFromCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentFromSpinner;
    private javax.swing.JLabel accidentLabel;
    private javax.swing.JCheckBox accidentToCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentToSpinner;
    private javax.swing.JCheckBox developmentFromCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentFromSpinner;
    private javax.swing.JLabel developmentLabel;
    private javax.swing.JCheckBox developmentToCheck;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentToSpinner;
    private javax.swing.Box.Filler filler;
    // End of variables declaration//GEN-END:variables

    private class CheckListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(accidentFromCheck == source) {
                enableDisable(accidentFromCheck, accidentFromSpinner);
            } else if(accidentToCheck == source) {
                enableDisable(accidentToCheck, accidentToSpinner);
            } else if(developmentFromCheck == source) {
                enableDisable(developmentFromCheck, developmentFromSpinner);
            } else if(developmentToCheck == source) {
                enableDisable(developmentToCheck, developmentToSpinner);
            }
        }
        
        private void enableDisable(JCheckBox check, JSpinner spinner) {
            spinner.setEnabled(check.isSelected());
        }
    }
    
    private static class Filter implements DataEntryFilter {
        private final MonthDate accidentStart;
        private final MonthDate accidentTo;
        private final MonthDate developmentStart;
        private final MonthDate developmentTo;

        private Filter(MonthDate accidentStart, MonthDate accidentTo, MonthDate developmentStart, MonthDate developmentTo) {
            this.accidentStart = accidentStart;
            this.accidentTo = accidentTo;
            this.developmentStart = developmentStart;
            this.developmentTo = developmentTo;
        }
        
        @Override
        public boolean acceptsEntry(DataEntry entry) {
            return accepts(accidentStart, accidentTo, entry.getAccidentDate()) &&
                   accepts(developmentStart, developmentTo, entry.getDevelopmentDate());
        }
        
        private boolean accepts(MonthDate min, MonthDate max, MonthDate date) {
            if(min != null && min.after(date))
                return false;
            if(max != null && max.before(date))
                return false;
            return true;
        }
    
    }
}
