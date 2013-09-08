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
package org.jreserve.gui.data.api.wizard;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.misc.utils.dataobject.DataObjectChooser;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSourceNameVisualPanel.Name=Name & Location"
})
public class DataSourceNameVisualPanel extends javax.swing.JPanel {

    private final DataSourceNameWizardPanel controller;
    private DataObjectProvider dop;
    private TextListener textListener = new TextListener();
    
    public DataSourceNameVisualPanel(DataSourceNameWizardPanel controller) {
        this.controller = controller;
        initComponents();
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_DataSourceNameVisualPanel_Name();
    }
    
    void setProject(Project project) {
        projectText.setText(ProjectUtils.getInformation(project).getDisplayName());
    }
    
    void setObjectProvider(DataObjectProvider dop) {
        if(dop != null) {
            this.dop = dop;
            dataTypeCombo.setEnabled(true);
            browseButton.setEnabled(true);
            nameText.setEnabled(true);
            folderText.setEnabled(true);
            folderText.setText(dop.getRootFolder().getName()+"/");
        }
    }
    
    void setDataFolder(DataFolder folder) {
        DataFolder root = dop==null? null : dop.getRootFolder();
        if(root != null && folder != null) {
            FileObject parent = root.getPrimaryFile().getParent();
            String rp = FileUtil.getRelativePath(parent, folder.getPrimaryFile());
            if(rp != null)
                folderText.setText(rp);
        }
    }
    
    DataObjectProvider getObjectProvider() {
        return dop;
    }
    
    String getDataSourcePath() {
        return pathText.getText();
    }
    
    DataType getDataType() {
        return (DataType) dataTypeCombo.getSelectedItem();
    }
    
    String getFileName() {
        return nameText.getText();
    }
    
    String getFolderName() {
        return folderText.getText();
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
        nameLabel = new javax.swing.JLabel();
        folderLabel = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        projectText = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        folderText = new javax.swing.JTextField();
        pathText = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        dataTypeLabel = new javax.swing.JLabel();
        dataTypeCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(projectLabel, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.projectLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(projectLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.nameLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(nameLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(folderLabel, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.folderLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(folderLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.pathLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(pathLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(projectText, null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(projectText, gridBagConstraints);

        nameText.setText(null);
        nameText.setEnabled(false);
        nameText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(nameText, gridBagConstraints);

        folderText.setText(null);
        folderText.setEnabled(false);
        folderText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(folderText, gridBagConstraints);

        pathText.setEditable(false);
        pathText.setText(null);
        pathText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(pathText, gridBagConstraints);

        browseButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.browseButton.text")); // NOI18N
        browseButton.setEnabled(false);
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(browseButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dataTypeLabel, org.openide.util.NbBundle.getMessage(DataSourceNameVisualPanel.class, "DataSourceNameVisualPanel.dataTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dataTypeLabel, gridBagConstraints);

        dataTypeCombo.setModel(new DefaultComboBoxModel(DataType.values()));
        dataTypeCombo.setEnabled(false);
        dataTypeCombo.setMinimumSize(new java.awt.Dimension(75, 18));
        dataTypeCombo.setRenderer(WidgetUtils.displayableListRenderer());
        dataTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataTypeComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(dataTypeCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void dataTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataTypeComboActionPerformed
        controller.panelChanged();
    }//GEN-LAST:event_dataTypeComboActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        DataFolder folder = DataObjectChooser.selectOneFolder(dop.getRootFolder());
        if(folder != null) {
            FileObject root = dop.getRootFolder().getPrimaryFile().getParent();
            FileObject child = folder.getPrimaryFile();
            folderText.setText(FileUtil.getRelativePath(root, child));
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JComboBox dataTypeCombo;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.Box.Filler filler;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JTextField folderText;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathText;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JLabel projectText;
    // End of variables declaration//GEN-END:variables
    
    private class TextListener implements DocumentListener {
        
        @Override
        public void changedUpdate(DocumentEvent e) {
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            documentEvent(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentEvent(e);
        }
        
        private void documentEvent(DocumentEvent e) {
            if(pathText.getDocument() != e.getDocument()) {
                pathText.setText(getEscapedFolder() + getEscapedName());
                controller.panelChanged();
            }
        }
        
        private String getEscapedFolder() {
            String folder = folderText.getText();
            if(!folder.endsWith("/"))
                folder += "/";
            return folder;
        }
        
        private String getEscapedName() {
            String name = nameText.getText();
            return name==null? "" : name;
        }
    }
}
