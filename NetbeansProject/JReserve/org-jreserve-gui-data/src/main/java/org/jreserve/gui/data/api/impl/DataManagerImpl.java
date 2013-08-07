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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataManagerImpl implements DataManager {
    
    @ProjectServiceProvider(
        service = DataManager.class,
        projectType = "org-jreserve-project-jreserve"
    )
    public static DataManager createDataManager(Project project) {
        return new DataManagerImpl(project);
    }
    
    private final static Logger logger = Logger.getLogger(DataManagerImpl.class.getName());
    private final static String LOG_ROOT_CREATE_ERROR = "Unable to create root DataCategory for project: %s";   //NOi18
    private final static String LOG_CATEGORY_NOT_FOUND = "No DataCategory at path: {0}";   //NOi18
    private final static String LOG_SOURCE_NOT_FOUND = "No DataCategory at path: {0}";   //NOi18
    private final static String ROOT_NAME = "Data"; //NOi18
    
    private final DataCategoryImpl root;
    private final Project project;
    
    DataManagerImpl(Project project) {
        this.project = project;
        root = createRoot(project);
    }
    
    @Override
    public Project getProject() {
        return project;
    }
    
    private DataCategoryImpl createRoot(Project project) {
        FileObject dir = project.getProjectDirectory();
        try {
            return new DataCategoryImpl(this, FileUtil.createFolder(dir, ROOT_NAME), null);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format(LOG_ROOT_CREATE_ERROR, dir.getPath()), ex);
            return null;
        }
    }
    
    @Override
    public synchronized DataCategoryImpl getCategory(String path) {
        if(path == null || path.length()==0 || ROOT_NAME.equalsIgnoreCase(path))
            return root;
        checkPath(path);
        
        AbstractDataItem item = root.getDataItem(path.substring(ROOT_NAME.length()+1));
        if(item instanceof DataCategoryImpl)
            return (DataCategoryImpl) item;
        
        logger.log(Level.FINE, LOG_CATEGORY_NOT_FOUND, path);
        return null;
    }
    
    private void checkPath(String path) {
        if(path == null || !path.startsWith("Data") || path.endsWith(""+AbstractDataItem.PATH_SEPARATOR)) {
            String msg = String.format("Invalid path: \"%s\"! Path must start with Data and must not end with '/'", path);
            throw new IllegalArgumentException(msg);
        }
    }
    
    @Override
    public synchronized DataSourceImpl getDataSource(String path) {
        checkPath(path);
        AbstractDataItem item = root.getDataItem(path.substring(ROOT_NAME.length()+1));
        if(item instanceof DataSourceImpl)
            return (DataSourceImpl) item;
        
        logger.log(Level.FINE, LOG_SOURCE_NOT_FOUND, path);
        return null;
    }
    
    @Override
    public synchronized DataCategory createDataCategory(DataCategory parent, String name) throws IOException {
        if(this != parent.getDataManager())
            throw new IllegalArgumentException("DataCategory belongs to another data manager!");
        DataCategoryImpl child = ((DataCategoryImpl) parent).createChildCategory(name);
        DataEvent.categoryCreated(child);
        return child;
    }
    
    @Override
    public synchronized void deleteDataItem(DataItem item) throws IOException {
        if(this != item.getDataManager())
            throw new IllegalArgumentException("DataItem belongs to another data manager!");
        if(item instanceof DataCategory)
            deleteChildren((DataCategory) item);
        
        DataCategory parent = item.getParent();
        ((AbstractDataItem)item).delete();
        DataEvent.itemDeleted(parent, item, true);
    }
    
    private void deleteChildren(DataCategory category) throws IOException {
        for(DataCategory child : category.getChildCategories()) {
            deleteChildren(child);
            ((AbstractDataItem) child).delete();
            DataEvent.itemDeleted(category, child, false);
        }
        
        for(DataSource child : category.getDataSources()) {
            ((AbstractDataItem)child).delete();
            DataEvent.itemDeleted(category, child, false);
        }
    }
    
    @Override
    public synchronized DataSource createDataSource(DataCategory parent, String name, DataType dataType, DataProvider dataProvider) throws IOException {
        if(this != parent.getDataManager())
            throw new IllegalArgumentException("DataCategory belongs to another data manager!");
        DataSourceImpl child = ((DataCategoryImpl) parent).createChildSource(name, dataType, dataProvider);
        DataEvent.sourceCreated(child);
        return child;
    }
    
    @Override
    public synchronized void renameDataItem(DataItem item, String newName) throws IOException {
        if(this != item.getDataManager())
            throw new IllegalArgumentException("DataItem belongs to another data manager!");
        
        AbstractDataItem impl = (AbstractDataItem) item;
        checkValidName(impl, newName);
        LinkedHashMap<DataItem, String> oldNames = new LinkedHashMap<DataItem, String>();
        fillOldNames(oldNames, item);
        impl.rename(newName);
        
        for(Map.Entry<DataItem, String> entry : oldNames.entrySet())
            DataEvent.itemRenamed(entry.getKey(), entry.getValue());
    }
    
    private void checkValidName(AbstractDataItem item, String name) {
        if(name == null)
            throw new NullPointerException("Name can not be null!");
        if(name.length() == 0)
            throw new IllegalArgumentException("Name can not be empty!");
        
        DataCategory parent = item.getParent();
        if(parent == null)
            throw new IllegalArgumentException("Can not rename root category!");
        
        for(DataItem sibling : getSiblings(item)) {
            if(sibling.getName().equalsIgnoreCase(name))
                throw new IllegalArgumentException(String.format("Name '%s' already used by '%s'!", name, sibling.getPath()));
        }
    }
    
    private List<DataItem> getSiblings(AbstractDataItem item) {
        List<DataItem> result = new ArrayList<DataItem>();
        if(item instanceof DataCategory)
            result.addAll(item.getParent().getChildCategories());
        else
            result.addAll(item.getParent().getDataSources());
        result.remove(item);
        return result;
    }
    
    private void fillOldNames(Map<DataItem, String> names, DataItem item) {
        if(item instanceof DataCategory) {
            DataCategory category = (DataCategory) item;
            for(DataCategory child : category.getChildCategories())
                fillOldNames(names, child);
            for(DataSource child : category.getDataSources())
                names.put(child, child.getPath());
        }
        names.put(item, item.getPath());
    }
    
    @Override
    public void moveDataItem(DataCategory target,  DataItem item) throws IOException {
        checkMovable(target, item);
        
        AbstractDataItem itemImpl = (AbstractDataItem) item;
        LinkedHashMap<DataItem, String> oldNames = new LinkedHashMap<DataItem, String>();
        fillOldNames(oldNames, item);
        ((AbstractDataItem) item).move((DataCategoryImpl)target);
        
        for(Map.Entry<DataItem, String> entry : oldNames.entrySet())
            DataEvent.itemRenamed(entry.getKey(), entry.getValue());
    }
    
    private void checkMovable(DataCategory target, DataItem item) {
        if(this != item.getDataManager())
            throw new IllegalArgumentException("DataItem belongs to another data manager!");
        if(this != target.getDataManager())
            throw new IllegalArgumentException("DataCategory belongs to another data manager!");
        if(isChildOf(item, target))
            throw new IllegalArgumentException(String.format("Can not move a DataItem '%s' to one of it's children '%s'!", item.getPath(), target.getPath()));
        if(nameExists(target, item.getName(), (item instanceof DataSource)))
            throw new IllegalArgumentException(String.format("Can not move a DataItem '%s' to category '%s'. Name already exists!", item.getPath(), target.getPath()));
    }
    
    private boolean isChildOf(DataItem parent, DataItem child) {
        return child.getPath().startsWith(parent.getPath());
    }
    
    private boolean nameExists(DataCategory parent, String name, boolean isSource) {
        List<DataItem> items = new ArrayList<DataItem>();
        items.addAll(isSource? parent.getDataSources() : parent.getChildCategories());
        for(DataItem item : items)
            if(item.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
}
