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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataManager;
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
        
        DataItem item = root.getDataItem(path.substring(ROOT_NAME.length()+1));
        if(item instanceof DataCategoryImpl)
            return (DataCategoryImpl) item;
        
        logger.log(Level.WARNING, LOG_CATEGORY_NOT_FOUND, path);
        return null;
    }
    
    private void checkPath(String path) {
        if(path == null || !path.startsWith("Data") || path.endsWith(""+DataItem.PATH_SEPARATOR)) {
            String msg = String.format("Invalid path: \"%s\"! Path must start with Data and must not end with '/'", path);
            throw new IllegalArgumentException(msg);
        }
    }
    
    @Override
    public synchronized DataSourceImpl getDataSource(String path) {
        checkPath(path);
        DataItem item = root.getDataItem(path.substring(ROOT_NAME.length()+1));
        if(item instanceof DataSourceImpl)
            return (DataSourceImpl) item;
        
        logger.log(Level.WARNING, LOG_SOURCE_NOT_FOUND, path);
        return null;
    }
}
