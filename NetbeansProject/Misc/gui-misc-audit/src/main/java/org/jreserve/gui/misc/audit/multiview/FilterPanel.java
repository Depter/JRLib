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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.FilterPanel.Select.User=Select user...",
    "LBL.FilterPanel.Select.Machine=Select machine..."
})
class FilterPanel extends javax.swing.JPanel {
    
    private Date minDate;
    private Date maxDate;
    private Map<String, Set<String>> machines = new TreeMap<String, Set<String>>();
    private InputListener inputListener = new InputListener();
    private boolean fireChange = true;
    private ChangeSupport cs = new ChangeSupport(this);
    private Filter filter = new Filter();
    private Set<String> allUsers = new TreeSet<String>();
    
    FilterPanel() {
        initComponents();
    }
    
    private JSpinner.DateEditor createEditor(JSpinner spinner) {
        String format = System.getProperty("default.date.format");
        if(format == null || format.length() == 0)
            format = new SimpleDateFormat().toPattern();
        return new JSpinner.DateEditor(spinner, format);
    }

    void clearFilter() {
        fireChange = false;
        dateFromSpinner.setValue(minDate);
        dateToSpinner.setValue(maxDate);
        machineCombo.setSelectedItem(null);
        fireChange = true;
        fireChange();
    }
    
    List<AuditRecord> filterRecords(List<AuditRecord> records) {
        List<AuditRecord> result = new ArrayList<AuditRecord>(records.size());
        filter.init();
        for(AuditRecord record : records)
            if(filter.accepts(record))
                result.add(record);
        return result;
    }

    void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    void removeChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    
    void initFromRecords(List<AuditRecord> records) {
        clearBounds();
        for(AuditRecord record : records) {
            checkDateBounds(record.getChangeDate());
            addUser(record.getUser(), record.getMachine());
        }
        initBounds();
    }
    
    private void clearBounds() {
        minDate = null;
        maxDate = null;
        machines.clear();
        allUsers.clear();
    }
    
    private void checkDateBounds(Date date) {
        if(minDate == null || minDate.after(date))
            minDate = date;
        if(maxDate == null || maxDate.before(date))
            maxDate = date;
    }
    
    private void addUser(String user, String machine) {
        Set<String> users = machines.get(machine);
        if(users == null) {
            users = new TreeSet<String>();
            machines.put(machine, users);
        }
        users.add(user);
        allUsers.add(user);
    }
    
    private void initBounds() {
        fireChange = false;
        if(minDate != null) {
            escapeDates();
            setMinMax(dateFromSpinner);
            setMinMax(dateToSpinner);
            dateFromSpinner.setValue(minDate);
            dateToSpinner.setValue(maxDate);
        }
        machineCombo.setModel(new DefaultComboBoxModel(toArrayWithNull(machines.keySet())));
        machineCombo.setSelectedItem(null);
        fireChange = true;
        fireChange();
    }
    
    private void escapeDates() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(minDate);
        minDate = setToMidnight(calendar);
        
        calendar.setTime(maxDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        maxDate = setToMidnight(calendar);
    }
    
    private Date setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    private void setMinMax(JSpinner spinner) {
        SpinnerDateModel model = (SpinnerDateModel) spinner.getModel();
        model.setStart(minDate);
        model.setEnd(maxDate);
    }
    
    private Object[] toArrayWithNull(Set<String> values) {
        int size = values.size();
        Object[] result = new Object[size+1];
        int i=1;
        for(String str : values)
            result[i++] = str;
        return result;
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

        dataLabel = new javax.swing.JLabel();
        dateFromSpinner = new javax.swing.JSpinner();
        dateSeparator = new javax.swing.JLabel();
        dateToSpinner = new javax.swing.JSpinner();
        userLabel = new javax.swing.JLabel();
        userCombo = new javax.swing.JComboBox();
        userSeparator = new javax.swing.JLabel();
        machineCombo = new javax.swing.JComboBox();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(dataLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.dataLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dataLabel, gridBagConstraints);

        dateFromSpinner.setModel(new javax.swing.SpinnerDateModel());
        dateFromSpinner.setEditor(createEditor(dateFromSpinner));
        dateFromSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dateFromSpinner, gridBagConstraints);

        dateSeparator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(dateSeparator, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.dateSeparator.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dateSeparator, gridBagConstraints);

        dateToSpinner.setModel(new javax.swing.SpinnerDateModel());
        dateToSpinner.setEditor(createEditor(dateToSpinner));
        dateToSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(dateToSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(userLabel, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.userLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(userLabel, gridBagConstraints);

        userCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        userCombo.addActionListener(inputListener);
        userCombo.setRenderer(new NullComboRenderer(Bundle.LBL_FilterPanel_Select_User()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(userCombo, gridBagConstraints);

        userSeparator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(userSeparator, org.openide.util.NbBundle.getMessage(FilterPanel.class, "FilterPanel.userSeparator.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(userSeparator, gridBagConstraints);

        machineCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        machineCombo.addActionListener(inputListener);
        machineCombo.setRenderer(new NullComboRenderer(Bundle.LBL_FilterPanel_Select_Machine()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(machineCombo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dataLabel;
    private javax.swing.JSpinner dateFromSpinner;
    private javax.swing.JLabel dateSeparator;
    private javax.swing.JSpinner dateToSpinner;
    private javax.swing.Box.Filler filler;
    private javax.swing.JComboBox machineCombo;
    private javax.swing.JComboBox userCombo;
    private javax.swing.JLabel userLabel;
    private javax.swing.JLabel userSeparator;
    // End of variables declaration//GEN-END:variables

    private class InputListener implements ActionListener, ChangeListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(source == machineCombo) {
                setUserNames();
            }
            fireChange();
        }
        
        private void setUserNames() {
            fireChange = false;
            String machine = (String) machineCombo.getSelectedItem();
            Set<String> users = machine==null? allUsers : machines.get(machine);
            userCombo.setModel(new DefaultComboBoxModel(toArrayWithNull(users)));
            userCombo.setSelectedItem(null);
            fireChange = true;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
    
    private static class NullComboRenderer extends DefaultListCellRenderer {

        private final String nullValue;
        
        private NullComboRenderer(String nullValue) {
            this.nullValue = nullValue;
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if(value == null)
                value = nullValue;
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
        }
    
    }
    
    private class Filter {
        
        private final Calendar calendar = GregorianCalendar.getInstance();
        
        private Date from;
        private Date to;
        private String machine;
        private String user;
        
        void init() {
            initFromDate();
            initToDate();
            machine = (String) machineCombo.getSelectedItem();
            user = (String) userCombo.getSelectedItem();
        }
        
        private void initFromDate() {
            from = (Date) dateFromSpinner.getValue();
            if(from != null) {
                calendar.setTime(from);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                from = calendar.getTime();
            }
        }
        
        private void initToDate() {
            to = (Date) dateToSpinner.getValue();
            if(to != null) {
                calendar.setTime(to);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.DATE, 1);
                to = calendar.getTime();
            }
        }
        
        boolean accepts(AuditRecord record) {
            return accepts(record.getChangeDate()) &&
                   accepts(machine, record.getMachine()) &&
                   accepts(user, record.getUser());
        }
        
        private boolean accepts(Date date) {
            if(from != null && from.after(date))
                return false;
            if(to == null)
                return true;
            return date.before(to);
        }
        
        private boolean accepts(String criteria, String value) {
            if(criteria != null && !criteria.equalsIgnoreCase(value))
                return false;
            return true;
        }
    }
}
