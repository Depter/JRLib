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
package org.jreserve.dummy.projecttree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NodeFactory extends ChildFactory<FileObject>{
    
    private final static String HOME = "JRLib/ProjectTree";
    
    static Node createRootNode() {
        FileObject root = FileUtil.getConfigFile(HOME);
        return new AbstractNode(Children.create(new NodeFactory(root), true), Lookups.singleton(root));
    }
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            int p1 = getPosition(o1);
            int p2 = getPosition(o2);
            if(p1 == p2) 
                return o1.getName().compareToIgnoreCase(o2.getName());
            return p1 - p2;
        }
        
        private int getPosition(FileObject fo) {
            Object position = fo.getAttribute("position");
            if(position instanceof Number)
                return ((Number)position).intValue();
            return Integer.MAX_VALUE;
        }
    };
    
    private FileObject root;
    
    NodeFactory(FileObject root) {
        this.root = root;
    }

    @Override
    protected boolean createKeys(List<FileObject> list) {
        list.addAll(Arrays.asList(getChildren()));
        return true;
    }
    
    private FileObject[] getChildren() {
        FileObject[] children = root==null? new FileObject[0] : root.getChildren();
        Arrays.sort(children, COMPARATOR);
        return children;
    }

    @Override
    protected Node createNodeForKey(FileObject file) {
        AbstractNode node = new AbstractNode(Children.create(new NodeFactory(file), true), Lookups.singleton(file));
        node.setDisplayName(file.getName());

        String tooltip = getStrAttribute("tooltip", file);
        if(tooltip != null)
            node.setShortDescription(tooltip);
        
        String icon = getStrAttribute("icon", file);
        if(icon != null)
            node.setIconBaseWithExtension(icon);
        
        return node;
    }
    
    private String getStrAttribute(String name, FileObject file) {
        Object attr = file.getAttribute(name);
        if(attr instanceof String)
            return (String) attr;
        return null;
    }
    
}
