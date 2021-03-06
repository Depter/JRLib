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
package org.jreserve.gui.calculations.factor.editor;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jreserve.gui.calculations.api.CalculationContents;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.CalculationProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.factor.impl.factors.FactorTriangleCalculationImpl;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.netbeans.api.project.Project;
import org.openide.awt.UndoRedo;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.SourceEditorPanel.Source.NoProvider=Project does not contain any claim triangles!",
    "MSG.SourceEditorPanel.Source.Empty=Field 'Claim Triangle' is empty!",
    "MSG.SourceEditorPanel.Source.NotFound=Claim triangle not found!"
})
class SourceEditorPanel extends javax.swing.JPanel {
    
    private FactorTriangleCalculationImpl calculation;
    private UndoRedo.Manager undo;

    SourceEditorPanel() {
        initComponents();
        EventBusManager.getDefault().subscribe(this);
        msgLabel.showError(Bundle.MSG_SourceEditorPanel_Source_NoProvider());
    }
    
    void setCalculation(FactorTriangleCalculationImpl factors) {
        this.calculation = factors;
        Project p = calculation.getProject();
        if(p != null && CalculationContents.containsCalculations(p)) {
            sourceText.setEnabled(true);
            browseButton.setEnabled(true);
            
            ClaimTriangleCalculation source = calculation.getSource();
            sourceText.setText(source==null? null : source.getPath());
            msgLabel.clearMessage();
        }
    }
    
    void setUndo(UndoRedo.Manager undo) {
        this.undo = undo;
    }
    
    void componentClosed() {
        EventBusManager.getDefault().unsubscribe(this);
        undo = null;
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

        sourceLabel = new javax.swing.JLabel();
        sourceText = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        msgLabel = new org.jreserve.gui.misc.utils.widgets.MsgLabel();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(sourceLabel, org.openide.util.NbBundle.getMessage(SourceEditorPanel.class, "SourceEditorPanel.sourceLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(sourceLabel, gridBagConstraints);

        sourceText.setText(null);
        sourceText.setEnabled(false);
        sourceText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(sourceText, gridBagConstraints);

        browseButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(SourceEditorPanel.class, "SourceEditorPanel.browseButton.text")); // NOI18N
        browseButton.setEnabled(false);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(browseButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(msgLabel, org.openide.util.NbBundle.getMessage(SourceEditorPanel.class, "SourceEditorPanel.msgLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(msgLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        ClaimTriangleCalculation ctc = CalculationContents.selectOne(calculation.getProject(), ClaimTriangleCalculation.class);
        if(ctc != null)
            setSource(ctc);
    }//GEN-LAST:event_browseButtonActionPerformed

    private void setSource(ClaimTriangleCalculation ctc) {
        SourceEdit edit = new SourceEdit();
        calculation.setSource(ctc);
        edit.initPostState();
    }

    private void sourceTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceTextActionPerformed
        String path = sourceText.getText();
        if(path == null || path.length() == 0) {
            msgLabel.showError(Bundle.MSG_SourceEditorPanel_Source_Empty());
            return;
        }
        
        ClaimTriangleCalculation ctc = CalculationContents.getCalculation(calculation.getProject(), path, ClaimTriangleCalculation.class);
        if(ctc == null) {
            msgLabel.showError(Bundle.MSG_SourceEditorPanel_Source_NotFound());
            return;
        }
        
        setSource(ctc);
    }//GEN-LAST:event_sourceTextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.Box.Filler filler;
    private org.jreserve.gui.misc.utils.widgets.MsgLabel msgLabel;
    private javax.swing.JLabel sourceLabel;
    private javax.swing.JTextField sourceText;
    // End of variables declaration//GEN-END:variables
    
    @EventBusListener(forceEDT = true)
    public void sourcePathChanged(CalculationEvent.Renamed evt) {
        CalculationProvider cp = evt.getCalculationProvider();
        if(cp == calculation.getSource())
            sourceText.setText(cp.getPath());
    }

    private class SourceEdit extends AbstractUndoableEdit {
        
        private ClaimTriangleCalculation preEdit;
        private ClaimTriangleCalculation postEdit;

        SourceEdit() {
            preEdit = calculation.getSource();
        }
        
        void initPostState() {
            postEdit = calculation.getSource();
            if(undo != null)
                undo.undoableEditHappened(new UndoableEditEvent(SourceEditorPanel.this, this));
        }

        @Override
        public void die() {
            preEdit = null;
            postEdit = null;
            super.die();
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            calculation.setSource(preEdit);
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            calculation.setSource(postEdit);
        }
    }
}
