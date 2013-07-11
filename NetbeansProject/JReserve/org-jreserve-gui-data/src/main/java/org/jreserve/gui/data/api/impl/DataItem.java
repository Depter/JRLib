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

import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataItem implements Comparable<DataItem> {
    
    final static char PATH_SEPARATOR = '/';

    protected final FileObject file;
    private final DataManagerImpl manager;
    private DataCategoryImpl parent;
    
    DataItem(DataManagerImpl manager, FileObject file, DataCategoryImpl parent) {
        this.manager = manager;
        this.file = file;
        this.parent = parent;
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
        if(parent == null)
            return getName();
        return parent.getPath() + PATH_SEPARATOR + getName();
    }

    @Override
    public int compareTo(DataItem o) {
        return file.getNameExt().compareToIgnoreCase(o.file.getNameExt());
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DataItem) &&
               compareTo(((DataItem)o)) == 0;
    }
    
    @Override
    public int hashCode() {
        return file.getNameExt().toLowerCase().hashCode();
    }
}
