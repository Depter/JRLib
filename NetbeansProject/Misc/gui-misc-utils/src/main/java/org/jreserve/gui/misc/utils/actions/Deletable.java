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
package org.jreserve.gui.misc.utils.actions;

import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public interface Deletable extends Displayable {
    
    public void delete() throws Exception;
    
    public static abstract class NodeDeletable implements Deletable {
        
        protected final Node node;

        protected NodeDeletable(Node node) {
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
            Node parent = node;
            String path = "";
            do {
                path = "/" + parent.getDisplayName() + path;
                parent = parent.getParentNode();
            } while (parent != null);
            
            return path.length()==0? path : path.substring(1);
        }
    }
}
