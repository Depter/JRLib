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

package org.jreserve.gui.trianglewidget.model.registration;

import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleModelAdapter implements Displayable {
    
    private final String id;
    private final String iconBase;
    private final String name;
    private final TriangleModel model;
    private Icon icon;

    TriangleModelAdapter(String id, String name, String iconBase, TriangleModel model) {
        this.id = id;
        this.name = name;
        this.iconBase = (iconBase==null || iconBase.length()==0)? null : iconBase;
        this.model = model;
    }

    @Override
    public Icon getIcon() {
        if(icon == null)
            icon = iconBase==null? EmptyIcon.EMPTY_16 : ImageUtilities.loadImageIcon(iconBase, false);
        return icon;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
    
    public TriangleModel getTriangleModel() {
        return model;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof TriangleModelAdapter) &&
                id.equals(((TriangleModelAdapter)o).id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("TriangleModelAdapter [%s]", id);
    }
}
