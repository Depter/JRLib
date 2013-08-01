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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCategoryImpl extends AbstractDataItem implements DataCategory {

    private Set<AbstractDataItem> children;

    DataCategoryImpl(FileObject folder, DataCategoryImpl parent) {
        super(parent.getDataManager(), folder, parent);
    }

    DataCategoryImpl(DataManagerImpl manager, FileObject folder, DataCategoryImpl parent) {
        super(manager, folder, parent);
    }

    AbstractDataItem getDataItem(String path) {
        int separatorIndex = path.indexOf(PATH_SEPARATOR);
        String childName;

        if(separatorIndex < 0) {
            childName = path;
            path = null;
        } else {
            childName = path.substring(0, separatorIndex);
            path = path.substring(separatorIndex + 1);
        }
        
        AbstractDataItem child = getChildItemByName(childName);
        if(path == null)
            return child;
        
        if(child instanceof DataCategoryImpl)
            return ((DataCategoryImpl)child).getDataItem(path);
        return null;
    }
    
    private AbstractDataItem getChildItemByName(String name) {
        for(AbstractDataItem item : getLoadedChildren())
            if(item.getName().equalsIgnoreCase(name))
                return item;
        return null;
    }

    @Override
    public List<DataCategory> getChildCategories() {
        List<DataCategory> result = new ArrayList<DataCategory>();
        for(AbstractDataItem item : getLoadedChildren())
            if(item instanceof DataCategoryImpl)
            result.add((DataCategoryImpl) item);
        return result;
    }

    private Set<AbstractDataItem> getLoadedChildren() {
        if(children == null)
            loadChildren();
        return children;
    }

    private void loadChildren() {
        children = new TreeSet<AbstractDataItem>();

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
        for(AbstractDataItem item : getLoadedChildren())
            if(item instanceof DataSourceImpl)
                result.add((DataSourceImpl) item);
        return result;
    }
    
    DataCategoryImpl createChildCategory(String name) throws IOException {
        if(file.getFileObject(name) != null)
            throw new IOException(String.format("DataCategory with name '%s/%s' already exists!", getPath(), name));
        
        if(name.indexOf(PATH_SEPARATOR) >= 0)
            throw new IOException(String.format("DataCategory name '%s' contains an illegal character!", name));
        
        FileObject folder = file.createFolder(name);
        DataCategoryImpl child = new DataCategoryImpl(folder, this);
        if(children != null)
            children.add(child);
        return child;
    }
    
    DataSourceImpl createChildSource(String name, DataProvider provider) throws IOException {
        if(provider == null)
            throw new NullPointerException("DataProvider can not be null!");
        
        if(file.getFileObject(name, DataSourceImpl.FILE_EXT) != null)
            throw new IOException(String.format("DataSource with name '%s/%s' already exists!", getPath(), name));
        
        if(name.indexOf(PATH_SEPARATOR) >= 0)
            throw new IOException(String.format("DataSource name '%s' contains an illegal character!", name));
        
        FileObject sourceFile = file.createData(name, DataSourceImpl.FILE_EXT);
        DataSourceImpl impl = new DataSourceImpl(sourceFile, this, provider);
        DataSourceUtil.save(impl);
        
        if(children != null)
            children.add(impl);
        
        return impl;
    }
    
    void removeChild(AbstractDataItem item) {
        children.remove(item);
    }
    
    @Override
    public String toString() {
        return String.format("DataCategory [%s]", getPath());
    }
}
