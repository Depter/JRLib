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
package org.jreserve.gui.calculations.triangle.wizard;

import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.dataobject.DataObjectChooser;
import org.jreserve.gui.misc.utils.dataobject.ProjectObjectLookup;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.TextPrompt;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataObject;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CreateClaimTriangleVisualPanel2.Name=Data & Geometry",
    "LBL.CreateClaimTriangleVisualPanel2.Storge.Prompt=Select Storage",
    "LBL.CreateClaimTriangleVisualPanel2.Storge.Select=Select Storage"
})
class CreateClaimTriangleVisualPanel2 extends javax.swing.JPanel {

    private final static Icon LINK_IMG = ImageUtilities.loadImageIcon("org/jreserve/gui/calculations/icons/chain.png", false);   //NOI18
    private final static String PREF_DS_PATH = "CreateClaimTriangleVisualPanel2.dataSource";
    private final static String PREF_START_DATE = "CreateClaimTriangleVisualPanel2.startDate";
    private final static String PREF_END_DATE = "CreateClaimTriangleVisualPanel2.endDate";
    private final static String PREF_AL = "CreateClaimTriangleVisualPanel2.accidentLength";
    private final static String PREF_DL = "CreateClaimTriangleVisualPanel2.developmentLength";
    private final static String PREF_SYMMETRIC = "CreateClaimTriangleVisualPanel2.symmetric";
    
    private final CreateClaimTriangleWizardPanel2 controller;
    private final InputListener inputListener = new InputListener();
    private DataSourceObjectProvider dop;
    private ProjectObjectLookup pol;
    
    CreateClaimTriangleVisualPanel2(CreateClaimTriangleWizardPanel2 controller) {
        this.controller = controller;
        initComponents();
        disableTextEditor(accidentLengthSpinner);
        disableTextEditor(developmentLengthSpinner);
    }
    
    private void disableTextEditor(JSpinner spinner) {
        Object editor = spinner.getEditor();
        if(editor instanceof JSpinner.DefaultEditor)
            ((JSpinner.DefaultEditor) editor).getTextField().setEditable(false);
    }
    
    void loadSettings() {
        Preferences prefs = NbPreferences.forModule(CreateClaimTriangleVisualPanel2.class);
        
        accidentLengthSpinner.setValue(prefs.getInt(PREF_AL, 12));
        developmentLengthSpinner.setValue(prefs.getInt(PREF_DL, 12));
        if(prefs.getBoolean(PREF_SYMMETRIC, true)) {
            developmentLengthSpinner.setEnabled(false);
            symmetricButton.setSelected(true);
        } else {
            developmentLengthSpinner.setEnabled(true);
            symmetricButton.setSelected(false);
        }
        
        String sd = prefs.get(PREF_START_DATE, null);
        if(sd != null)
            startDateSpinner.setMonthDate(new MonthDate(sd));
        
        String ed = prefs.get(PREF_END_DATE, null);
        if(ed != null)
            endDateSpinner.setMonthDate(new MonthDate(ed));
    }
    
    @Override
    public String getName() {
        return Bundle.LBL_CreateClaimTriangleVisualPanel2_Name();
    }
    
    void setProject(Project p) {
        dop = p.getLookup().lookup(DataSourceObjectProvider.class);
        pol = p.getLookup().lookup(ProjectObjectLookup.class);
        browseSourceButton.setEnabled(dop != null);
    }
    
    DataSourceObjectProvider getDataSourceProvider() {
        return dop;
    }
    
    ProjectObjectLookup getProjectObjectLookup() {
        return pol;
    }
    
    String getDataSourcePath() {
        return sourceText.getText();
    }
    
    MonthDate getStartDate() {
        return startDateSpinner.getMonthDate();
    }
    
    MonthDate getEndDate() {
        return endDateSpinner.getMonthDate();
    }

    int getAccidentLength() {
        return (Integer) accidentLengthSpinner.getValue();
    }
    
    int getDevelopmentLength() {
        return (Integer) developmentLengthSpinner.getValue();
    }
    
    void storeSettings() {
        Preferences prefs = NbPreferences.forModule(CreateClaimTriangleVisualPanel2.class);
        prefs.put(PREF_DS_PATH, getDataSourcePath());
        prefs.put(PREF_START_DATE, getStartDate().toString());
        prefs.put(PREF_END_DATE, getEndDate().toString());
        prefs.putInt(PREF_AL, getAccidentLength());
        prefs.putInt(PREF_DL, getDevelopmentLength());
        prefs.putBoolean(PREF_SYMMETRIC, symmetricButton.isSelected());
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
        browseSourceButton = new javax.swing.JButton();
        geometryLabel = new javax.swing.JLabel();
        intervalLabel = new javax.swing.JLabel();
        intervalSeparatorLabel = new javax.swing.JLabel();
        startDateSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        endDateSpinner = new org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner();
        startLabel = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        accidentLabel = new javax.swing.JLabel();
        developmentLabel = new javax.swing.JLabel();
        lengthLabel = new javax.swing.JLabel();
        accidentLengthSpinner = new javax.swing.JSpinner();
        symmetricButton = new javax.swing.JToggleButton();
        developmentLengthSpinner = new javax.swing.JSpinner();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(sourceLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.sourceLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 5);
        add(sourceLabel, gridBagConstraints);

        sourceText.setText(null);
        sourceText.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.sourceText.toolTipText")); // NOI18N
        sourceText.getDocument().addDocumentListener(inputListener);
        TextPrompt.createStandard(Bundle.LBL_CreateClaimTriangleVisualPanel2_Storge_Prompt(), sourceText);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 5);
        add(sourceText, gridBagConstraints);

