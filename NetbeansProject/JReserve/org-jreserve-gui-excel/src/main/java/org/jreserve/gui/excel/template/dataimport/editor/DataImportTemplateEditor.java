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
package org.jreserve.gui.excel.template.dataimport.editor;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.data.api.inport.ImportUtil;
import org.jreserve.gui.poi.ExcelFileFilter;
import org.jreserve.gui.poi.read.PoiUtil;
import org.jreserve.gui.poi.read.ReferenceUtil;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplate;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplateItem;
import org.jreserve.gui.excel.template.dataimport.DataImportTemplates;
import org.jreserve.gui.excel.template.dataimport.createwizard.SourceType;
import org.jreserve.gui.excel.template.dataimport.createwizard.TemplateRow;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.notifications.FileDialog;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.TextPrompt;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Task;
import org.openide.util.TaskListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataImportTemplateEditor.Title=Edit Template",
    "LBL.DataImportTemplateEditor.File.Prompt=Select tempalte",
    "LBL.DataImportTemplateEditor.File.DialogTitle=Select tempalte",
    "MSG.DataImportTemplateEditor.Excel.ReadError=Unable to read Excel file!",
    "MSG.DataImportTemplateEditor.Name.Empty=Name is not set!",
    "# {0} - name",
    "MSG.DataImportTemplateEditor.Name.Exists=Name ''{0}'' already exists!",
    "MSG.DataImportTemplateEditor.Rows.Empty=Tempalte must contain at least one item!",
    "MSG.DataImportTemplateEditor.Template.Same=Template does not changed!",
    "# {0} - row",
    "MSG.DataImportTemplateEditor.Data.Empty.Reference=Reference is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.DataImportTemplateEditor.Data.Invalid.Reference=Reference is invalid in row ''{0}''!",
    "# {0} - row",
    "MSG.DataImportTemplateEditor.Data.Empty.StartDate=Start date is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.DataImportTemplateEditor.Data.Empty.ALength=Accident length is not set in row ''{0}''!",
    "# {0} - row",
    "MSG.DataImportTemplateEditor.Data.Empty.DLength=Development length is not set in row ''{0}''!",
    "MSG.DataImportTemplateEditor.Rename.Error=Template can not be renamed!"
})
public class DataImportTemplateEditor extends javax.swing.JPanel {

    private final static boolean MODAL = true;
    
    public static void editTemplate(DataImportTemplate template) {
        DataImportTemplateEditor panel = new DataImportTemplateEditor(template);
        panel.setVisible();
    }
    
    private final InputListener inputListenr = new InputListener();
    private final ButtonListener buttonListener = new ButtonListener();
    private final DataImportTemplate template;
    private final List<TemplateRow> originalRows;
    private DialogDescriptor dd;
    private Dialog dialog;
    
    private DataImportTemplateEditor(DataImportTemplate template) {
        this.template = template;
        this.originalRows = createRows();
        initComponents();
        initDescriptor();
    }
    
    private List<TemplateRow> createRows() {
        List<TemplateRow> result = new ArrayList<TemplateRow>();
        for(DataImportTemplateItem item : template.getItems())
            result.add(createRow(item));
        return result;
    }
    
    private TemplateRow createRow(DataImportTemplateItem item) {
        TemplateRow row = new TemplateRow();
        row.setDataType(item.getDataType());
        row.setReference(item.getReference());
        row.setCummulated(item.isCummulated());
        if(item instanceof DataImportTemplateItem.Table) {
            row.setSourceType(SourceType.TABLE);
        } else {
            row.setSourceType(SourceType.TRIANGLE);
            DataImportTemplateItem.Triangle tItem = (DataImportTemplateItem.Triangle) item;
            row.setMonthDate(tItem.getStartDate());
            row.setAccidentLength(tItem.getAccidentLength());
            row.setDevelopmentLength(tItem.getDevelopmentLength());
        }
        return row;
    }
    
