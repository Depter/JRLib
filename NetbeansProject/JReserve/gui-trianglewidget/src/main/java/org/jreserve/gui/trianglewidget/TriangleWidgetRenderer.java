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

/**
 * TriangleWidgetRenderer renders the values within a layer of
 * a {@link TriangleWidget TriangleWidget}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleWidgetRenderer {
    
    /**
     * Returns the component representing the given cell.
     * 
     * @param widget the widget being rendered.
     * @param value the value, to render.
     * @param row the row index of the value.
     * @param column the column index of the value.
     * @param selected *true* if the cell is selected.
     */
    public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected);
}
