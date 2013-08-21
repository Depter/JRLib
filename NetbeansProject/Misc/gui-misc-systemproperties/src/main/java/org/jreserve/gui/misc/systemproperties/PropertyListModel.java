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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PropertyListModel implements TreeModel {

    private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    private PropertyItem listRoot = new PropertyItem(null, "root");
    private PropertyItem root;
    private String search;

    PropertyListModel(PropertyItem root) {
        this.root = root;
    }
    
    void setSearch(String search) {
        this.search = search;
        query();
    }
    
    void setRoot(PropertyItem root) {
        this.root = root;
        query();
    }
    
    private void query() {
        listRoot.getChildren().clear();
        if(search != null)
            query(root);
        fireChanged();
    }
    
    private void query(PropertyItem item) {
        String path = item.getRoot();
        if(path != null && path.startsWith(search))
            addItem(item);
        
        for(PropertyItem child : item.getChildren())
            query(child);
    }
    
    private void addItem(PropertyItem item) {
        String path = item.getRoot();
        if(System.getProperties().containsKey(path)) {
            PropertyItem copy = new PropertyItem(path, path);
            listRoot.getChildren().add(copy);
        }
    }
    
    private void fireChanged() {
        Object[] path = new Object[]{listRoot};
        TreeModelEvent evt = new TreeModelEvent(this, path);
        for(TreeModelListener listener : listeners)
            listener.treeStructureChanged(evt);
    }
    
    
    @Override
    public Object getRoot() {
        return listRoot;
    }

    @Override
    public Object getChild(Object parent, int index) {
        PropertyItem item = (PropertyItem) parent;
        int i = 0;
        for(PropertyItem child : item.getChildren())
            if(i++ == index)
                return child;
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(listRoot == parent)
            return listRoot.getChildren().size();
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return listRoot!= node;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        PropertyItem item = (PropertyItem) parent;
        int i = 0;
        for(PropertyItem pc : item.getChildren()) {
            if(child == pc)
                return i;
            i++;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }
}