        browseSourceButton.setIcon(CommonIcons.search());
        org.openide.awt.Mnemonics.setLocalizedText(browseSourceButton, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.browseSourceButton.text")); // NOI18N
        browseSourceButton.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.browseSourceButton.toolTipText")); // NOI18N
        browseSourceButton.setEnabled(false);
        browseSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseSourceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        add(browseSourceButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(geometryLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.geometryLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(geometryLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(intervalLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.intervalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 5);
        add(intervalLabel, gridBagConstraints);

        intervalSeparatorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(intervalSeparatorLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.intervalSeparatorLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(intervalSeparatorLabel, gridBagConstraints);

        startDateSpinner.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.startDateSpinner.toolTipText")); // NOI18N
        startDateSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(startDateSpinner, gridBagConstraints);

        endDateSpinner.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.endDateSpinner.toolTipText")); // NOI18N
        endDateSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 5);
        add(endDateSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(startLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.startLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(startLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(endLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.endLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(endLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(accidentLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.accidentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(accidentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(developmentLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.developmentLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(developmentLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lengthLabel, org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.lengthLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 5);
        add(lengthLabel, gridBagConstraints);

        accidentLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
        accidentLengthSpinner.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.accidentLengthSpinner.toolTipText")); // NOI18N
        accidentLengthSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(accidentLengthSpinner, gridBagConstraints);

        symmetricButton.setIcon(LINK_IMG);
        symmetricButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(symmetricButton, null);
        symmetricButton.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.symmetricButton.toolTipText")); // NOI18N
        symmetricButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(symmetricButton, gridBagConstraints);

        developmentLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
        developmentLengthSpinner.setToolTipText(org.openide.util.NbBundle.getMessage(CreateClaimTriangleVisualPanel2.class, "CreateClaimTriangleVisualPanel2.developmentLengthSpinner.toolTipText")); // NOI18N
        developmentLengthSpinner.setEnabled(false);
        developmentLengthSpinner.addChangeListener(inputListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(developmentLengthSpinner, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void symmetricButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricButtonActionPerformed
        if(symmetricButton.isSelected()) {
            developmentLengthSpinner.setValue(accidentLengthSpinner.getValue());
            developmentLengthSpinner.setEnabled(false);
        } else {
            developmentLengthSpinner.setEnabled(true);
        }
    }//GEN-LAST:event_symmetricButtonActionPerformed

    private void browseSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseSourceButtonActionPerformed
        DataObject obj = DataObjectChooser.selectOne(new DataSourceController());
        if(obj != null) {
            DataSource ds = obj.getLookup().lookup(DataSource.class);
            sourceText.setText(ds.getPath());
        }
    }//GEN-LAST:event_browseSourceButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accidentLabel;
    private javax.swing.JSpinner accidentLengthSpinner;
    private javax.swing.JButton browseSourceButton;
    private javax.swing.JLabel developmentLabel;
    private javax.swing.JSpinner developmentLengthSpinner;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner endDateSpinner;
    private javax.swing.JLabel endLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel geometryLabel;
    private javax.swing.JLabel intervalLabel;
    private javax.swing.JLabel intervalSeparatorLabel;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JLabel sourceLabel;
    private javax.swing.JTextField sourceText;
    private org.jreserve.gui.trianglewidget.geometry.MonthDateSpinner startDateSpinner;
    private javax.swing.JLabel startLabel;
    private javax.swing.JToggleButton symmetricButton;
    // End of variables declaration//GEN-END:variables

    private class InputListener implements ChangeListener, DocumentListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if(e.getSource() == accidentLengthSpinner && symmetricButton.isSelected())
                developmentLengthSpinner.setValue(accidentLengthSpinner.getValue());
            controller.panelChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            controller.panelChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            controller.panelChanged();
        }
    }
    
    private class DataSourceController extends DataObjectChooser.DefaultController {

        public DataSourceController() {
            super(Bundle.LBL_CreateClaimTriangleVisualPanel2_Storge_Select(), dop.getRootFolder());
        }

        @Override
        public boolean showDataObject(DataObject obj) {
            return super.showDataObject(obj) ||
                   obj.getLookup().lookup(DataSource.class) != null;
        }

        @Override
        public boolean canSelectObject(DataObject obj) {
            return obj.getLookup().lookup(DataSource.class) != null;
        }
    }
}
