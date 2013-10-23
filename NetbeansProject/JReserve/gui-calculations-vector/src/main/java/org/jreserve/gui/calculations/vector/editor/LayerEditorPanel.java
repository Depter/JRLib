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
package org.jreserve.gui.calculations.vector.editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.Callable;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.EditableCalculationModifier;
import org.jreserve.gui.calculations.api.edit.UndoUtil;
import org.jreserve.gui.calculations.vector.VectorModifier;
import org.jreserve.gui.calculations.vector.impl.VectorCalculationImpl;
import org.jreserve.gui.calculations.vector.modifications.VectorCorrectionModifier;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.trianglewidget.TriangleEditController;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.gui.trianglewidget.TriangleWidgetPanel;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.vector.Vector;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionReferences({
    @ActionReference(
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.calculations.actions.ExcludeAction"),
        path = LayerEditorPanel.POPUP_PATH,
        position = 100
    ),
    @ActionReference(
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.calculations.actions.SmoothAction"),
        path = LayerEditorPanel.POPUP_PATH,
        position = 200
    )
})
class LayerEditorPanel extends javax.swing.JPanel {

    final static String POPUP_PATH = "JReserve/ClaimTriangle/LayerEditor/Popup";
    
    private VectorCalculationImpl calculation;
    private DefaultListModel modificationModel = new DefaultListModel();
    private UndoUtil undo;
    
    LayerEditorPanel(VectorCalculationImpl calculation) {
        this.calculation = calculation;
        initComponents();
        initCalculation();
    }
    
    private void initCalculation() {
        if(calculation != null) {
            widgetPanel.setTriangleGeometry(calculation.getGeometry());
            widgetPanel.setLayers(calculation.createLayers());
            widgetPanel.getTriangleWidget().setEditController(new LayerEditController());
            resetModificationList();
            EventBusManager.getDefault().subscribe(this);
        }
    }
    
    private void resetModificationList() {
        Object[] selected = layerList.getSelectedValues();
        reloadModificationModel();
        reselectModifications(selected);
    }
    
    private void reloadModificationModel() {
        modificationModel.removeAllElements();
        int count = calculation.getModificationCount();
        for(int i=0; i<count; i++)
            modificationModel.addElement(calculation.getModificationAt(i));
    }
    
    private void reselectModifications(Object[] selected) {
        ListSelectionModel sModel = layerList.getSelectionModel();
        sModel.setValueIsAdjusting(true);
        for(Object o : selected) {
            int index = modificationModel.indexOf(o);
            if(index >= 0)
                sModel.addSelectionInterval(index, index);
        }
        sModel.setValueIsAdjusting(false);
    }
    
    TriangleWidgetPanel getWidgetPanel() {
        return widgetPanel;
    }
    
    void componentClosed() {
        EventBusManager.getDefault().unsubscribe(this);
    }
    
    void setUndoUtil(UndoUtil undoUtil) {
        this.undo = undoUtil;
    }
    
