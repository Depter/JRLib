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
package org.jreserve.jrlib.gui.data;

import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataType.Triangle=Triangle",
    "LBL.DataType.Vector=Vector"
})
public enum DataType implements Displayable {

    TRIANGLE(Bundle.LBL_DataType_Triangle(), "org/jreserve/jrlib/gui/data/triangle.png"),   //NOI18
    VECTOR(Bundle.LBL_DataType_Vector(), "org/jreserve/jrlib/gui/data/vector.png");   //NOI18
    
    private final String displayName;
    private final Icon icon;
    
    private DataType(String displayName, String iconBase) {
        this.displayName = displayName;
        this.icon = ImageUtilities.loadImageIcon(iconBase, false);
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Icon getIcon() {
        return icon;
    }
}
