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
package org.jreserve.gui.data.actions;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCategoryTreeRenderer extends DefaultTreeCellRenderer {
        
    private final static Icon ICON = ImageUtilities.loadImageIcon("org/jreserve/gui/data/icons/folder_db.png", false);
    private final static Icon TRIANGLE = ImageUtilities.loadImageIcon("org/jreserve/gui/data/icons/database_triangle.png", false);
    private final static Icon VECTOR = ImageUtilities.loadImageIcon("org/jreserve/gui/data/icons/database_vector.png", false);

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if(value instanceof DataCategory) {
            setText(((DataCategory)value).getName());
            setIcon(ICON);
        } else if(value instanceof DataSource) {
            DataSource ds = (DataSource) value;
            setText(ds.getName());
            setIcon(ds.getDataType()== DataType.TRIANGLE? TRIANGLE : VECTOR);
        }
        return this;
    }
}
