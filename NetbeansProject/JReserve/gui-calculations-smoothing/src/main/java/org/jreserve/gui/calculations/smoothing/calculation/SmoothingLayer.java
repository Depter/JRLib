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
package org.jreserve.gui.calculations.smoothing.calculation;

import java.awt.Color;
import java.awt.Component;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.triangle.SmoothedTriangle;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SmoothingLayer extends DefaultTriangleLayer {
    
    private final static Color BACKGROUND = new Color(153, 204, 255);
    private final static Color APPLIED_BACKGROUND = new Color(102, 153, 255);
    
    private SmoothingCell[] cells;
    
    SmoothingLayer(Triangle triangle, String name) {
        super(triangle, name, AbstractSmoothingModifier.ICON);
        super.setCellRenderer(new Renderer());
        if(triangle instanceof SmoothedTriangle) {
            cells = ((SmoothedTriangle)triangle).getSmoothing().getSmoothingCells();
        } else {
            cells = new SmoothingCell[0];
        }
    }
    
    @Override
    public boolean rendersCell(int accident, int development) {
        for(SmoothingCell cell : cells)
            if(cell.equals(accident, development))
                return true;
        return false;
    }
    
    private class Renderer extends DefaultTriangleWidgetRenderer {

        @Override
        public Component getComponent(TriangleWidget widget, double value, int accident, int development, boolean selected) {
            super.getComponent(widget, value, accident, development, selected);
            if(!selected)
                setBackground(isApplied(accident, development)? APPLIED_BACKGROUND : BACKGROUND);
            return this;
        }
        
        private boolean isApplied(int accident, int development) {
            for(SmoothingCell cell : cells)
                if(cell.equals(accident, development))
                    return cell.isApplied();
            return false;
        }
    }
}
