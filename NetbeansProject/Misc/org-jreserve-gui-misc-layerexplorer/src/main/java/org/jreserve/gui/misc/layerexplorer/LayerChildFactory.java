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
package org.jreserve.gui.misc.layerexplorer;

import java.util.Collections;
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
class LayerChildFactory extends ChildFactory<FileObject> {
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {
        @Override
        public int compare(FileObject o1, FileObject o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareTo(n2);
        }
    };
    
    private FileObject parent;

    LayerChildFactory(FileObject parent) {
        this.parent = parent;
    }
    
    @Override
    protected boolean createKeys(List<FileObject> list) {
        
        list.addAll(getChildFolders());
        list.addAll(getChildFiles());
        return true;
    }
    
    private List<? extends FileObject> getChildFolders() {
        if(parent == null)
            parent = FileUtil.getConfigRoot();
        List<? extends FileObject> folders = Collections.list(parent.getFolders(false));
        Collections.sort(folders, COMPARATOR);
        return folders;
    }
    
    private List<? extends FileObject> getChildFiles() {
        List<? extends FileObject> files = Collections.list(parent.getData(false));
        Collections.sort(files, COMPARATOR);
        return files;
    }

    @Override
    protected Node createNodeForKey(FileObject key) {
        AbstractNode node = createNode(key);
        node.setDisplayName(key.getNameExt());
        setIconBase(node, key);
        return node;
    }
    
    private AbstractNode createNode(FileObject key) {
        Children children = key.isFolder()? Children.create(new LayerChildFactory(key), true) : Children.LEAF;
        AbstractNode node = new AbstractNode(children, Lookups.singleton(key));
        return node;
    }
    
    private void setIconBase(AbstractNode node, FileObject file) {
        Object base = file.getAttribute("iconBase");
        if(base instanceof String)
            node.setIconBaseWithExtension((String) base);
        else if(file.isFolder())
            node.setIconBaseWithExtension("org/jreserve/gui/misc/layerexplorer/folder.png");
    }
}