    @EventBusListener(forceEDT = true)
    public void calculationChanged(CalculationEvent.ValueChanged evt) {
        if(calculation == evt.getCalculationProvider()) {
            widgetPanel.setTriangleGeometry(calculation.getGeometry());
            widgetPanel.setLayers(calculation.createLayers());
            resetModificationList();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        widgetPanel = new org.jreserve.gui.trianglewidget.TriangleWidgetPanel();
        layerPanel = new javax.swing.JPanel();
        layerToolBar = new javax.swing.JToolBar();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        layerScroll = new javax.swing.JScrollPane();
        layerList = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);

        widgetPanel.getTriangleWidget().addMouseListener(new TrianglePopUpListener());
        splitPane.setLeftComponent(widgetPanel);

        layerPanel.setLayout(new java.awt.BorderLayout());

        layerToolBar.setFloatable(false);
        layerToolBar.setRollover(true);

        upButton.setIcon(CommonIcons.arrowUp());
        org.openide.awt.Mnemonics.setLocalizedText(upButton, null);
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayerEditorPanel.class, "LayerEditorPanel.upButton.toolTipText")); // NOI18N
        upButton.setEnabled(false);
        upButton.setFocusable(false);
        upButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        upButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        layerToolBar.add(upButton);

        downButton.setIcon(CommonIcons.arrowDown());
        org.openide.awt.Mnemonics.setLocalizedText(downButton, null);
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayerEditorPanel.class, "LayerEditorPanel.downButton.toolTipText")); // NOI18N
        downButton.setEnabled(false);
        downButton.setFocusable(false);
        downButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        downButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        layerToolBar.add(downButton);

        deleteButton.setIcon(CommonIcons.delete());
        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, null);
        deleteButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayerEditorPanel.class, "LayerEditorPanel.deleteButton.toolTipText")); // NOI18N
        deleteButton.setEnabled(false);
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        layerToolBar.add(deleteButton);

        editButton.setIcon(CommonIcons.edit());
        org.openide.awt.Mnemonics.setLocalizedText(editButton, null);
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(LayerEditorPanel.class, "LayerEditorPanel.editButton.toolTipText")); // NOI18N
        editButton.setEnabled(false);
        editButton.setFocusable(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        layerToolBar.add(editButton);

        layerPanel.add(layerToolBar, java.awt.BorderLayout.PAGE_START);

        layerList.setModel(modificationModel);
        layerList.setCellRenderer(new ModificationRenderer());
        layerList.addListSelectionListener(new ListListener());
        layerList.addMouseListener(new ListClickListener());
        layerScroll.setViewportView(layerList);

        layerPanel.add(layerScroll, java.awt.BorderLayout.CENTER);

        splitPane.setRightComponent(layerPanel);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        for(Object o : layerList.getSelectedValues()) {
            CalculationModifier m = (CalculationModifier) o;
            int index = calculation.indexOfModification(m);
            if(index >= 0)
                calculation.deleteModification(index);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        ReplaceModification.moveUp(calculation, layerList);
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        ReplaceModification.moveDown(calculation, layerList);
    }//GEN-LAST:event_downButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        editLayer();
    }//GEN-LAST:event_editButtonActionPerformed
    
    private void editLayer() {
        CalculationModifier<Vector> cm = (CalculationModifier<Vector>) layerList.getSelectedValue();
        if(cm instanceof EditableCalculationModifier) {
            EditableCalculationModifier<Vector> ecm = (EditableCalculationModifier<Vector>) cm;
            ecm.edit(calculation);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton editButton;
    private javax.swing.JList layerList;
    private javax.swing.JPanel layerPanel;
    private javax.swing.JScrollPane layerScroll;
    private javax.swing.JToolBar layerToolBar;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JButton upButton;
    private org.jreserve.gui.trianglewidget.TriangleWidgetPanel widgetPanel;
    // End of variables declaration//GEN-END:variables

    private class TrianglePopUpListener extends MouseAdapter {
        
        @Override
        public void mousePressed(MouseEvent evt) {
            if(evt.isPopupTrigger())
                showPopUp(evt);
        }
        
        private void showPopUp(MouseEvent evt) {
            Point p = evt.getPoint();
            
            if(widgetPanel.getTriangleWidget().getCellAt(p) == null)
                return;
            
            JPopupMenu popup = WidgetUtils.createPopupMenu(POPUP_PATH);
            if(popup == null)
                return;
            
            popup.show(widgetPanel.getTriangleWidget(), p.x, p.y);
        }
        
        @Override
        public void mouseReleased(MouseEvent evt) {
            if(evt.isPopupTrigger())
                showPopUp(evt);
        }
    }
    
    private class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int[] indices = layerList.getSelectedIndices();
            
            editButton.setEnabled(indices.length == 1);
            deleteButton.setEnabled(indices.length > 0);
            
            boolean continuous = isContinuous(indices);
            if(continuous) {
                upButton.setEnabled(layerList.getMinSelectionIndex() > 0);
                downButton.setEnabled(layerList.getMaxSelectionIndex() < layerList.getModel().getSize()-1);
            } else {
                upButton.setEnabled(false);
                downButton.setEnabled(false);
            }
            
            highlightIndices(indices);
        }
        
        private boolean isContinuous(int[] indices) {
            int size = indices.length;
            if(size < 1)
                return false;
            
            Arrays.sort(indices);
            for(int i=1; i<size; i++)
                if(indices[i] - indices[i-1] != 1)
                    return false;
            return true;
        }
        
        private void highlightIndices(int[] indices) {
            TriangleSelectionModel model = widgetPanel.getTriangleWidget().getTriangleSelectionModel();
            model.setValueAdjusting(true);
            model.clearSelection();
            for(int i : indices) {
                VectorModifier modifier = (VectorModifier) layerList.getModel().getElementAt(i);
                highLight(model, modifier);
            }
            model.setValueAdjusting(false);
        }
        
        private void highLight(TriangleSelectionModel model, VectorModifier modifier) {
            for(Cell cell : modifier.getAffectedCells())
                model.setSelected(cell.getAccident(), cell.getDevelopment());
        }
    }
    
    private class ListClickListener extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            if(evt.getClickCount() == 2) {
                editLayer();
            }
        }
    }
    
    private class ModificationRenderer implements ListCellRenderer {
        
        private ListCellRenderer delegate = WidgetUtils.displayableListRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            CalculationModifier cm = null;
            if(value instanceof CalculationModifier) {
                cm = (CalculationModifier) value;
                value = cm.getDisplayable();
            }
            JComponent c = (JComponent) delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(cm != null)
                c.setToolTipText(cm.getDescription());
            
            return c;
        }
    }
    
   private class LayerEditController implements TriangleEditController {

        @Override
        public boolean allowsEdit(TriangleWidget widget, int accident, int development) {
            if(widget.isCummulated() || development == 0)
                return true;
            
            Triangle t = widget.getModel().getTriangle();
            return !Double.isNaN(t.getValue(accident, development-1));
        }

        @Override
        public void processEdit(TriangleWidget widget, int accident, int development, double value) {
            value = getValue(widget, accident, development, value);
            
            VectorCorrectionModifier m = new VectorCorrectionModifier(accident, value);
            int index = widgetPanel.getSelectedLayerIndex();
            if(index < 0 || widgetPanel.getLayers().size() == (index+1)) {
                undo.addModification(m);
            } else {
                undo.addModification(index, m);
            }
        }
        
        private double getValue(TriangleWidget widget, int accident, int development, double value) {
            if(widget.isCummulated() || development == 0)
                return value;
            
            Triangle t = widget.getModel().getTriangle();
            double original = t.getValue(accident, development);
            double prev = t.getValue(accident, development-1);
            double dif = original - prev;
            double correction = dif - value;
            
            return original - correction;
        }
    }

    @Messages({
        "# {0} - from",
        "# {1} - to",
        "MSG.ReplaceModification.PH.Title=Replaceing modification {0} -> {1}"
    })
    private static class ReplaceModification implements Callable<Void> {
        
        static void moveUp(VectorCalculationImpl calc, JList list) {
            int from = list.getMinSelectionIndex()-1;
            int to = list.getSelectedIndices().length + from;
            
            ReplaceModification task = new ReplaceModification(calc, from, to);
            String title = Bundle.MSG_ReplaceModification_PH_Title(from+1, to+1);
            TaskUtil.execute(task, null, title);
        }
        
        static void moveDown(VectorCalculationImpl calc, JList list) {
            int from = list.getMaxSelectionIndex()+1;
            int to = from - list.getSelectedIndices().length;
            
            ReplaceModification task = new ReplaceModification(calc, from, to);
            String title = Bundle.MSG_ReplaceModification_PH_Title(from+1, to+1);
            TaskUtil.execute(task, null, title);
        }
        
        private final VectorCalculationImpl calc;
        private final int fromIndex;
        private final int toIndex;
        
        private ReplaceModification(VectorCalculationImpl calc, int fromIndex, int toIndex) {
            this.calc = calc;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }
        
        @Override
        public Void call() throws Exception {
            synchronized(calc) {
                CalculationModifier<Vector> modifier = calc.getModificationAt(fromIndex);
                calc.deleteModification(fromIndex);
                if(toIndex == calc.getModificationCount())
                    calc.addModification(modifier);
                else
                    calc.addModification(toIndex, modifier);
            }
            return null;
        }
    }
}
