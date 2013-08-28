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

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Peter Deics
 * @version 1.0
 */
public class FilterPanel extends javax.swing.JPanel {

    private MonthDate aMin;
    private MonthDate aMax;
    private MonthDate dMin;
    private MonthDate dMax;
    private final ChangeSupport cs = new ChangeSupport(this);
    private final InputListener inputListener = new InputListener();
    private boolean fireChange = true;
    
    public FilterPanel() {
        initComponents();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        accidentFromSpinner.setEnabled(enabled);
        accidentToSpinner.setEnabled(enabled);
        developmentFromSpinner.setEnabled(enabled);
        developmentToSpinner.setEnabled(enabled);
    }
    
    void clearFilter() {
        fireChange = false;
        if(aMin != null) {
            accidentFromSpinner.setMonthDate(aMin);
            accidentToSpinner.setMonthDate(aMax);
            developmentFromSpinner.setMonthDate(dMin);
            developmentToSpinner.setMonthDate(dMax);
        }
        fireChange = true;
        fireChange();
    }
    
    void setBounds(List<DataEntry> records) {
        fireChange = false;
        aMin = null;
        aMax = null;
        dMax = null;
        dMin = null;
        
        for(DataEntry entry : records) {
            MonthDate date = entry.getAccidentDate();
            if(aMin == null || aMin.after(date))
                aMin = date;
            if(aMax == null || aMax.before(date))
                aMax = date;
            date = entry.getDevelopmentDate();
            if(dMin == null || dMin.after(date))
                dMin = date;
            if(dMax == null || dMax.before(date))
                dMax = date;
        }
        
        if(aMin != null) {
            setBounds(aMin, aMax, accidentFromSpinner, accidentToSpinner);
            setBounds(dMin, dMax, developmentFromSpinner, developmentToSpinner);
        }
        
        fireChange = true;
        fireChange();
    }
    
    private void setBounds(MonthDate min, MonthDate max, MonthDateSpinner from, MonthDateSpinner to) {
        from.setStartMonthDate(min);
        from.setEndMonthDate(max);
        from.setMonthDate(min);
        to.setStartMonthDate(min);
        to.setEndMonthDate(max);
        to.setMonthDate(max);
    }
    
    DataEntryFilter createFilter() {
        return new Filter();
    }
    
    void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    
    void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
    
    private void fireChange() {
        if(fireChange)
            cs.fireChange();
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

        accidentLabel = new javax.swing.JLabel();
        accidentSepLabel = new javax.swing.JLabel();
        accidentFromSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        accidentToSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        developmentLabel = new javax.swing.JLabel();
        developmentSepLabel = new javax.swing.JLabel();
        developmentFromSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        developmentToSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(accidentLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentFromLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(accidentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentSepLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.accidentSepLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(accidentSepLabel, gridBagConstraints);

        accidentFromSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(accidentFromSpinner, gridBagConstraints);

        accidentToSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(accidentToSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentToLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(developmentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentSepLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.developmentSepLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(developmentSepLabel, gridBagConstraints);

        developmentFromSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(developmentFromSpinner, gridBagConstraints);

        developmentToSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(developmentToSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentFromSpinner;
    private javax.swing.JLabel accidentLabel;
    private javax.swing.JLabel accidentSepLabel;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner accidentToSpinner;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentFromSpinner;
    private javax.swing.JLabel developmentLabel;
    private javax.swing.JLabel developmentSepLabel;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner developmentToSpinner;
    private javax.swing.Box.Filler filler;
    // End of variables declaration//GEN-END:variables

    private class InputListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
    
    private class Filter implements DataEntryFilter {
        
        private final MonthDate aMin = accidentFromSpinner.getMonthDate();
        private final MonthDate aMax = accidentToSpinner.getMonthDate();
        private final MonthDate dMin = developmentFromSpinner.getMonthDate();
        private final MonthDate dMax = developmentToSpinner.getMonthDate();
        
        @Override
        public boolean acceptsEntry(DataEntry entry) {
            MonthDate date = entry.getAccidentDate();
            if(this.aMin!=null && this.aMin.after(date))
                return false;
            if(this.aMax!=null && this.aMax.before(date))
                return false;
            date = entry.getDevelopmentDate();
            if(this.dMin!=null && this.dMin.after(date))
                return false;
            if(this.dMax!=null && this.dMax.before(date))
                return false;
            return true;
        }
    }
}