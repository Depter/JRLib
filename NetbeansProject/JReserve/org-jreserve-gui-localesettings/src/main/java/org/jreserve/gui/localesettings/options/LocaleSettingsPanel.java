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
package org.jreserve.gui.localesettings.options;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

@Messages({
    "MSG.LocaleSettingsPanel.DecimalSeparator.Empty=Decimal separator is not set!",
    "MSG.LocaleSettingsPanel.ThousandSeparator.Empty=Thousand separator is not set!",
    "MSG.LocaleSettingsPanel.DateFormat.Empty=Date format is not set!",
    "# {0} - pattern",
    "MSG.LocaleSettingsPanel.DateFormat.Invalid=Date format ''{0}'' is invalid!"
})
final class LocaleSettingsPanel extends javax.swing.JPanel {
    
    private final static String ERR_IMG = "org/netbeans/modules/dialogs/error.gif"; //NOI18
    private final LocaleSettingsOptionsPanelController controller;
    private boolean valid = false;
    private final SpinnerListener spinnerListener = new SpinnerListener();
    private final ComboListener comboListener = new ComboListener();
    private final TextListener textListener = new TextListener();
    private JTextComponent dateEditor;
    
    LocaleSettingsPanel(LocaleSettingsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        decimalSepLabel = new javax.swing.JLabel();
        thousandSepLabel = new javax.swing.JLabel();
        decimalsLabel = new javax.swing.JLabel();
        decimalsSpinner = new javax.swing.JSpinner();
        decimalExampleText = new javax.swing.JLabel();
        dateFormatLabel = new javax.swing.JLabel();
        decimalSepText = new javax.swing.JTextField();
        thousandSepText = new javax.swing.JTextField();
        dateFormatCombo = new javax.swing.JComboBox();
        dateExampleText = new javax.swing.JLabel();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        bottomPanel = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        defaultButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(decimalSepLabel, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.decimalSepLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalSepLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(thousandSepLabel, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.thousandSepLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(thousandSepLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(decimalsLabel, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.decimalsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalsLabel, gridBagConstraints);

        decimalsSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        ((JSpinner.DefaultEditor)decimalsSpinner.getEditor()).getTextField().setEditable(false);
        decimalsSpinner.addChangeListener(spinnerListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalsSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(decimalExampleText, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.decimalExampleText.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        add(decimalExampleText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dateFormatLabel, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.dateFormatLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(dateFormatLabel, gridBagConstraints);

        WidgetUtils.oneCharacterInput(decimalSepText);
        decimalSepText.setText(null);
        decimalSepText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(decimalSepText, gridBagConstraints);

        WidgetUtils.oneCharacterInput(thousandSepText);
        thousandSepText.setText(null);
        thousandSepText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(thousandSepText, gridBagConstraints);

        dateFormatCombo.setEditable(true);
        dateEditor = (JTextComponent) dateFormatCombo.getEditor().getEditorComponent();
        ((AbstractDocument)dateEditor.getDocument()).addDocumentListener(textListener);
        dateFormatCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dateFormatCombo.addActionListener(comboListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dateFormatCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dateExampleText, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.dateExampleText.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        add(dateExampleText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);

        bottomPanel.setLayout(new java.awt.BorderLayout());

        errorLabel.setIcon(ImageUtilities.loadImageIcon(ERR_IMG, false));
        org.openide.awt.Mnemonics.setLocalizedText(errorLabel, null);
        errorLabel.setVisible(false);
        bottomPanel.add(errorLabel, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(defaultButton, org.openide.util.NbBundle.getMessage(LocaleSettingsPanel.class, "LocaleSettingsPanel.defaultButton.text")); // NOI18N
        defaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(defaultButton, java.awt.BorderLayout.LINE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(bottomPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultButtonActionPerformed
        LocaleSettings.clear();
        load();
    }//GEN-LAST:event_defaultButtonActionPerformed

    void load() {
        decimalSepText.setText(""+LocaleSettings.getDecimalSeparator());
        thousandSepText.setText(""+LocaleSettings.getThousandSeparator());
        decimalsSpinner.setValue(LocaleSettings.getDecimalCount());
        dateFormatCombo.setModel(new DefaultComboBoxModel(LocaleSettings.getDateFormats()));
        dateFormatCombo.setSelectedItem(LocaleSettings.getDateFormat());
    }

    void store() {
        LocaleSettings.setDecimalCount((Integer) decimalsSpinner.getValue());
        LocaleSettings.setDecimalSeparator(decimalSepText.getText().charAt(0));
        LocaleSettings.setThousandSeparator(thousandSepText.getText().charAt(0));
        LocaleSettings.setDateFormat(new SimpleDateFormat(dateEditor.getText()));
    }

    boolean valid() {
        return valid;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel dateExampleText;
    private javax.swing.JComboBox dateFormatCombo;
    private javax.swing.JLabel dateFormatLabel;
    private javax.swing.JLabel decimalExampleText;
    private javax.swing.JLabel decimalSepLabel;
    private javax.swing.JTextField decimalSepText;
    private javax.swing.JLabel decimalsLabel;
    private javax.swing.JSpinner decimalsSpinner;
    private javax.swing.JButton defaultButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.Box.Filler filler;
    private javax.swing.JLabel thousandSepLabel;
    private javax.swing.JTextField thousandSepText;
    // End of variables declaration//GEN-END:variables

    private void validateInput() {
        valid = isInputValid();
        if(valid)
            showError(null);
        controller.changed();
    }
    
    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(msg != null);
    }
    
    private boolean isInputValid() {
        if(isDecimalSeparatorValid() && isThousandSeparatorValid()) {
            setDecimalExample();
        } else {
            decimalExampleText.setText(null);
            return false;
        }
        
        return isDateFormatValid();
    }
    
    private void setDecimalExample() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(decimalSepText.getText().charAt(0));
        dfs.setGroupingSeparator(thousandSepText.getText().charAt(0));
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(dfs);
        int digits = (Integer) decimalsSpinner.getValue();
        df.setMaximumFractionDigits(digits);
        df.setMinimumFractionDigits(digits);
        
        double v = 1234.56789;
        decimalExampleText.setText(df.format(v) + "; " + df.format(-v));
    }
    
    private boolean isDecimalSeparatorValid() {
        String str = decimalSepText.getText();
        if(str == null || str.length() == 0) {
            showError(Bundle.MSG_LocaleSettingsPanel_DecimalSeparator_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isThousandSeparatorValid() {
        String str = thousandSepText.getText();
        if(str == null || str.length() == 0) {
            showError(Bundle.MSG_LocaleSettingsPanel_ThousandSeparator_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isDateFormatValid() {
        String str = dateEditor.getText();
        //String str = (String) dateFormatCombo.getSelectedItem();
        if(str == null || str.length()==0) {
            showError(Bundle.MSG_LocaleSettingsPanel_DateFormat_Empty());
            return false;
        }
        
        try {
            DateFormat df = new SimpleDateFormat(str);
            dateExampleText.setText(df.format(new Date()));
        } catch (Exception ex) {
            showError(Bundle.MSG_LocaleSettingsPanel_DateFormat_Invalid(str));
            return false;
        }
        return true;
    }
    
    private class SpinnerListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            validateInput();
        }
    }
    
    private class ComboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            validateInput();
        }
    }
    
    private class TextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }
        
        private void update(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
    
}