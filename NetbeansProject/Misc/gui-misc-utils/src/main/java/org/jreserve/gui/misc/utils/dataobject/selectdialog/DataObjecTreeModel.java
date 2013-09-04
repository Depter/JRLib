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
package org.jreserve.gui.misc.utils.dataobject.selectdialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jreserve.gui.misc.utils.dataobject.DataObjectChooser;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataObjecTreeModel implements TreeModel {

    private DataObjectChooser.Controller root;
    
    DataObjecTreeModel(DataObjectChooser.Controller controller) {
        this.root = controller;
    }
    
    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if(parent == root)
            return root.getRoots()[index];
        return getChildren((DataObject)parent).get(index);
    }
    
    private List<DataObject> getChildren(DataObject obj) {
        if(obj instanceof DataFolder) {
            List<DataObject> result = new ArrayList<DataObject>();
            for(DataObject child : ((DataFolder)obj).getChildren())
                if(root.showDataObject(child))
                    result.add(child);
            return result;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent == root)
            return root.getRoots().length;
        return getChildren((DataObject)parent).size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return node != root && !(node instanceof DataFolder);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent == root) {
            DataObject[] roots = root.getRoots();
            for(int i=0; i<roots.length; i++)
                if(roots[i] == child)
                    return i;
            return -1;
        }
        
        List<DataObject> children = getChildren((DataObject)parent);
        for(int i=0; i<children.size(); i++)
            if(children.get(i) == child)
                return i;
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }
}
