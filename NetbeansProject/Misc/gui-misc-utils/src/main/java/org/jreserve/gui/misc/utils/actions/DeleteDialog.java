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
package org.jreserve.gui.misc.utils.actions;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DeleteDialog.Title=Delete Items",
    "LBL.DeleteDialog.Question=Do you want to delete the following items?",
    "LBL.DeleteDialog.Ok=Ok",
    "LBL.DeleteDialog.Cancel=Cancel",
    "# {0} - name",
    "MSG.DeleteDialog.DeleteError=Unable to delete: {0}"
})
class DeleteDialog extends JPanel {
    
    private List<Deletable> deletables;
    private JButton okButton = new JButton(Bundle.LBL_DeleteDialog_Ok(), CommonIcons.ok());
    private JButton cancelButton = new JButton(Bundle.LBL_DeleteDialog_Cancel(), CommonIcons.cancel());
    private JProgressBar pBar = new JProgressBar();
    private Dialog dialog;
    
    DeleteDialog(List<Deletable> deletables) {
        this.deletables = deletables;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        JLabel question = new JLabel(Bundle.LBL_DeleteDialog_Question());
        add(question, BorderLayout.NORTH);
        add(createItemsPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }
    
    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=1d; gc.weighty=0d;
        gc.insets = new Insets(0, 10, 5, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        
        Iterator<Deletable> it = deletables.iterator();
        while(it.hasNext()) {
            Deletable d = it.next();
            
            if(!it.hasNext())
                gc.insets = new Insets(0, 10, 0, 0);
            
            panel.add(createLabel(d), gc);
            gc.gridy++;
        }
        
        gc.weighty=1d;
        gc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createGlue(), gc);
        
        return panel;
    }
    
    private JLabel createLabel(Deletable d) {
        String name = d.getDisplayName();
        Icon icon = d.getIcon();
        return new JLabel(name, icon, SwingConstants.LEFT);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0;
        gc.weightx=0d; gc.weighty=0d;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pBar, gc);
        
        gc.gridx++;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.weightx=1d;
        panel.add(Box.createHorizontalGlue(), gc);
        
        ButtonListener listener = new ButtonListener();
        gc.gridx++;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.weightx=0d;
        okButton.addActionListener(listener);
        panel.add(okButton, gc);
        
        gc.gridx++;
        gc.insets = new Insets(0, 0, 0, 0);
        cancelButton.addActionListener(listener);
        panel.add(cancelButton, gc);
        
        return panel;
    }
    
    void showDialog() {
        DialogDescriptor dd = new DialogDescriptor(
                this, Bundle.LBL_DeleteDialog_Title(), true,
                new Object[0], cancelButton, DialogDescriptor.DEFAULT_ALIGN,
                null, null);
        dialog = DialogDisplayer.getDefault().createDialog(dd);
        
        dialog.pack();
        pBar.setVisible(false);
        dialog.setVisible(true);
    }
    
    private class ButtonListener implements ActionListener, TaskListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(cancelButton == e.getSource()) {
                dialog.dispose();
            } else {
                pBar.setIndeterminate(true);
                pBar.setVisible(true);
                TaskUtil.executeSwingCallback(new DeleteTask(deletables), this);
            }
        }

        @Override
        public void taskFinished(Task task) {
            pBar.setIndeterminate(false);
            pBar.setVisible(false);
            dialog.dispose();
        }
    }
    
    private static class DeleteTask implements Runnable {
        
        private final List<Deletable> items;

        public DeleteTask(List<Deletable> items) {
            this.items = new ArrayList<Deletable>(items);
        }
        
        @Override
        public void run() {
            for(Deletable d : items)
                delete(d);
        }
        
        private void delete(Deletable d) {
            try {
                d.delete();
            } catch (Exception ex) {
                String msg = Bundle.MSG_DeleteDialog_DeleteError(d.getDisplayName());
                BubbleUtil.showException(msg, ex);
            }
        }
    }
    
}
