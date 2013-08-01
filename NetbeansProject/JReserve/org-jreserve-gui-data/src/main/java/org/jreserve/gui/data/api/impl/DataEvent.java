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

import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.DataEvent.DataCategory.Created=Data category created.",
    "MSG.DataEvent.DataCategory.Deleted=Data category deleted.",
    "MSG.DataEvent.DataSource.Created=Data storage created.",
    "MSG.DataEvent.DataSource.Deleted=Data storage deleted.",
    "# {0} - oldPath",
    "# {1} - newPath",
    "MSG.DataEvent.DataCategory.Renamed=Data category renamed '{0}' -> '{1}'.",
    "# {0} - oldPath",
    "# {1} - newPath",
    "MSG.DataEvent.DataSource.Renamed=Data storage renamed '{0}' -> '{1}'."
})
public abstract class DataEvent<T extends DataItem> extends AbstractAuditEvent {
    
    static void itemRenamed(DataItem item, String oldPath) {
        String change = (item instanceof DataCategory)?
                Bundle.MSG_DataEvent_DataCategory_Renamed(oldPath, item.getPath()) :
                Bundle.MSG_DataEvent_DataSource_Renamed(oldPath, item.getPath());
        DataItemRenamed evt = new DataItemRenamed(item, change, oldPath);
        EventBusManager.getDefault().publish(evt);
    }
    
    static void categoryCreated(DataCategory category) {
        String msg = Bundle.MSG_DataEvent_DataCategory_Created();
        DataCategoryCreatedEvent evt = new DataCategoryCreatedEvent(category, msg);
        EventBusManager.getDefault().publish(evt);
    }
    
    static void itemDeleted(DataCategory parent, DataItem item, boolean isRoot) {
        DataEvent evt;
        if(item instanceof DataCategory) {
            evt = new DataCategoryDeleteEvent(parent, (DataCategory) item, Bundle.MSG_DataEvent_DataCategory_Deleted(), isRoot);
        } else {
            evt = new DataSourceDeleteEvent(parent, (DataSource) item, Bundle.MSG_DataEvent_DataSource_Deleted(), isRoot);
        }
        EventBusManager.getDefault().publish(evt);
    }
    
    static void sourceCreated(DataSource source) {
        String msg = Bundle.MSG_DataEvent_DataSource_Created();
        DataSourceCreatedEvent evt = new DataSourceCreatedEvent(source, msg);
        EventBusManager.getDefault().publish(evt);
    }
    
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
        private DataItemRenamed(T item, String change, String oldPath) {
            super(item, change);
            this.oldPath = oldPath;
        }
        
        public String getOldPath() {
            return oldPath;
        }
    }
    
    public static class DataCategoryCreatedEvent extends DataEvent<DataCategory> {
        private DataCategoryCreatedEvent(DataCategory category, String change) {
            super(category, change);
        }
    }
    
    public static class DataSourceCreatedEvent extends DataEvent<DataSource> {
        private DataSourceCreatedEvent(DataSource source, String change) {
            super(source, change);
        }
    }
    
    public static abstract class DataItemDeletedEvent<T extends DataItem> extends DataEvent<T> {
        
        private final boolean isRoot;
        private final DataCategory parent;
        
        protected DataItemDeletedEvent(DataCategory parent, T item, String change, boolean isRoot) {
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
        private DataCategoryDeleteEvent(DataCategory parent, DataCategory item, String change, boolean isRoot) {
            super(parent, item, change, isRoot);
        }
    }
    
    public static class DataSourceDeleteEvent extends DataItemDeletedEvent<DataSource> {
        private DataSourceDeleteEvent(DataCategory parent, DataSource item, String change, boolean isRoot) {
            super(parent, item, change, isRoot);
        }
    }
}
