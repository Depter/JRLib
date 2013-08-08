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

package org.jreserve.gui.trianglewidget.model;

import org.jreserve.gui.trianglewidget.TriangleWidget;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometryTitelModel implements TitleModel {

    public static enum Type {
        ACCIDENT {
            @Override
            MonthDate getDate(TriangleGeometry geometry, int index) {
                return geometry.getAccidentDate(index);
            }
        },
        CALENDAR {
            @Override
            MonthDate getDate(TriangleGeometry geometry, int index) {
                return geometry.getDevelopmentDate(0, index);
            }
        };
        
        abstract MonthDate getDate(TriangleGeometry geometry, int index);
    }

    private Type type;
    
    public GeometryTitelModel(Type type) {
        if(type == null)
            throw new NullPointerException("Type is null!");
        this.type = type;
    }
    
    @Override
    public String getName(TriangleWidget widget, int index) {
        TriangleGeometry geometry = widget.getTriangleGeometry();
        if(geometry == null)
            return ""+(index+1);
        return type.getDate(geometry, index).toString();
    }
}
