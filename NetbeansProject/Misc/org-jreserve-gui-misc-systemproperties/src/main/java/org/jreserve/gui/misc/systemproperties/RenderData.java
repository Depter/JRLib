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
package org.jreserve.gui.misc.systemproperties;

import java.awt.Color;
import javax.swing.Icon;
import org.netbeans.swing.outline.RenderDataProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RenderData implements RenderDataProvider {

    @Override
    public String getDisplayName(Object o) {
        return ((PropertyItem)o).getName();
    }

    @Override
    public boolean isHtmlDisplayName(Object o) {
        return false;
    }

    @Override
    public String getTooltipText(Object o) {
        return ((PropertyItem)o).getRoot();
    }

    @Override
    public Color getBackground(Object o) {
        return null;
    }

    @Override
    public Color getForeground(Object o) {
        return null;
    }

    @Override
    public Icon getIcon(Object o) {
        return null;
    }
}
