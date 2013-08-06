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
 * WidgetHeaderRenderer is responsible to render the headers (eitehr
 * column or row) of a {@link TriangleWidget TriangleWidget}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetHeaderRenderer {
    
    /**
     * Returns the rendered header for the given column or row.
     * 
     * @param widget the widget, needs to be rendered.
     * @param title the title for the column or row.
     * @param index the index of the rendered column or row.
     * @param selected wether the give column or row is selected.
     */
    public Component getComponent(TriangleWidget widget, Object title, int index, boolean selected);
}
