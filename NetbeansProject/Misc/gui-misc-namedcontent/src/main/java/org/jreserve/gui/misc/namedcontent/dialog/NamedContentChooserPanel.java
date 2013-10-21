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
package org.jreserve.gui.misc.namedcontent.dialog;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.jreserve.gui.misc.namedcontent.NamedContent;
import org.jreserve.gui.misc.namedcontent.NamedContentChooserController;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.TextPrompt;
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
    "PROMPT.NamedContentChooserPanel.Search=Search..."
})
public class NamedContentChooserPanel extends javax.swing.JPanel {
    
    private final static boolean MODAL = true;
    
    public static List<String> selectFolder(NamedContentChooserController controller, boolean multiSelect) {
        NamedContentChooserPanel panel = new NamedContentChooserPanel(controller, multiSelect, true);
        showPanel(panel, controller.getTitle());
        
        if(panel.closedWithOk)
            return getFolders(panel.selection);
        return Collections.EMPTY_LIST;
    }
    
    private static void showPanel(NamedContentChooserPanel panel, String title) {
        DialogDescriptor dd = new DialogDescriptor(
                panel, title, MODAL, 
                new Object[0], null, DialogDescriptor.DEFAULT_ALIGN, 
                HelpCtx.DEFAULT_HELP, null);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        panel.showDialog(dialog);
    }
    
    private static List<String> getFolders(List<TreeItem> selection) {
        List<String> result = new ArrayList<String>(selection.size());
        for(TreeItem item : selection)
            result.add(item.getPath());
        return result;
    }
    
    public static List<NamedContent> selectContent(NamedContentChooserController controller, boolean multiSelect) {
        NamedContentChooserPanel panel = new NamedContentChooserPanel(controller, multiSelect, false);
        showPanel(panel, controller.getTitle());
        if(panel.closedWithOk)
            return getContents(panel.selection);
        return Collections.EMPTY_LIST;
    }
    
    private static List<NamedContent> getContents(List<TreeItem> selection) {
        List<NamedContent> result = new ArrayList<NamedContent>(selection.size());
        for(TreeItem item : selection)
            result.add(((TreeFile)item).getContent());
        return result;
    }
    
    private final static String TREE_CARD = "tree";
    private final static String LIST_CARD = "list";
    
    private NamedContentChooserController controller;
    private boolean multiSelect;
    private boolean isFolder;
    
    private NamedContentTreeModel treeModel;
    private NamedContentListModel listModel;
    
    private ButtonListener buttonListener = new ButtonListener();
    private SelectionListener selectionListener = new SelectionListener();
    
    private List<TreeItem> selection = new ArrayList<TreeItem>();
    private boolean closedWithOk = false;
    private Dialog dialog;
    
    private NamedContentChooserPanel(NamedContentChooserController controller, boolean multiSelect, boolean isFolder) {
        this.controller = controller;
        this.multiSelect = multiSelect;
        this.isFolder = isFolder;
        
        TreeFolder root = isFolder? 
                TreeFolder.createFolders(controller) : 
                TreeFolder.createRoot(controller);
        treeModel = new NamedContentTreeModel(root);
        listModel = new NamedContentListModel(root, controller, isFolder);
        
        initComponents();
        TextPrompt.createStandard(Bundle.PROMPT_NamedContentChooserPanel_Search(), CommonIcons.search(), searchText);
        showCard(TREE_CARD);
    }
    
    private void showCard(String name) {
        CardLayout layout = (CardLayout) selectPanel.getLayout();
        layout.show(selectPanel, name);
    }
    
    private void showDialog(Dialog dialog) {
        this.dialog = dialog;
        this.dialog.pack();
        this.dialog.setVisible(true);
    }
    
    private void hideDialog() {
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
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

        searchPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchText = new javax.swing.JTextField();
        selectPanel = new javax.swing.JPanel();
        treeScroll = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        listScroll = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.BorderLayout(10, 10));

        searchPanel.setLayout(new java.awt.BorderLayout(5, 0));

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(NamedContentChooserPanel.class, "NamedContentChooserPanel.searchLabel.text")); // NOI18N
        searchPanel.add(searchLabel, java.awt.BorderLayout.LINE_START);

        searchText.setText(null);
        searchText.getDocument().addDocumentListener(new SearchTextListener());
        searchPanel.add(searchText, java.awt.BorderLayout.CENTER);

        add(searchPanel, java.awt.BorderLayout.NORTH);

        selectPanel.setLayout(new java.awt.CardLayout());

        tree.setModel(treeModel);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(selectionListener);
        tree.setCellRenderer(new TreeRenderer());
        treeScroll.setViewportView(tree);

        selectPanel.add(treeScroll, "tree");

        list.setModel(listModel);
        list.addListSelectionListener(selectionListener);
        list.setCellRenderer(new ListRenderer());
        listScroll.setViewportView(list);

        selectPanel.add(listScroll, "list");

        add(selectPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        buttonPanel.add(filler1, gridBagConstraints);

        okButton.setIcon(CommonIcons.ok());
        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(NamedContentChooserPanel.class, "NamedContentChooserPanel.okButton.text")); // NOI18N
        okButton.setEnabled(false);
        okButton.addActionListener (buttonListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        buttonPanel.add(okButton, gridBagConstraints);

        cancelButton.setIcon(CommonIcons.cancel());
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(NamedContentChooserPanel.class, "NamedContentChooserPanel.cancelButton.text")); // NOI18N
        cancelButton.addActionListener (buttonListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        buttonPanel.add(cancelButton, gridBagConstraints);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JList list;
    private javax.swing.JScrollPane listScroll;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField searchText;
    private javax.swing.JPanel selectPanel;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeScroll;
    // End of variables declaration//GEN-END:variables

    private void setListSelection() {
        Object[] sel = list.getSelectedValues();
        selectionChanged(sel);
    }
    
    private void selectionChanged(Object[] selection) {
        setSelection(selection);
        if(this.selection.isEmpty()) {
            okButton.setEnabled(false);
        } else if(this.selection.size() == 1) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(multiSelect);
        }
    }
    
    private void setSelection(Object[] selection) {
        this.selection.clear();
        for(Object item : selection) {
            TreeItem ti = (TreeItem) item;
            if(acceptsItem(ti))
                this.selection.add(ti);
        }
    }
    
    private boolean acceptsItem(TreeItem item) {
        if(isFolder) {
            return (item instanceof TreeFolder);
        } else {
            return (item instanceof TreeFile) &&
                   controller.acceptsContent(((TreeFile)item).getContent());
        }
    }
    
    private void setTreeSelection() {
        TreePath[] pathes = tree.getSelectionPaths();
        int size = pathes.length;
        Object[] sel = new Object[size];
        for(int i=0; i<size; i++)
            sel[i] = pathes[i].getLastPathComponent();
        selectionChanged(sel);
    }
    
    private class SearchTextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            setPath();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setPath();
        }
        
        private void setPath() {
            String path = searchText.getText();
            listModel.setPath(path);
            if(path == null || path.length() == 0) {
                showCard(TREE_CARD);
                setTreeSelection();
                searchText.repaint();
            } else {
                showCard(LIST_CARD);
                setListSelection();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }

    private class SelectionListener implements ListSelectionListener, TreeSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setListSelection();
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            setTreeSelection();
        }
    
    }
    
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancelButton)
                selection.clear();
            else
                closedWithOk = true;
            hideDialog();
        }
    
    }
    
}
