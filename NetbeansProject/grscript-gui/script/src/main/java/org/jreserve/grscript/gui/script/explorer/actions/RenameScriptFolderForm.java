package org.jreserve.grscript.gui.script.explorer.actions;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFolder;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotificationLineSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.RenameScriptFolderForm.Title=Rename folder",
    "LBL.RenameScriptFolderForm.Name=Name:",
    "# {0} - name",
    "MSG.RenameScriptFolderForm.Name.Exists=Name \"{0}\" already exists!",
    "MSG.RenameScriptFolderForm.Name.NotChange=Name is not changed!",
    "MSG.RenameScriptFolderForm.Name.Empty=Name is empty!"
})
public class RenameScriptFolderForm extends javax.swing.JPanel {


    private final static boolean MODAL = true;
    private DialogDescriptor dd;
    private NotificationLineSupport nls;
    private ScriptFolder folder;
    private ButtonListener buttonListener = new ButtonListener();

    public RenameScriptFolderForm(ScriptFolder folder) {
        this.folder = folder;
        initComponents();
        initDialog();
        validateName();
    }

    private void initDialog() {
        dd = new DialogDescriptor(this, Bundle.LBL_RenameScriptFolderForm_Title(), MODAL, buttonListener);
        nls = dd.createNotificationLineSupport();
    }

    void showDialog() {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);
    }

    private void validateName() {
        String name = nameText.getText();
        boolean valid = nameNotEmpty(name) && nameNotUsed(name) && nameChanged(name);
        dd.setValid(valid);
        if (valid) {
            nls.clearMessages();
        }
    }

    private boolean nameNotEmpty(String name) {
        if (name == null || name.trim().length() == 0) {
            nls.setErrorMessage(Bundle.MSG_RenameScriptFolderForm_Name_Empty());
            return false;
        }
        return true;
    }

    private boolean nameNotUsed(String name) {
        for (ScriptFolder child : folder.getParent().getFolders()) {
            if(child!=folder && name.equalsIgnoreCase(child.getName())) {
                nls.setErrorMessage(Bundle.MSG_RenameScriptFolderForm_Name_Exists(name));
                return false;
            }
        }
        return true;
    }
    
    private boolean nameChanged(String name) {
        if(folder.getName().equals(name)) {
            nls.setWarningMessage(Bundle.MSG_RenameScriptFolderForm_Name_NotChange());
            return false;
        }
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

        nameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, Bundle.LBL_AddScriptFolderForm_Name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(nameLabel, gridBagConstraints);

        nameText.setText(folder.getName());
        nameText.getDocument().addDocumentListener(new NameListener());
        nameText.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        add(nameText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    // End of variables declaration//GEN-END:variables

    private class NameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateName();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == DialogDescriptor.OK_OPTION) {
                String name = nameText.getText().trim();
                folder.setName(name);
            }
        }
    }
}