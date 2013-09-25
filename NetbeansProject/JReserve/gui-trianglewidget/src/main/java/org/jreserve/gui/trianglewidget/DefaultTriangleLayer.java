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

import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultTriangleLayer implements TriangleLayer {

    private Triangle triangle;
    private String displayName;
    private Icon icon;
    private DefaultTriangleWidgetRenderer renderer;
    
    public DefaultTriangleLayer(Triangle triangle) {
        this(triangle, triangle.toString());
    }
    
    public DefaultTriangleLayer(Triangle triangle, String displayName) {
        this(triangle, displayName, EmptyIcon.EMPTY_16);
    }
    
    public DefaultTriangleLayer(Triangle triangle, String displayName, Icon icon) {
        this(triangle, displayName, icon, new DefaultTriangleWidgetRenderer());
    }

    public DefaultTriangleLayer(Triangle triangle, String displayName, Icon icon, DefaultTriangleWidgetRenderer renderer) {
        this.triangle = triangle;
        this.displayName = displayName;
        this.icon = icon;
        this.renderer = renderer;
    }
    
    @Override
    public Triangle getTriangle() {
        return triangle;
    }

    @Override
    public boolean rendersCell(int accident, int development) {
        return accident >= 0 && 
               accident < triangle.getAccidentCount() &&
               development >= 0 &&
               development < triangle.getDevelopmentCount(accident);
    }

    @Override
    public TriangleWidgetRenderer getCellRenderer() {
        return renderer;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
