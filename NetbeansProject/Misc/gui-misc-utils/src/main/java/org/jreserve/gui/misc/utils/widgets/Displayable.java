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

import java.awt.Image;
import java.beans.BeanInfo;
import javax.swing.Icon;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Displayable {
    
    public Icon getIcon();
    
    public String getDisplayName();
    
    public static class Utils {
        public static String displayProjectPath(FileObject fo) {
            return displayProjectPath(fo, false);
        }
        
        public static String displayProjectPath(FileObject fo, boolean extension) {
            StringBuilder path = new StringBuilder();
            path.append(extension? fo.getNameExt() : fo.getName());
            
            Project p = FileOwnerQuery.getOwner(fo);
            FileObject pFo = p==null? null : p.getProjectDirectory();
            if(pFo != null && FileUtil.isParentOf(pFo, fo)) {
                fo = fo.getParent();
                while(!pFo.equals(fo)) {
                    path.insert(0, '/').insert(0, fo.getName());
                    fo = fo.getParent();
                }
            }
            
            return path.toString();
        }
        
        public static String displayPath(org.openide.nodes.Node node) {
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
    
    public static class Node implements Displayable {
        
        private final org.openide.nodes.Node node;
        
        public Node(org.openide.nodes.Node node) {
            this.node = node;
        }
        
        @Override
        public Icon getIcon() {
            Image img = node.getIcon(BeanInfo.ICON_COLOR_16x16);
            return ImageUtilities.image2Icon(img);
        }

        @Override
        public String getDisplayName() {
            return Displayable.Utils.displayPath(node);
        }
    }
}
