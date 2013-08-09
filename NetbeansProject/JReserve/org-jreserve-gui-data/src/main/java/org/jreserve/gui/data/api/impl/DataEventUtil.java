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
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
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
public class DataEventUtil {
    
    static void itemRenamed(DataItem item, String oldPath) {
        String change = (item instanceof DataCategory)?
                Bundle.MSG_DataEvent_DataCategory_Renamed(oldPath, item.getPath()) :
                Bundle.MSG_DataEvent_DataSource_Renamed(oldPath, item.getPath());
        DataEvent.DataItemRenamed evt = new DataEvent.DataItemRenamed(item, change, oldPath);
        EventBusManager.getDefault().publish(evt);
    }
    
    static void categoryCreated(DataCategory category) {
        String msg = Bundle.MSG_DataEvent_DataCategory_Created();
        DataEvent.DataCategoryCreatedEvent evt = new DataEvent.DataCategoryCreatedEvent(category, msg);
        EventBusManager.getDefault().publish(evt);
    }
    
    static void itemDeleted(DataCategory parent, DataItem item, boolean isRoot) {
        DataEvent evt;
        if(item instanceof DataCategory) {
            evt = new DataEvent.DataCategoryDeleteEvent(parent, (DataCategory) item, Bundle.MSG_DataEvent_DataCategory_Deleted(), isRoot);
        } else {
            evt = new DataEvent.DataSourceDeleteEvent(parent, (DataSource) item, Bundle.MSG_DataEvent_DataSource_Deleted(), isRoot);
        }
        EventBusManager.getDefault().publish(evt);
    }
    
    static void sourceCreated(DataSource source) {
        String msg = Bundle.MSG_DataEvent_DataSource_Created();
        DataEvent.DataSourceCreatedEvent evt = new DataEvent.DataSourceCreatedEvent(source, msg);
        EventBusManager.getDefault().publish(evt);
    }
    
}
