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

package org.jreserve.gui.data.api;

import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DataEvent<T extends DataItem> extends AbstractAuditEvent {
    
    private T item;
    
    protected DataEvent(T item, String change) {
        super(item.getDataManager().getProject(), item.getPath(), change);
        this.item = item;
    }
    
    public T getDataItem() {
        return item;
    }
    
    public static class DataItemRenamed<T extends DataItem> extends DataEvent<T> {
        private final String oldPath;
        public DataItemRenamed(T item, String change, String oldPath) {
            super(item, change);
            this.oldPath = oldPath;
        }
        
        public String getOldPath() {
            return oldPath;
        }
    }
    
    public static class DataChangeEvent extends DataEvent<DataSource> {
        public DataChangeEvent(DataSource source, String change) {
            super(source, change);
        }
    }
    
    public static class DataCategoryCreatedEvent extends DataEvent<DataCategory> {
        public DataCategoryCreatedEvent(DataCategory category, String change) {
            super(category, change);
        }
    }
    
    public static class DataSourceCreatedEvent extends DataEvent<DataSource> {
        public DataSourceCreatedEvent(DataSource source, String change) {
            super(source, change);
        }
    }
    
    public static abstract class DataItemDeletedEvent<T extends DataItem> extends DataEvent<T> {
        
        private final boolean isRoot;
        private final DataCategory parent;
        
        public DataItemDeletedEvent(DataCategory parent, T item, String change, boolean isRoot) {
            super(item, change);
            this.isRoot = isRoot;
            this.parent = parent;
        }
        
        public DataCategory getParent() {
            return parent;
        }
        
        public boolean isRootDelete() {
            return isRoot;
        }
    }
    
    public static class DataCategoryDeleteEvent extends DataItemDeletedEvent<DataCategory> {
        public DataCategoryDeleteEvent(DataCategory parent, DataCategory item, String change, boolean isRoot) {
            super(parent, item, change, isRoot);
        }
    }
    
    public static class DataSourceDeleteEvent extends DataItemDeletedEvent<DataSource> {
        public DataSourceDeleteEvent(DataCategory parent, DataSource item, String change, boolean isRoot) {
            super(parent, item, change, isRoot);
        }
    }
}
