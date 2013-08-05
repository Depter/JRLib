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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataItemChooser;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.netbeans.api.project.ProjectUtils;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportDataWizardVisualPanel1.Title=Storage & Source"
})
class ImportDataWizardVisualPanel1 extends javax.swing.JPanel {
    
    private DataManager dm;
    
    ImportDataWizardVisualPanel1() {
        initComponents();
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_ImportDataWizardVisualPanel1_Title();
    }
    
    void setDataItem(DataItem item) {
        this.dm = item.getDataManager();
        storageButton.setEnabled(true);
        projectText.setText(ProjectUtils.getInformation(dm.getProject()).getDisplayName());
        if(item instanceof DataSource)
            storageText.setText(item.getPath());
    }
    
    void setImportProvider(ImportDataProviderAdapter importProvider) {
        providerCombo.setSelectedItem(importProvider);
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

        projectLabel = new javax.swing.JLabel();
        projectText = new javax.swing.JLabel();
        storageLabel = new javax.swing.JLabel();
        storageText = new javax.swing.JTextField();
        storageButton = new javax.swing.JButton();
        providerLabel = new javax.swing.JLabel();
        providerCombo = new javax.swing.JComboBox(ImportDataProviderRegistry.getAdapters().toArray());
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(projectLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanel1.class, "ImportDataWizardVisualPanel1.projectLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(projectLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(projectText, null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(projectText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(storageLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanel1.class, "ImportDataWizardVisualPanel1.storageLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(storageLabel, gridBagConstraints);

        storageText.setText(null);
        storageText.getDocument().addDocumentListener(new StorageListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(storageText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(storageButton, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanel1.class, "ImportDataWizardVisualPanel1.storageButton.text")); // NOI18N
        storageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(storageButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(providerLabel, org.openide.util.NbBundle.getMessage(ImportDataWizardVisualPanel1.class, "ImportDataWizardVisualPanel1.providerLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(providerLabel, gridBagConstraints);

        providerCombo.setRenderer(WidgetUtils.displayableListRenderer());
        providerCombo.addActionListener(new ProviderListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(providerCombo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void storageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storageButtonActionPerformed
        DataSource ds = DataItemChooser.chooseSource(dm);
        if(ds != null)
            storageText.setText(ds.getPath());
    }//GEN-LAST:event_storageButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JLabel projectText;
    private javax.swing.JComboBox providerCombo;
    private javax.swing.JLabel providerLabel;
    private javax.swing.JButton storageButton;
    private javax.swing.JLabel storageLabel;
    private javax.swing.JTextField storageText;
    // End of variables declaration//GEN-END:variables

    private class StorageListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateStorage();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateStorage();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        private void updateStorage() {
            DataSource ds = getSelectedDataSource();
            putClientProperty(ImportDataWizardPanel1.PROP_DATA_SOURCE_PATH, storageText.getText());
            putClientProperty(ImportDataProvider.PROP_DATA_SOURCE, ds);
        }
        
        private DataSource getSelectedDataSource() {
            String path = storageText.getText();
            if(path==null || path.length() == 0 || dm == null)
                return null;
            return dm.getDataSource(path);
        }
    }
    
    private class ProviderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImportDataProviderAdapter adapter = (ImportDataProviderAdapter) providerCombo.getSelectedItem();
            if(adapter == null) {
                putClientProperty(ImportDataWizardPanel1.PROP_IMPORT_DATA_PROVIDER_ADAPTER, null);
                putClientProperty(ImportDataProvider.PROP_IMPORT_WIZARD, null);
            } else {
                putClientProperty(ImportDataWizardPanel1.PROP_IMPORT_DATA_PROVIDER_ADAPTER, adapter);
                putClientProperty(ImportDataProvider.PROP_IMPORT_WIZARD, adapter.getImportDataProvider());
            }
        }
    }
}