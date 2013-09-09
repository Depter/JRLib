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
package org.jreserve.gui.project.newproject;

import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.NewProjectWizardPanelVisual.FileChooser.Title=Select Project Location",
    "LBL.NewProjectWizardPanelVisual.DefaultProjectName=Project",
    "MSG.NewProjectWizardPanelVisual.Invalid.Name=Project Name is not a valid folder name.",
    "MSG.NewProjectWizardPanelVisual.Invalid.Folder=Project Folder is not a valid path.",
    "MSG.NewProjectWizardPanelVisual.Invalid.Folder.Create=Project Folder cannot be created.",
    "MSG.NewProjectWizardPanelVisual.Invalid.Folder.Path=Project Folder is not a valid path.",
    "MSG.NewProjectWizardPanelVisual.Invalid.Folder.Exists=Project Folder already exists and is not empty."
})
class NewProjectWizardPanelVisual extends javax.swing.JPanel {
    
    private final static String PROP_FOLDER = "new.project.folder";
    
    private final NewProjectWizardPanel panel;
    private TextListener textListener = new TextListener();
    
    NewProjectWizardPanelVisual(NewProjectWizardPanel panel) {
        this.panel = panel;
        initComponents();
    }
    
    void read(WizardDescriptor wizard) {
        readFolder(wizard);
        readName(wizard);
    }
    
    private void readFolder(WizardDescriptor wizard) {
        File folder = (File) wizard.getProperty(NewProjectWizardIterator.PROP_PROJECT_FOLDER);
        if(folder == null || folder.getParentFile() == null || !folder.getParentFile().isDirectory()) {
            String prev = NbPreferences.forModule(NewProjectWizardPanelVisual.class).get(PROP_FOLDER, null);
            if(prev != null)
                folder = new File(prev);
            else
                folder = ProjectChooser.getProjectsFolder();
        } else {
            folder = folder.getParentFile();
        }
        this.folderText.setText(folder.getAbsolutePath());
    }
    
    private void readName(WizardDescriptor wizard) {
        String name = (String) wizard.getProperty(NewProjectWizardIterator.PROP_PROJECT_NAME);
        if(name == null)
            name = Bundle.LBL_NewProjectWizardPanelVisual_DefaultProjectName();
        this.nameText.setText(name);
        this.nameText.selectAll();
    }
    
    void store(WizardDescriptor wizard) {
        String name = nameText.getText().trim();
        wizard.putProperty(NewProjectWizardIterator.PROP_PROJECT_NAME, name);

        String folder = folderText.getText().trim();
        wizard.putProperty(NewProjectWizardIterator.PROP_PROJECT_FOLDER, new File(folder));
        NbPreferences.forModule(NewProjectWizardPanelVisual.class).put(PROP_FOLDER, folder);
    }
    
    boolean isValide(WizardDescriptor wizard) {
        boolean isValid = nameValid(wizard) && 
                         folderValid(wizard) && 
                         locationValid(wizard) &&
                         locationEmpty(wizard);
        if(isValid)
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null);
        return isValid;
    }
    
    private boolean nameValid(WizardDescriptor wizard) {
        if (nameText.getText().length() == 0) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_NewProjectWizardPanelVisual_Invalid_Name());
            return false;
        }
        return true;
    }
    
    private boolean folderValid(WizardDescriptor wizard) {
        File f = FileUtil.normalizeFile(new File(folderText.getText()).getAbsoluteFile());
        if (!f.isDirectory()) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_NewProjectWizardPanelVisual_Invalid_Folder());
            return false;
        }
        return true;
    }
    
    private boolean locationValid(WizardDescriptor wizard) {
        File projLoc = FileUtil.normalizeFile(new File(pathText.getText()).getAbsoluteFile());

        while (projLoc != null && !projLoc.exists())
            projLoc = projLoc.getParentFile();
        
        if (projLoc == null || !projLoc.canWrite()) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_NewProjectWizardPanelVisual_Invalid_Folder_Create());
            return false;
        }

        if (FileUtil.toFileObject(projLoc) == null) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_NewProjectWizardPanelVisual_Invalid_Folder_Path());
            return false;
        }
        
        return true;
    }
    
    private boolean locationEmpty(WizardDescriptor wizard) {
        File destFolder = FileUtil.normalizeFile(new File(pathText.getText()).getAbsoluteFile());
        File[] kids = destFolder.listFiles();
        if (destFolder.exists() && kids != null && kids.length > 0) {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, Bundle.MSG_NewProjectWizardPanelVisual_Invalid_Folder_Exists());
            return false;
        }
        return true;
    }
    
    void validate(WizardDescriptor wizard) {
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

        nameLabel = new javax.swing.JLabel();
        folderLabel = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        folderText = new javax.swing.JTextField();
        pathText = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        bottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(NewProjectWizardPanelVisual.class, "NewProjectWizardPanelVisual.nameLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(nameLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(folderLabel, org.openide.util.NbBundle.getMessage(NewProjectWizardPanelVisual.class, "NewProjectWizardPanelVisual.folderLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(folderLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(NewProjectWizardPanelVisual.class, "NewProjectWizardPanelVisual.pathLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(pathLabel, gridBagConstraints);

        nameText.setText(null);
        nameText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(nameText, gridBagConstraints);

        folderText.setText(null);
        folderText.getDocument().addDocumentListener(textListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(folderText, gridBagConstraints);

        pathText.setEditable(false);
        pathText.setText(null);
        pathText.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(pathText, gridBagConstraints);

        browseButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(NewProjectWizardPanelVisual.class, "NewProjectWizardPanelVisual.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(browseButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(bottomFiller, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        File folder = userSelectsFolder();
        if(folder != null)
            folderText.setText(FileUtil.normalizeFile(folder).getAbsolutePath());
        panel.fireChange();
    }//GEN-LAST:event_browseButtonActionPerformed

    private File userSelectsFolder() {
        return new FileChooserBuilder(Object.class)
                .setDirectoriesOnly(true).setTitle(Bundle.LBL_NewProjectWizardPanelVisual_FileChooser_Title())
                .showOpenDialog();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler bottomFiller;
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JTextField folderText;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathText;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        nameText.requestFocus();
    }
    
    private class TextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateTexts(e);
            if(nameText.getDocument() == e.getDocument())
                firePropertyChange(NewProjectWizardIterator.PROP_PROJECT_NAME, null, nameText.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateTexts(e);
            if(nameText.getDocument() == e.getDocument())
                firePropertyChange(NewProjectWizardIterator.PROP_PROJECT_NAME, null, nameText.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateTexts(e);
            if(nameText.getDocument() == e.getDocument())
                firePropertyChange(NewProjectWizardIterator.PROP_PROJECT_NAME, null, nameText.getText());
        }
        
        private void updateTexts(DocumentEvent evt) {
            Document doc = evt.getDocument();

            if(doc==nameText.getDocument() || doc==folderText.getDocument()) {
                String projectName = nameText.getText();
                String projectFolder = folderText.getText();
                pathText.setText(projectFolder + File.separatorChar + projectName);
            }
            panel.fireChange();
        }
        
    }
}