    private void initDescriptor() {
        dd = new DialogDescriptor(this, Bundle.LBL_DataImportTemplateEditor_Title());
        dd.setModal(MODAL);
        dd.setOptions(new Object[0]);
    }
    
    private void setVisible() {
        dialog = DialogDisplayer.getDefault().createDialog(dd);
        cancelButton.requestFocus();
        cancelButton.requestFocusInWindow();
        validateInput();
        dialog.pack();
        dialog.setVisible(true);
    }
    
    private void closeDialog() {
        dialog.setVisible(false);
        dialog.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fileLabel = new javax.swing.JLabel();
        fileText = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        itemsLabel = new javax.swing.JLabel();
        table = new org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateItemTable();
        msgLabel = new org.jreserve.gui.misc.utils.widgets.MessageLabel();
        bottomPanel = new javax.swing.JPanel();
        pBar = new javax.swing.JProgressBar();
        buttonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(fileLabel, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.fileLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(fileLabel, gridBagConstraints);

        fileText.setEditable(false);
        TextPrompt.createStandard(Bundle.LBL_DataImportTemplateEditor_File_Prompt(), fileText);
        fileText.setText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(fileText, gridBagConstraints);

        browseButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(browseButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.nameLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 5);
        add(nameLabel, gridBagConstraints);

        nameText.setText(template.getName());
        nameText.getDocument().addDocumentListener(inputListenr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 5);
        add(nameText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(itemsLabel, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.itemsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        add(itemsLabel, gridBagConstraints);

        table.addChangeListener(inputListenr);
        table.setRows(originalRows);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        add(table, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(msgLabel, null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(msgLabel, gridBagConstraints);

        bottomPanel.setLayout(new java.awt.GridBagLayout());

        pBar.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        bottomPanel.add(pBar, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        bottomPanel.add(buttonFiller, gridBagConstraints);

        okButton.setIcon(CommonIcons.ok());
        okButton.addActionListener(buttonListener);
        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.okButton.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        bottomPanel.add(okButton, gridBagConstraints);

        cancelButton.setIcon(CommonIcons.cancel());
        cancelButton.addActionListener(buttonListener);
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(DataImportTemplateEditor.class, "DataImportTemplateEditor.cancelButton.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        bottomPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(bottomPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        File f = FileDialog.openFile(new ExcelFileFilter(), Bundle.LBL_DataImportTemplateEditor_File_DialogTitle());
        if(f != null) {
            fileText.setText(f.getAbsolutePath());
            readExcel(f);
        }
    }//GEN-LAST:event_browseButtonActionPerformed
    
    private void readExcel(File file) {
        setProcessRunning(true);
        final PoiUtil.Task<ReferenceUtil> reader = PoiUtil.getReferenceUtilTask(file);
        Task task = ImportUtil.getRP().create(reader);
        task.addTaskListener(new TaskListener() {
            @Override
            public void taskFinished(Task task) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            table.setReferenceUtil(reader.get());
                        } catch (Exception ex) {
                            BubbleUtil.showException(Bundle.MSG_DataImportTemplateEditor_Excel_ReadError(), ex);
                            table.setReferenceUtil(null);
                        } finally {
                            setProcessRunning(false);
                        }
                    }
                });
            }
        });
        ImportUtil.getRP().execute(task);
    }
    
    private void setProcessRunning(boolean running) {
        pBar.setVisible(running);
        pBar.setIndeterminate(running);
        browseButton.setEnabled(!running);
        fileText.setEnabled(!running);
        nameText.setEnabled(!running);
        table.setEnabled(!running);
        
        if(running)
            okButton.setEnabled(false);
        else
            validateInput();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton browseButton;
    private javax.swing.Box.Filler buttonFiller;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JTextField fileText;
    private javax.swing.JLabel itemsLabel;
    private org.jreserve.gui.misc.utils.widgets.MessageLabel msgLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar pBar;
    private org.jreserve.gui.excel.template.dataimport.createwizard.ImportTemplateItemTable table;
    // End of variables declaration//GEN-END:variables
    
    private void validateInput() {
        boolean valid = isNameValid() && isRowsValid() && isChanged();
        okButton.setEnabled(valid);
        if(valid)
            msgLabel.setErrorMessage(null);
    }
    
    private boolean isNameValid() {
        String name = nameText.getText();
        if(name==null || name.length()==0) {
            msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Name_Empty());
            return false;
        }
        
        for(DataImportTemplate t : template.getManager().getTemplates()) {
            if(template!=t && name.equalsIgnoreCase(t.getName())) {
                msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Name_Exists(name));
                return false;
            }
        }
        return true;
    }
    
    private boolean isRowsValid() {
        List<TemplateRow> newRows = table.getRows();
        if(newRows.isEmpty()) {
            msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Rows_Empty());
            return false;
        }
        
        int rowIndex = 1;
        for(TemplateRow row : newRows)
            if(!isRowValid(row, rowIndex++))
                return false;
        
        return true;
    }
    
