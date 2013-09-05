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
package org.jreserve.gui.misc.utils.widgets;

import javax.swing.Icon;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Displayable {
    
    public Icon getIcon();
    
    public String getDisplayName();
    
    public static class Utils {
        
        public static String displayPath(Node node) {
            StringBuilder path = new StringBuilder();
            while(node != null) {
                if(path.length() > 0)
                    path.insert(0, '/');
                path.insert(0, node.getDisplayName());
                
                node = node.getParentNode();
            }
            return path.toString();
        }
        
        private Utils() {}
    }
}
