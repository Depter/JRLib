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
package org.jreserve.gui.excel.template.dataimport.createwizard;

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
    "LBL.SourceType.Table=Table",
    "LBL.SourceType.Triangle=Triangle"
})
public enum SourceType implements Displayable {

    TABLE(
        Bundle.LBL_SourceType_Table(),
        "org/jreserve/gui/excel/source_table.png"), //NOI18
    TRIANGLE(
        Bundle.LBL_SourceType_Triangle(),
        "org/jreserve/gui/excel/source_triangle.png");  //NOI18
    
    private final String displayName;
    private final Icon icon;

    private SourceType(String displayName, String iconBase) {
        this.displayName = displayName;
        this.icon = ImageUtilities.loadImageIcon(iconBase, false);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
}
