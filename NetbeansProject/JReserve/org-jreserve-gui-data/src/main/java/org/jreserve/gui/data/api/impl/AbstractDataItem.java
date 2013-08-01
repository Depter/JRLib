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
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractDataItem implements Comparable<AbstractDataItem> {
    
    private final static Logger logger = Logger.getLogger(AbstractDataItem.class.getName());
    
    final static char PATH_SEPARATOR = '/';

    protected final FileObject file;
    private final DataManagerImpl manager;
    private DataCategoryImpl parent;
    private String path;
    
    AbstractDataItem(DataManagerImpl manager, FileObject file, DataCategoryImpl parent) {
        this.manager = manager;
        this.file = file;
        this.parent = parent;
        initPath();
    }
    
    private void initPath() {
        this.path = parent==null? "" : parent.getPath() + PATH_SEPARATOR;
        this.path += file.getName();
    }
    
    public DataManagerImpl getDataManager() {
        return manager;
    }
    
    public DataCategoryImpl getParent() {
        return parent;
    }
    
    public String getName() {
        return file.getName();
    }
    
    public String getPath() {
        return path;
    }
    
    void rename(String name) throws IOException {
        FileLock lock = null;
        try {
            String oldPath = getPath();
            lock = file.lock();
            file.rename(lock, name, file.getExt());
            initPath();
            logger.info(String.format("Renamed DataItem: '%s' -> '%s'", oldPath, getPath()));
        } catch (Exception ex) {
            String msg = "Unable to rename DataItem: " + getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }

    void delete() throws IOException {
        try {
            file.delete();
            if(parent != null) {
                parent.removeChild(this);
                this.parent = null;
            }
            logger.log(Level.INFO, "Deleted DataItem: {0}", getPath());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to delete DataItem: " + getPath(), ex);
            throw ex;
        }
    }
    
    public FileObject getFile() {
        return file;
    }
    
    @Override
    public int compareTo(AbstractDataItem o) {
        return file.getNameExt().compareToIgnoreCase(o.file.getNameExt());
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof AbstractDataItem) &&
               compareTo(((AbstractDataItem)o)) == 0;
    }
    
    @Override
    public int hashCode() {
        return file.getNameExt().toLowerCase().hashCode();
    }
}
