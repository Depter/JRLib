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
package org.jreserve.gui.data.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCategoryImpl extends DataItem implements DataCategory {

    private Set<DataItem> children;

    DataCategoryImpl(FileObject folder, DataCategoryImpl parent) {
        super(parent.getDataManager(), folder, parent);
    }

    DataCategoryImpl(DataManagerImpl manager, FileObject folder, DataCategoryImpl parent) {
        super(manager, folder, parent);
    }

    DataItem getDataItem(String path) {
        int separatorIndex = path.indexOf(PATH_SEPARATOR);
        String childName;

        if(separatorIndex < 0) {
            childName = path;
            path = null;
        } else {
            childName = path.substring(0, separatorIndex);
            path = path.substring(separatorIndex + 1);
        }
        
        DataItem child = getChildItemByName(childName);
        if(path == null)
            return child;
        
        if(child instanceof DataCategoryImpl)
            return ((DataCategoryImpl)child).getDataItem(path);
        return null;
    }
    
    private DataItem getChildItemByName(String name) {
        for(DataItem item : getLoadedChildren())
            if(item.getName().equalsIgnoreCase(name))
                return item;
        return null;
    }

    @Override
    public List<DataCategory> getChildCategories() {
        List<DataCategory> result = new ArrayList<DataCategory>();
        for(DataItem item : getLoadedChildren())
            if(item instanceof DataCategoryImpl)
            result.add((DataCategoryImpl) item);
        return result;
    }

    private Set<DataItem> getLoadedChildren() {
        if(children == null)
            loadChildren();
        return children;
    }

    private void loadChildren() {
        children = new TreeSet<DataItem>();

        for(FileObject fo : file.getChildren()) {
            if(fo.isFolder()) {
                children.add(new DataCategoryImpl(fo, this));
            } else if(DataSourceImpl.isSourceFile(fo)) {
                children.add(new DataSourceImpl(fo, this));
            }
        }
    }

    @Override
    public List<DataSource> getDataSources() {
        List<DataSource> result = new ArrayList<DataSource>();
        for(DataItem item : getLoadedChildren())
            if(item instanceof DataSourceImpl)
                result.add((DataSourceImpl) item);
        return result;
    }

    @Override
    public String toString() {
        return String.format("DataCategory [%s]", getPath());
    }
}