    private boolean isRowValid(TemplateRow row, int rowIndex) {
        String ref = row.getReference();
        if(ref==null || ref.length()==0) {
            msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Data_Empty_Reference(rowIndex));
            return false;
        }
        
        ReferenceUtil refUtil = table.getReferenceUtil();
        if(refUtil!=null && !refUtil.isReferenceValid(ref)) {
            msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Data_Invalid_Reference(rowIndex));
            return false;
        }
        
        if(SourceType.TRIANGLE == row.getSourceType()) {
            if(row.getMonthDate() == null) {
                msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Data_Empty_StartDate(rowIndex));
                return false;
            }
        
            if(row.getAccidentLength()==null || row.getAccidentLength()==0 || row.getAccidentLength() < 1) {
                msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Data_Empty_ALength(rowIndex));
                return false;
            }
        
            if(row.getDevelopmentLength()==null || row.getDevelopmentLength()==0 || row.getDevelopmentLength() < 1) {
                msgLabel.setErrorMessage(Bundle.MSG_DataImportTemplateEditor_Data_Empty_DLength(rowIndex));
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isChanged() {
        String newName = nameText.getText();
        List<TemplateRow> newRows = table.getRows();
        if(template.getName().equals(newName) && originalRows.equals(newRows)) {
            msgLabel.setWarningMessage(Bundle.MSG_DataImportTemplateEditor_Template_Same());
            return false;
        }
        return true;
    } 
    
    private class InputListener implements DocumentListener, ChangeListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            validateInput();
        }
    }
    
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(cancelButton == e.getSource()) {
                closeDialog();
            } else {
                String newName = nameText.getText();
                List<TemplateRow> newRows = table.getRows();
                if(originalRows.equals(newRows))
                    newRows = null;
                EditWorker worker = new EditWorker(template, newName, newRows);
                setProcessRunning(true);
                worker.execute();
            }
        }
    }
    
    private class EditWorker extends SwingWorker<Void, Void> {
        private final DataImportTemplate template;
        private final String newName;
        private final List<TemplateRow> newRows;

        EditWorker(DataImportTemplate template, String newName, List<TemplateRow> newRows) {
            this.template = template;
            this.newName = newName;
            this.newRows = newRows;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            if(newRows != null)
                template.setItmes(createTemplateItems());
        
            if(!newName.equals(template.getName()))
                rename();
            
            return null;
        }
    
        private List<DataImportTemplateItem> createTemplateItems() {
            List<DataImportTemplateItem> items = new ArrayList<DataImportTemplateItem>();
            for(TemplateRow row : newRows)
                items.add(row.createTempalteItem());
            return items;
        }
    
        private void rename() {
            try {
                DataImportTemplates m = (DataImportTemplates) template.getManager();
                m.renameTemplate(template, newName);
            } catch (IOException ex) {
                BubbleUtil.showException(Bundle.MSG_DataImportTemplateEditor_Rename_Error(), ex);
            }
        }

        @Override
        protected void done() {
            setProcessRunning(false);
            closeDialog();
        }
    
    }
}
