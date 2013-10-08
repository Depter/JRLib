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
package org.jreserve.gui.calculations.claimtriangle.modifications.smoothing;

import java.awt.Color;
import java.awt.Component;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.DefaultTriangleWidgetRenderer;
import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.SmoothedClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SmoothingLayer extends DefaultTriangleLayer {

    private SmoothingCell[] cells;
    
    SmoothingLayer(Triangle triangle, String name) {
        super(triangle, name, AbstractSmoothingModifier.ICON, 
                new SmoothingRenderer());
        if(triangle instanceof SmoothedClaimTriangle) {
            cells = ((SmoothedClaimTriangle)triangle).getSmoothing().getSmoothingCells();
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
    
    private static class SmoothingRenderer extends DefaultTriangleWidgetRenderer {

        private final static Color BACKGROUND = new Color(157, 222, 255);
        
        @Override
        public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected) {
            super.getComponent(widget, value, row, column, selected);
            if(!selected)
                setBackground(BACKGROUND);
            return this;
        }
        
    }
    
}
