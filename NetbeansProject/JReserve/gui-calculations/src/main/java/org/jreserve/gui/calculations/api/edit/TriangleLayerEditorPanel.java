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
package org.jreserve.gui.calculations.api.edit;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.modification.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import org.jreserve.gui.calculations.api.modification.EditableCalculationModifier;
import org.jreserve.gui.calculations.api.modification.ModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.modification.triangle.TriangleModifier;
//import org.jreserve.gui.misc.eventbus.EventBusListener;
//import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.WidgetUtils;
import org.jreserve.gui.trianglewidget.TriangleEditController;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.gui.trianglewidget.TriangleWidgetPanel;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TriangleLayerEditorPanel.Button.Up.ToolTip=Move Up",
    "MSG.TriangleLayerEditorPanel.Button.Down.ToolTip=Move Down",
    "MSG.TriangleLayerEditorPanel.Button.Delete.ToolTip=Delete",
    "MSG.TriangleLayerEditorPanel.Button.Edit.ToolTip=Edit"
})
public abstract class TriangleLayerEditorPanel<T extends Triangle, C extends AbstractModifiableCalculationProvider<T>> extends JPanel {

    private C calculation;
    private DefaultListModel modificationModel = new DefaultListModel();
    private UndoUtil undo;
    
    private TriangleWidgetPanel widgetPanel;
    private JList layerList;
    private JButton upButton;
    private JButton downButton;
    private JButton editButton;
    private JButton deleteButton;
    
    protected TriangleLayerEditorPanel(C calculation) {
        this.calculation = calculation;
        initComponents();
        initCalculation();
    }

    private void initCalculation() {
        if (calculation != null) {
//            EventBusManager.getDefault().subscribe(this);
            widgetPanel.setTriangleGeometry(getGeometry(calculation));
            widgetPanel.setLayers(createLayers(calculation));
            widgetPanel.getTriangleWidget().setEditController(new LayerEditController());
            resetModificationList();
        }
    }

    protected abstract TriangleGeometry getGeometry(C calculation);

    protected abstract List<TriangleLayer> createLayers(C calculation);

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
    
    public TriangleWidgetPanel getWidgetPanel() {
        return widgetPanel;
    }
    
    public void closeComponent() {
        this.calculation = null;
        this.undo = null;
//        EventBusManager.getDefault().unsubscribe(this);
    }
    
    public void setUndoUtil(UndoUtil undoUtil) {
        this.undo = undoUtil;
    }
    
//    @EventBusListener(forceEDT = true)
//    public void calculationChanged(CalculationEvent.ValueChanged evt) {
//        if(calculation == evt.getCalculationProvider()) {
//        }
//    }
    
    public void recalculate() {
        widgetPanel.setTriangleGeometry(getGeometry(calculation));
        widgetPanel.setLayers(createLayers(calculation));
        resetModificationList();
    }
    
    private void initComponents() {
        JSplitPane splitPane = new JSplitPane();
        widgetPanel = new TriangleWidgetPanel();
        JPanel layerPanel = new JPanel();
        JToolBar layerToolBar = new JToolBar();
        JScrollPane layerScroll = new JScrollPane();
        layerList = new JList();

        setLayout(new java.awt.BorderLayout());

        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);

        widgetPanel.getTriangleWidget().addMouseListener(new TrianglePopUpListener());
        splitPane.setLeftComponent(widgetPanel);

        layerPanel.setLayout(new java.awt.BorderLayout());

        layerToolBar.setFloatable(false);
        layerToolBar.setRollover(true);
        
