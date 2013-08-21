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
class PropertyTableModel implements TreeModel {
    
    private PropertyItem root;
    private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    
    PropertyTableModel() {
        this(PropertyItem.createRoot());
    }

    PropertyTableModel(PropertyItem root) {
        this.root = root;
    }
    
    void setRoot(PropertyItem root) {
        this.root = root;
        fireChanged();
    }
    
    private void fireChanged() {
        TreeModelEvent evt = new TreeModelEvent(this, new Object[]{root});
        for(TreeModelListener listener : listeners) {
            listener.treeStructureChanged(evt);
        }
    }
    
    @Override
    public Object getRoot() {
        return root;
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
        return ((PropertyItem) parent).getChildren().size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
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
    public void valueForPathChanged(TreePath path, Object newValue) {
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
