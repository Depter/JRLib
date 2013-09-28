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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionModel;
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
                    Component comp = renderer.getComponent(widget, tm.getValueAt(r, c), r, c, false);
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
        
        boolean selected = isSelected(row, column);
        Component c = renderer.getComponent(widget, value, row, column, selected);
        initCellBounds(row, column);
        paintCell(c, g);
    }
    
    private boolean isSelected(int row, int column) {
        int accident = model.getAccidentIndex(row, column);
        int development = model.getDevelopmentIndex(row, column);
        return selectionModel.isSelected(accident, development);
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
    
    
    private class MouseSelectionListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if(cellBounds.width == 0 || cellBounds.height == 0)
                return;
            
            Point p = e.getPoint();
            int r = p.y / cellBounds.height;
            int c = p.x / cellBounds.width;
            
            if(!widget.getModel().hasValueAt(r, c))
                return;
            
            int a = model.getAccidentIndex(r, c);
            int d = model.getDevelopmentIndex(r, c);
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
}
