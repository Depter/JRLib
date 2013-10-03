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
package org.jreserve.gui.misc.utils.actions.deletable;

import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class NodeDeletable implements Deletable {
        
    protected final org.openide.nodes.Node node;

    protected NodeDeletable(org.openide.nodes.Node node) {
        this.node = node;
    }
        
    @Override
    public Icon getIcon() {
        Image img = node.getIcon(BeanInfo.ICON_COLOR_16x16);
        if(img == null)
            return EmptyIcon.EMPTY_16;
        return ImageUtilities.image2Icon(img);
    }

    @Override
    public String getDisplayName() {
        return Displayable.Utils.displayPath(node);
    }    
}
