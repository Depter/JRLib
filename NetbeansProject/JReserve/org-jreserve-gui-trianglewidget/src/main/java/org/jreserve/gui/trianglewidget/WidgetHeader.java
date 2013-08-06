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
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.jreserve.gui.trianglewidget.model.TitleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class WidgetHeader extends JComponent {

    final static int X_AXIS = 0;
    final static int Y_AXIS = 1;
    private final static int PREFFERED_CELL_WIDTH = 50;
    private final static int PREFFERED_CELL_HEIGHT = 20;
    private final static int MIN_CELL_WIDTH = 50;
    private final static int MIN_CELL_HEIGHT = 20;
    
    private final static Dimension MIN_SIZE = new Dimension(0, 0);
    private final static Dimension MAX_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    
    private TriangleWidget widget;
    private int axis;
    private WidgetHeaderRenderer renderer;
    
    public WidgetHeader(TriangleWidget widget, int axis) {
        this.renderer = new DefaultWidgetHeaderRenderer();
        this.widget = widget;
        this.axis = axis;
        calculateSizes();
    }

    private void calculateSizes() {
        TitleModel titleModel;
        int cellCount;
        if(X_AXIS == axis) {
            cellCount = widget.getColumnCount();
            titleModel = widget.getHorizontalTitleModel();
        } else {
            cellCount = widget.getRowCount();
            titleModel = widget.getVerticalTitleModel();
        }
        
        int pSize = 0;
        for(int i=0; i<cellCount; i++)
            pSize = Math.max(pSize, getSize(renderer.getComponent(widget, titleModel.getName(i), i, false)));
        
        //Dimension wSize = widget.getContentPanel().getPreferredSize();
        Dimension prefSize;
        Dimension minSize;
        if (X_AXIS == axis) {
            prefSize = new Dimension(cellCount*PREFFERED_CELL_WIDTH, pSize);
            minSize = new Dimension(cellCount*MIN_CELL_WIDTH, pSize);
        } else {
            prefSize = new Dimension(pSize, cellCount*PREFFERED_CELL_HEIGHT);
            minSize = new Dimension(pSize, cellCount*MIN_CELL_HEIGHT);
        }
        
        super.setPreferredSize(prefSize);
        super.setMinimumSize(minSize);
    }
    
    private int getSize(Component c) {
        return (X_AXIS == axis)?
                c.getPreferredSize().height :
                c.getPreferredSize().width;
    }
    
    void resize() {
        calculateSizes();
        revalidate();
    }

//    @Override
//    public void setMaximumSize(Dimension maximumSize) {
//    }
//
//    @Override
//    public Dimension getMaximumSize() {
//        return MAX_SIZE;
//    }
//
//    @Override
//    public void setMinimumSize(Dimension minimumSize) {
//    }
//
//    @Override
//    public Dimension getMinimumSize() {
//        return MIN_SIZE;
//    }
    
    private TitleModel titles;
    private int count;
    private int size;
    private Rectangle cellBounds = new Rectangle();
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(widget.getColumnCount() == 0 || widget.getRowCount()==0)
            return;
        
        initPaint();
        for(int i=0; i<count; i++) {
            Component c = renderer.getComponent(widget, titles.getName(i), i, false);
            initCellBounds(i);
            paintCell(c, g);
        }
    }
    
    private void initPaint() {
        Component content = widget.getContentPanel();
        if(X_AXIS == axis) {
            titles = widget.getHorizontalTitleModel();
            count = widget.getColumnCount();
            if(count > 0)
                size = content.getWidth() / count;
        } else {
            titles = widget.getVerticalTitleModel();
            count = widget.getRowCount();
            if(count > 0)
                size = content.getHeight() / count;
        }
    }
    
    private void initCellBounds(int index) {
        if(X_AXIS == axis) {
            cellBounds.x = index * size;
            cellBounds.y = 0;
            cellBounds.width = size;
            cellBounds.height = getHeight();
        } else {
            cellBounds.x = 0;
            cellBounds.y = index * size;
            cellBounds.width = getWidth();
            cellBounds.height = size;
        }
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
    
}