        upButton = createButton(CommonIcons.arrowUp(), Bundle.MSG_TriangleLayerEditorPanel_Button_Up_ToolTip());
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed();
            }
        });
        layerToolBar.add(upButton);

        downButton = createButton(CommonIcons.arrowDown(), Bundle.MSG_TriangleLayerEditorPanel_Button_Down_ToolTip());
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed();
            }
        });
        layerToolBar.add(downButton);

        editButton = createButton(CommonIcons.edit(), Bundle.MSG_TriangleLayerEditorPanel_Button_Edit_ToolTip());
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed();
            }
        });
        layerToolBar.add(editButton);

        deleteButton = createButton(CommonIcons.delete(), Bundle.MSG_TriangleLayerEditorPanel_Button_Delete_ToolTip());
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed();
            }
        });
        layerToolBar.add(deleteButton);

        layerPanel.add(layerToolBar, java.awt.BorderLayout.PAGE_START);

        layerList.setModel(modificationModel);
        layerList.setCellRenderer(new ModificationRenderer());
        layerList.addListSelectionListener(new ListListener());
        layerList.addMouseListener(new ListClickListener());
        layerScroll.setViewportView(layerList);

        layerPanel.add(layerScroll, java.awt.BorderLayout.CENTER);

        splitPane.setRightComponent(layerPanel);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }      
    
    private JButton createButton(Icon icon, String toolTip) {
        JButton button = new JButton(icon);
        button.setToolTipText(toolTip);
        button.setEnabled(false);
        button.setFocusable(false);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        return button;
    }
    
    private void deleteButtonActionPerformed() {                                             
        for(Object o : layerList.getSelectedValues()) {
            CalculationModifier m = (CalculationModifier) o;
            int index = calculation.indexOfModification(m);
            if(index >= 0)
                calculation.deleteModification(index);
        }
    }                                            

    private void upButtonActionPerformed() {                                         
        ReplaceModification.moveUp(calculation, layerList);
    }                                        

    private void downButtonActionPerformed() {                                           
        ReplaceModification.moveDown(calculation, layerList);
    }                                          

    private void editButtonActionPerformed() {                                           
        editLayer();
    }                                          
    
    private void editLayer() {
        TriangleModifier<T> cm = (TriangleModifier<T>) layerList.getSelectedValue();
        if(cm instanceof EditableCalculationModifier) {
            EditableCalculationModifier<T> ecm = (EditableCalculationModifier<T>) cm;
            ecm.edit(calculation);
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
                TriangleModifier modifier = (TriangleModifier) layerList.getModel().getElementAt(i);
                highLight(model, modifier);
            }
            model.setValueAdjusting(false);
        }
        
        private void highLight(TriangleSelectionModel model, TriangleModifier<T> modifier) {
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
    
    private class LayerEditController implements TriangleEditController {

        @Override
        public boolean allowsEdit(TriangleWidget widget, int accident, int development) {
            return canEdit(widget, accident, development);
        }

        @Override
        public void processEdit(TriangleWidget widget, int accident, int development, double value) {
            value = getValue(widget, accident, development, value);

            TriangleModifier<T> m = createEdit(accident, development, value);
            int index = widgetPanel.getSelectedLayerIndex();
            if (index < 0 || widgetPanel.getLayers().size() == (index + 1)) {
                undo.addModification(m);
            } else {
                undo.addModification(index, m);
            }
        }

        private double getValue(TriangleWidget widget, int accident, int development, double value) {
            if (widget.isCummulated() || development == 0) {
                return value;
            }

            Triangle t = widget.getModel().getTriangle();
            double original = t.getValue(accident, development);
            double prev = t.getValue(accident, development - 1);
            double dif = original - prev;
            double correction = dif - value;

            return original - correction;
        }
    }

    protected boolean canEdit(TriangleWidget widget, int accident, int development) {
        if(widget.isCummulated() || development == 0)
            return true;
        Triangle t = widget.getModel().getTriangle();
        return !Double.isNaN(t.getValue(accident, development - 1));
    }

    protected abstract TriangleModifier<T> createEdit(int accident, int development, double value);

    private class TrianglePopUpListener extends MouseAdapter {
        
        @Override
        public void mousePressed(MouseEvent evt) {
            if(evt.isPopupTrigger())
                showPopUp(evt);
        }
        
        private void showPopUp(MouseEvent evt) {
            String popUpPath = getPopUpPath();
            if(popUpPath == null)
                return;
            
            Point p = evt.getPoint();
            
            if(widgetPanel.getTriangleWidget().getCellAt(p) == null)
                return;
            
            JPopupMenu popup = WidgetUtils.createPopupMenu(popUpPath);
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
    
    protected abstract String getPopUpPath();

    @Messages({
        "# {0} - from",
        "# {1} - to",
        "MSG.ReplaceModification.PH.Title=Replaceing modification {0} -> {1}"
    })
    private static class ReplaceModification implements Callable<Void> {
        
        static void moveUp(ModifiableCalculationProvider calc, JList list) {
            int from = list.getMinSelectionIndex()-1;
            int to = list.getSelectedIndices().length + from;
            
            ReplaceModification task = new ReplaceModification(calc, from, to);
            String title = Bundle.MSG_ReplaceModification_PH_Title(from+1, to+1);
            TaskUtil.execute(task, null, title);
        }
        
        static void moveDown(ModifiableCalculationProvider calc, JList list) {
            int from = list.getMaxSelectionIndex()+1;
            int to = from - list.getSelectedIndices().length;
            
            ReplaceModification task = new ReplaceModification(calc, from, to);
            String title = Bundle.MSG_ReplaceModification_PH_Title(from+1, to+1);
            TaskUtil.execute(task, null, title);
        }
        
        private final ModifiableCalculationProvider calc;
        private final int fromIndex;
        private final int toIndex;
        
        private ReplaceModification(ModifiableCalculationProvider calc, int fromIndex, int toIndex) {
            this.calc = calc;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }
        
        @Override
        public Void call() throws Exception {
            synchronized(calc) {
                CalculationModifier modifier = calc.getModificationAt(fromIndex);
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
