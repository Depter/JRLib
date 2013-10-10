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

import org.jreserve.gui.trianglewidget.editing.TriangleEditor;
import org.jreserve.gui.trianglewidget.editing.TriangleEditorListener;
import org.jreserve.gui.trianglewidget.editing.AbstractTriangleEditor;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class WidgetContentPanel extends JComponent {
    
    private TriangleWidget widget;
    private TriangleWidgetRenderer renderer;
    private Rectangle cellBounds = new Rectangle();
    private boolean isCummulated = true;
    
    //Editing
    private TriangleEditor editor = new AbstractTriangleEditor();
    private Component editorComp;
    private TriangleEditorListener editorListener = new EditorListener();
    private EditorRemover editorRemover;
    private int editingRow = -1;
    private int editingColumn = -1;
    
    //Init before painting
    private TriangleModel model;
    private TriangleSelectionModel selectionModel;
    private int rowCount;
    private int columnCount;
    
    WidgetContentPanel(TriangleWidget widget, TriangleWidgetRenderer renderer) {
        this.widget = widget;
        this.renderer = renderer;
        this.addMouseListener(new MouseSelectionListener());
        calculateSizes();
    }
    
    void setCummualted(boolean isCummualted) {
        this.isCummulated = isCummualted;
    }
    
    boolean isCummualted() {
        return isCummulated;
    }
    
    private void calculateSizes() {
        int rCount = widget.getRowCount();
        int cCount = widget.getColumnCount();
        TriangleModel tm = widget.getModel();
        
        int wPref = 0;
        int hPref = 0;
        int wMin = 0;
        int hMin = 0;
        for(int r=0; r<rCount; r++) {
            for(int c=0; c<cCount; c++) {
                if(tm.hasValueAt(r, c)) {
                    int accident = tm.getAccidentIndex(r, c);
                    int development = tm.getDevelopmentIndex(r, c);
                    
                    Component comp = renderer.getComponent(
                            widget, tm.getValueAt(r, c), 
                            accident, development, false);
                    Dimension size = comp.getPreferredSize();
                    wPref = Math.max(wPref, size.width);
                    hPref = Math.max(hPref, size.height);
                    
                    size = comp.getMinimumSize();
                    wMin = Math.max(wMin, size.width);
                    hMin = Math.max(hMin, size.height);
                }
            }
        }
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        Dimension preferred = new Dimension(cCount * wPref, rCount * hPref);
        setMinimumSize(preferred);
        setPreferredSize(preferred);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        initPainting();
        for(int r=0; r<rowCount; r++)
            for(int c=0; c<columnCount; c++)
                paintCell(r, c, g);
    }
    
    private void initPainting() {
        model = widget.getModel();
        selectionModel = widget.getTriangleSelectionModel();
        rowCount = model.getRowCount();
        columnCount = model.getColumnCount();

        if(rowCount > 0 && columnCount > 0) {
            cellBounds.height = getHeight() / rowCount;
            cellBounds.width = getWidth() / columnCount;
        }
    }
    
    private void paintCell(int row, int column, Graphics g) {
        if(!model.hasValueAt(row, column))
            return;
        
        double value = getScaledValue(row, column);
        int accident = model.getAccidentIndex(row, column);
        int development = model.getDevelopmentIndex(row, column);
        boolean selected = selectionModel.isSelected(accident, development);
        
        Component c = renderer.getComponent(widget, value, accident, development, selected);
        initCellBounds(row, column);
        paintCell(c, g);
    }
    
    private double getScaledValue(int row, int column) {
        return isCummulated? model.getValueAt(row, column) : getDecummualtedValue(row, column);
    }
    
    private double getDecummualtedValue(int row, int column) {
        Triangle triangle = model.getTriangle();
        if(triangle == null)
            return Double.NaN;
        int development = model.getDevelopmentIndex(row, column);
        int accident = model.getAccidentIndex(row, column);
        double value = triangle.getValue(accident, development);
        return development<=0?
                value :
                value - triangle.getValue(accident, development-1);
    }
    
    private void initCellBounds(int row, int column) {
        cellBounds.x = column * cellBounds.width;
        cellBounds.y = row * cellBounds.height;
    }
    
    private void paintCell(Component c, Graphics g) {
        Graphics cg = g.create(cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);
	try {
            c.setBounds(cellBounds);
            c.validate();
	    c.paint(cg);
	}
	finally {
	    cg.dispose();
	}

	c.setBounds(-cellBounds.width, -cellBounds.height, 0, 0);    
    }
    
    boolean editCellAt(int row, int column) {
        if(!widget.isEditable() || !model.hasValueAt(row, column))
            return true;
        
        if (editor != null && !editor.stopEditing())
            return false;

        if(!model.hasValueAt(row, column))
            return false;

        if (editorRemover == null) {
            KeyboardFocusManager fm =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
            editorRemover = new EditorRemover(fm);
            fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);
        }

        int accident = model.getAccidentIndex(row, column);
        int development = model.getDevelopmentIndex(row, column);
        TriangleEditController editController = widget.getEditController();
        if (editor != null && editController != null && editController.allowsEdit(widget, accident, development)) {
	    editorComp = prepareEditor(row, column);
	    if (editorComp == null) {
		removeEditor();
		return false;
	    }
            
            initCellBounds(row, column);
            editorComp.setBounds(cellBounds);
	    add(editorComp);
	    editorComp.validate();
            editorComp.repaint();

	    editor.addTriangleEditorListener(editorListener);
            editingRow = row;
            editingColumn = column;
            
	    return true;
        }
        return false;
    }
    
    private Component prepareEditor(int row, int column) {
        double value = model.getValueAt(row, column);
        int accident = model.getAccidentIndex(row, column);
        int development = model.getDevelopmentIndex(row, column);
        boolean isSelected = selectionModel.isSelected(accident, development);
        Component comp = editor.getEditorComponent(widget, value, accident, development, isSelected);
        
        if (comp instanceof JComponent) {
	    JComponent jComp = (JComponent)comp;
	    if (jComp.getNextFocusableComponent() == null) {
		jComp.setNextFocusableComponent(this);
	    }
	}
	return comp;
    }
    
    private void removeEditor() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
            removePropertyChangeListener("permanentFocusOwner", editorRemover);
	editorRemover = null;

        if(editor != null) {
            editor.removeTriangleEditorListener(editorListener);
            if (editorComp != null) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                boolean isFocusOwnerInTheTable = focusOwner != null?   
                        SwingUtilities.isDescendingFrom(focusOwner, this):false;                    
                remove(editorComp);
                if(isFocusOwnerInTheTable)
                    requestFocusInWindow();
	    }
            
            initCellBounds(editingRow, editingColumn);
            editingRow = -1;
            editingColumn = -1;
            editorComp = null;

            repaint(cellBounds);
        }
    }
    
    int getRowIndex(Point point) {
        if(contains(point))
            return point.y / cellBounds.height;
        return -1;
    }
    
    int getColumnIndex(Point point) {
        if(contains(point))
            return point.x / cellBounds.width;
        return -1;
    }
    
    boolean hasValueAt(Point point) {
        if(contains(point)) {
            int r = point.y / cellBounds.height;
            int c = point.x / cellBounds.width;
            return widget.getModel().hasValueAt(r, c);
        } else {
            return false;
        }
    }
    
    Cell getCellAt(Point point) {
        if(!contains(point))
            return null;
        
        int r = point.y / cellBounds.height;
        int c = point.x / cellBounds.width;
            
        TriangleModel tm = widget.getModel();
        if(!tm.hasValueAt(r, c))
            return null;
        
        int accident = tm.getAccidentIndex(r, c);
        int development = tm.getDevelopmentIndex(r, c);
        return new Cell(accident, development);
    }
    
    private class EditorListener implements TriangleEditorListener {

        @Override
        public void editingStopped(ChangeEvent evt) {
            if(editor != null) {
                Double value = editor.getEditorValue();
                int accident = widget.getModel().getAccidentIndex(editingRow, editingColumn);
                int development = widget.getModel().getDevelopmentIndex(editingRow, editingColumn);
                removeEditor();
                
                TriangleEditController editController = widget.getEditController();
                if(value != null && editController != null)
                    editController.processEdit(widget, accident, development, value);
            }
        }

        @Override
        public void editingCancelled(ChangeEvent evt) {
            removeEditor();
        }
    }
    
    private class MouseSelectionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                int[] cell = getCell(e);
                if(cell != null)
                    editCellAt(cell[0], cell[1]);
            }
        }
        
        private int[] getCell(MouseEvent evt) {
            if(cellBounds.width == 0 || cellBounds.height == 0)
                return null;
            
            Point p = evt.getPoint();
            int r = getRowIndex(p);
            int c = getColumnIndex(p);
            
            if(!widget.getModel().hasValueAt(r, c))
                return null;
            
            return new int[]{r, c};
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger() || e.getButton() != MouseEvent.BUTTON1)
                return;
            
            int[] cell = getCell(e);
            if(cell == null)
                return;
            
            if(editingRow >= 0 && editingColumn >= 0) {
                if(editingRow == cell[0] && editingColumn == cell[1])
                    return;
                else
                    editor.cancelEditing();
            }
            
            int a = model.getAccidentIndex(cell[0], cell[1]);
            int d = model.getDevelopmentIndex(cell[0], cell[1]);

            if(e.isControlDown()) {
                switchSelection(a, d);
            } else {
                setSelection(a, d);
            }
        }
        
        private void switchSelection(int accident, int development) {
            TriangleSelectionModel selectionModel = widget.getTriangleSelectionModel();
            if(selectionModel.isSelected(accident, development))
                selectionModel.removeSelection(accident, development);
            else
                selectionModel.setSelected(accident, development);
        }
        
        private void setSelection(int accident, int development) {
            TriangleSelectionModel selectionModel = widget.getTriangleSelectionModel();
            selectionModel.setValueAdjusting(true);
            selectionModel.clearSelection();
            selectionModel.setSelected(accident, development);
            selectionModel.setValueAdjusting(false);
        }
    }
    
    private class EditorRemover implements PropertyChangeListener {
        private KeyboardFocusManager focusManager;

        private EditorRemover(KeyboardFocusManager fm) {
            this.focusManager = fm;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent ev) {
            if (editingRow < 0 || editingColumn < 0 || 
                getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE)
                return;

            Component c = focusManager.getPermanentFocusOwner();
            while (c != null) {
                if (c == widget) {
                    // focus remains inside the table
                    return;
                } else if ((c instanceof Window) ||
                           (c instanceof Applet && c.getParent() == null)) {
                    if (c == SwingUtilities.getRoot(widget)) {
                        if (!editor.stopEditing()) {
                            editor.cancelEditing();
                        }
                    }
                    break;
                }
                c = c.getParent();
            }
        }
    }
    
}
