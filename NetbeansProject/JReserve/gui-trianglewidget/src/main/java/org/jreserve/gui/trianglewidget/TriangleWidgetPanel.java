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
package org.jreserve.gui.trianglewidget;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.trianglewidget.model.TitleModel;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
import org.jreserve.gui.trianglewidget.model.registration.TriangleModelAdapter;
import org.jreserve.gui.trianglewidget.model.registration.TriangleModelRegistry;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidgetPanel extends javax.swing.JPanel {
    
    private List<TriangleLayer> layers = new ArrayList<TriangleLayer>();
    
    public TriangleWidgetPanel() {
        initComponents();
    }
    
    public void setCummulated(boolean cummulated) {
        triangleWidget.setCummulated(cummulated);
    }
    
    public boolean isCummulated() {
        return triangleWidget.isCummulated();
    }
    
    public Map<String, TriangleModel> getModels() {
        int size = layoutCombo.getItemCount();
        Map<String, TriangleModel> result = new TreeMap<String, TriangleModel>();
        for(int i=0; i<size; i++) {
            TriangleModelAdapter adapter = (TriangleModelAdapter) layoutCombo.getItemAt(i);
            result.put(adapter.getId(), adapter.getTriangleModel());
        }
        return result;
    }
    
    public TriangleModel getModel() {
        TriangleModelAdapter adapter = (TriangleModelAdapter) layoutCombo.getSelectedItem();
        if(adapter == null)
            return null;
        return adapter.getTriangleModel();
    }
    
    public String getModelId() {
        TriangleModelAdapter adapter = (TriangleModelAdapter) layoutCombo.getSelectedItem();
        return adapter==null? null : adapter.getId();
    }
    
    public void setModelId(String id) {
        int index = getModelIndex(id);
        layoutCombo.setSelectedIndex(index);
    }
    
    private int getModelIndex(String layoutId) {
        int size = layoutCombo.getItemCount();
        for(int i=0; i<size; i++)
            if(((TriangleModelAdapter) layoutCombo.getItemAt(i)).getId().equals(layoutId))
                return i;
        return -1;
    }
    
    public void setLayers(TriangleLayer... layers) {
        setLayers(Arrays.asList(layers));
    }
    
    public void setLayers(List<TriangleLayer> layers) {
        setNewLayers(layers);
        layerCombo.setModel(new DefaultComboBoxModel(this.layers.toArray()));
        if(layers != null && !layers.isEmpty())
            layerCombo.setSelectedIndex(layers.size()-1);
    }
    
    private void setNewLayers(List<TriangleLayer> layers) {
        if(layers == null)
            layers = Collections.EMPTY_LIST;
        this.layers.clear();
        this.layers.addAll(layers);
    }
    
    public void setSelectedLayer(TriangleLayer layer) {
        int index = this.layers.indexOf(layer);
        layerCombo.setSelectedIndex(index);
    }
    
    public List<TriangleLayer> getLayers() {
        return new ArrayList<TriangleLayer>(layers);
    }
    
    public TriangleSelectionModel getTriangleSelectionModel() {
        return triangleWidget.getTriangleSelectionModel();
    }
    
    public void setDecimalCount(int decimalCount) {
        scaleSpinner.setValue(decimalCount);
    }
    
    public int getDecimalCount(int decimalCount) {
        return (Integer) scaleSpinner.getValue();
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        triangleWidget.setTriangleGeometry(geometry);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return triangleWidget.getTriangleGeometry();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        toolBarPanel = new javax.swing.JPanel();
        toolBarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        cumulatedLabel = new javax.swing.JLabel();
        cummulatedCheck = new javax.swing.JCheckBox();
        scaleLabel = new javax.swing.JLabel();
        scaleSpinner = new org.jreserve.gui.localesettings.ScaleSpinner();
        layoutLabel = new javax.swing.JLabel();
        layoutCombo = new javax.swing.JComboBox();
        layerLabel = new javax.swing.JLabel();
        layerCombo = new javax.swing.JComboBox();
        widgetScroll = new javax.swing.JScrollPane();
        triangleWidget = new org.jreserve.gui.trianglewidget.TriangleWidget();

        setLayout(new java.awt.BorderLayout());

        toolBarPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(toolBarFiller, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cumulatedLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.cumulatedLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(cumulatedLabel, gridBagConstraints);

        cummulatedCheck.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cummulatedCheck, null);
        cummulatedCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cummulatedCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(cummulatedCheck, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(scaleLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.scaleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(scaleLabel, gridBagConstraints);

        scaleSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scaleSpinnerChanged(e);
            }
        });

        scaleSpinner.setValue(LocaleSettings.getDecimalCount());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(scaleSpinner, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(layoutLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.layoutLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(layoutLabel, gridBagConstraints);

        layoutCombo.setModel(new DefaultComboBoxModel(TriangleModelRegistry.getAdapters().toArray()));
        layoutCombo.setRenderer(WidgetUtils.displayableListRenderer());
        layoutCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                layoutComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(layoutCombo, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(layerLabel, org.openide.util.NbBundle.getMessage(TriangleWidgetPanel.class, "TriangleWidgetPanel.layerLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        toolBarPanel.add(layerLabel, gridBagConstraints);

        layerCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        layerCombo.setRenderer(WidgetUtils.displayableListRenderer());
        layerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                layerComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        toolBarPanel.add(layerCombo, gridBagConstraints);

        add(toolBarPanel, java.awt.BorderLayout.NORTH);

        widgetScroll.setViewportView(triangleWidget);

        add(widgetScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void layoutComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layoutComboActionPerformed
        TriangleModelAdapter adapter = (TriangleModelAdapter) layoutCombo.getSelectedItem();
        if(adapter != null)
            triangleWidget.setModel(adapter.getTriangleModel());
    }//GEN-LAST:event_layoutComboActionPerformed

    private void layerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layerComboActionPerformed
        int index = layerCombo.getSelectedIndex();
        List<TriangleLayer> selected = new ArrayList<TriangleLayer>();
        for(int i=0; i<=index; i++) 
            selected.add((TriangleLayer)layerCombo.getItemAt(i));
        triangleWidget.setLayers(selected);
    }//GEN-LAST:event_layerComboActionPerformed

    private void cummulatedCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cummulatedCheckActionPerformed
        triangleWidget.setCummulated(cummulatedCheck.isSelected());
    }//GEN-LAST:event_cummulatedCheckActionPerformed

    private void scaleSpinnerChanged(ChangeEvent evt) {
        LocaleSettings.DecimalFormatter df = triangleWidget.getDecimalFormatter();
        df.setDecimalCount((Integer)scaleSpinner.getValue());
        triangleWidget.setDecimalFormatter(df);
    }
    
    public void setCummulatedControlVisible(boolean visible) {
        cumulatedLabel.setVisible(visible);
        cummulatedCheck.setVisible(visible);
    }
    
    public boolean isCummulatedControlVisible() {
        return cummulatedCheck.isVisible();
    }
    
    public void setScaleControlVisible(boolean visible) {
        scaleLabel.setVisible(visible);
        scaleSpinner.setVisible(visible);
    }
    
    public boolean isScaleControlVisible() {
        return scaleSpinner.isVisible();
    }
    
    public void setLayerControlVisible(boolean visible) {
        layerLabel.setVisible(visible);
        layerCombo.setVisible(visible);
    }
    
    public boolean isLayerControlVisible() {
        return layerCombo.isVisible();
    }
    
    public void setLayoutControlVisible(boolean visible) {
        layoutLabel.setVisible(visible);
        layoutCombo.setVisible(visible);
    }
    
    public boolean isLayoutControlVisible() {
        return layoutCombo.isVisible();
    }
    
    public ClipboardUtil.Copiable createCopiable() {
        return new TriangleCopiable();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cummulatedCheck;
    private javax.swing.JLabel cumulatedLabel;
    private javax.swing.JComboBox layerCombo;
    private javax.swing.JLabel layerLabel;
    private javax.swing.JComboBox layoutCombo;
    private javax.swing.JLabel layoutLabel;
    private javax.swing.JLabel scaleLabel;
    private org.jreserve.gui.localesettings.ScaleSpinner scaleSpinner;
    private javax.swing.Box.Filler toolBarFiller;
    private javax.swing.JPanel toolBarPanel;
    private org.jreserve.gui.trianglewidget.TriangleWidget triangleWidget;
    private javax.swing.JScrollPane widgetScroll;
    // End of variables declaration//GEN-END:variables

    private class TriangleCopiable implements ClipboardUtil.Copiable {

        private char separator;
        private String nan;
        private String inf;
        private StringBuilder sb = new StringBuilder();
        private TriangleModel model;
        private TitleModel columnNames;
        private TitleModel rowNames;
        private int rows;
        private int columns;
        private double[][] values;
        
        @Override
        public boolean canCopy() {
            return true;
        }
        
        @Override
        public Transferable clipboardCopy() throws IOException {
            initState();
            String result = toStringTriangle();
            clearState();
            return new StringSelection(result);
        }
        
        private void initState() {
            separator = LocaleSettings.getDecimalSeparator();
            nan = LocaleSettings.getNaN();
            inf = LocaleSettings.getInfinity();
            model = triangleWidget.getModel();
            rowNames = model.getVerticalTitleModel();
            columnNames = model.getHorizontalTitleModel();
            rows = model.getRowCount();
            columns = model.getColumnCount();
            
            values = model.getTriangle().toArray();
            if(!triangleWidget.isCummulated())
                TriangleUtil.deCummulate(values);
        }
        
        private String toStringTriangle() {
            appendHeader();
            for(int r=0; r<rows; r++) {
                sb.append('\n');
                appendRow(r);
            }
            return sb.toString();
        }
        
        private void appendHeader() {
            for(int c=0; c<columns; c++)
                sb.append('\t').append(columnNames.getName(triangleWidget, c));
        }
        
        private void appendRow(int row) {
            sb.append(rowNames.getName(triangleWidget, row));
            for(int c=0; c<columns; c++) {
                sb.append('\t');
                if(model.hasValueAt(row, c))
                    appendCell(row, c);
            }
        }
        
        private void appendCell(int row, int column) {
            double value = getValueAt(row, column);
            String str = toString(value);
            sb.append(str);
        }
        
        private double getValueAt(int row, int column) {
            int accident = model.getAccidentIndex(row, column);
            int development = model.getDevelopmentIndex(row, column);
            if(accident < 0 || accident >= values.length)
                return Double.NaN;
            double[] rValues = values[accident];
            if(development < 0 || development >= rValues.length)
                return Double.NaN;
            return rValues[development];
        }
        
        private String toString(double value) {
            if(Double.isNaN(value))
                return nan;
            else if(Double.isInfinite(value))
                return inf;
            else
                return (""+value).replace('.', separator);
        }
    
        private void clearState() {
            separator = '.';
            nan = null;
            inf = null;
            model = null;
            rowNames = null;
            columnNames = null;
            rows = 0;
            columns = 0;
            values = null;
            sb.setLength(0);
        }
    }
}
