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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCategoryTreeModel implements TreeModel {

    private DataManager manager;
    private boolean showSources;
    private DataType dataType;
    private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    
    public DataCategoryTreeModel() {
        this(null);
    }
    
    public DataCategoryTreeModel(DataManager manager) {
        this(manager, false);
    }
    
    public DataCategoryTreeModel(DataManager manager, boolean showSources) {
        this(manager, showSources, null);
    }
    
    public DataCategoryTreeModel(DataManager manager, boolean showSources, DataType dataType) {
        this.manager = manager;
        this.showSources = showSources;
        this.dataType = dataType;
    }
    
    public void setShowSources(boolean showSources) {
        this.showSources = showSources;
        fireChange();
    }
    
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
        fireChange();
    }
    
    private void fireChange() {
        TreeModelEvent evt = new TreeModelEvent(this, new Object[]{getRoot()});
        for(TreeModelListener l : listeners.toArray(new TreeModelListener[listeners.size()]))
            l.treeStructureChanged(evt);
    }
    
    public void setDataManager(DataManager manager) {
        this.manager = manager;
        fireChange();
    }

    @Override
    public Object getRoot() {
        return manager==null? null : manager.getCategory(null);
    }

    @Override
    public Object getChild(Object parent, int index) {
        return getChildren(parent).get(index);
    }

    private List<DataItem> getChildren(Object parent) {
        if(parent instanceof DataCategory)
            return getChildren((DataCategory)parent);
        return Collections.EMPTY_LIST;
    }
    
    private List<DataItem> getChildren(DataCategory category) {
        List<DataItem> result = new ArrayList<DataItem>();
        result.addAll(category.getChildCategories());
        if(showSources) {
            if(dataType == null) {
                result.addAll(category.getDataSources());
            } else {
                for(DataSource ds : category.getDataSources())
                    if(dataType == ds.getDataType())
                        result.add(ds);
            }
        }
        return result;
    }

    @Override
    public int getChildCount(Object parent) {
        return parent==null? 0 : getChildren(parent).size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return node==null || (node instanceof DataSource);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return getChildren(parent).indexOf(child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if(!listeners.contains(l))
            listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }
}