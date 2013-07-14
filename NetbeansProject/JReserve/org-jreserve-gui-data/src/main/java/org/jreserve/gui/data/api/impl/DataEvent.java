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
import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.DataEvent.DataCategory.Created=Data category created."
})
public class DataEvent {
    
    static void categoryCreated(DataCategory category) {
        String msg = Bundle.MSG_DataEvent_DataCategory_Created();
        DataCategoryCreatedEvent evt = new DataCategoryCreatedEvent(category, msg);
        EventBusManager.getDefault().publish(evt);
    }
    
    public static class DataCategoryEvent extends AbstractAuditEvent {
        private DataCategory category;
        
        protected DataCategoryEvent(DataCategory category, String change) {
            super(category.getDataManager().getProject(), 
                  category.getPath(), change);
            this.category = category;
        }
        
        public DataCategory getDataCategory() {
            return category;
        }
    
    }
    
    public static class DataCategoryCreatedEvent extends DataCategoryEvent {

        private DataCategoryCreatedEvent(DataCategory category, String change) {
            super(category, change);
        }
    }
}
