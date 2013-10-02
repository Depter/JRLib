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
package org.jreserve.gui.misc.renameable.dialog;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.misc.renameable.Renameable;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.MsgLabel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.RenameDialog.Title=Rename",
    "LBL.RenameDialog.Name=Name:",
    "LBL.RenameDialog.Ok=Ok",
    "LBL.RenameDialog.Cancel=Cancel",
    "MSG.RenameDialog.Name.Empty=Name is empty!",
    "MSG.RenameDialog.Name.Exists=Name already used!"
})
public class RenameDialog extends JPanel {
    
    private final static boolean MODAL = true;
    
    public static void rename(Renameable renameable) {
        RenameDialog content = new RenameDialog(renameable);
        DialogDescriptor dd = new DialogDescriptor(
            content, Bundle.LBL_RenameDialog_Title(), MODAL,
            new Object[0], null, DialogDescriptor.DEFAULT_ALIGN,
            HelpCtx.DEFAULT_HELP, null);
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        content.showDialog(dialog);
    }
    
    private Dialog dialog;
    private final Renameable renameable;
    
    private RenameDialog(Renameable renameable) {
        this.renameable = renameable;
        initComponents();
    }
    
    private void showDialog(Dialog dialog) {
        this.dialog = dialog;
        dialog.setVisible(true);
    }
    
    private void initComponents() {
        nameText = new JTextField();
        msgLabel = new MsgLabel();
        pBar = new JProgressBar();
        okButton = new JButton(Bundle.LBL_RenameDialog_Ok(), CommonIcons.ok());
        cancelButton = new JButton(Bundle.LBL_RenameDialog_Cancel(), CommonIcons.cancel());
        
        
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints gc = new GridBagConstraints();
        
        JLabel nameLabel = new JLabel(Bundle.LBL_RenameDialog_Name());
        gc.gridx=0; gc.gridy=0;
        gc.fill=GridBagConstraints.HORIZONTAL;
        gc.anchor=GridBagConstraints.BASELINE_LEADING;
        gc.insets = new Insets(0, 0, 10, 5);
        super.add(nameLabel, gc);
        
        nameText.getDocument().addDocumentListener(new NameListener());
        nameText.setText(renameable.getObject().getName());
        gc.gridx=1;
        gc.anchor=GridBagConstraints.BASELINE_TRAILING;
        gc.insets = new Insets(0, 0, 10, 0);
        super.add(nameText, gc);
        
        Component filler = Box.createGlue();
        filler.setPreferredSize(new Dimension(400, 20));
        gc.gridx=0; gc.gridy=1;
        gc.anchor=GridBagConstraints.CENTER;
        gc.gridwidth=2; 
        gc.weightx=1d; gc.weighty=1d;
        gc.insets = new Insets(0, 0, 0, 0);
        super.add(filler, gc);
        
        gc.gridy=2;
        gc.weighty=0d;
        gc.insets = new Insets(0, 0, 20, 0);
        super.add(msgLabel, gc);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        gc.gridy=3;
        gc.weighty=0d;
        gc.weightx=1d;
        gc.fill=GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 0, 0);
        super.add(buttonPanel, gc);
        
        pBar.setMinimumSize(new Dimension(0, 0));
        pBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        pBar.setVisible(false);
        buttonPanel.add(pBar);
        
        ButtonListener bl = new ButtonListener();
        okButton.addActionListener(bl);
        buttonPanel.add(okButton);
        
        cancelButton.addActionListener(bl);
        buttonPanel.add(cancelButton);
    }
    
    private JTextField nameText;
    private MsgLabel msgLabel;
    private JProgressBar pBar;
    private JButton okButton;
    private JButton cancelButton;
    
    private void checkName() {
        boolean valid = isNameValid();
        okButton.setEnabled(valid);
        if(valid)
            msgLabel.clearMessage();
    }
    
    private boolean isNameValid() {
        String name = nameText.getText();
        if(name == null || name.length() == 0) {
            msgLabel.showError(Bundle.MSG_RenameDialog_Name_Empty());
            return false;
        }
        
        DataObject obj = renameable.getObject();
        FileObject file = obj.getPrimaryFile();
        FileObject parent = file.getParent();
        
        file = parent.getFileObject(name, file.getExt());
        if(file != null) {
            msgLabel.showError(Bundle.MSG_RenameDialog_Name_Exists());
            return false;
        }
        return true;
    }
    
    private void startWorker() {
        nameText.setEnabled(false);
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
        new Worker().execute();
    }
    
    private void closeDialog() {
        dialog.setVisible(false);
        dialog.dispose();
    }
    
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if(cancelButton == source) {
                closeDialog();
            } else {
                startWorker();
            }
        }
    }
    
    private class NameListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            checkName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            checkName();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
    
    private class Worker extends RenameWorker {

        public Worker() {
            super(renameable.getObject(), nameText.getText(), pBar);
        }

        @Override
        protected void done() {
            closeDialog();
        }
    }
}
