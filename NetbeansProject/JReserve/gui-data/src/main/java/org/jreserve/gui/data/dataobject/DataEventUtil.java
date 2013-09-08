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

package org.jreserve.gui.data.dataobject;

import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.DataEventUtil.Created=Storage created",
    "MSG.DataEventUtil.Deleted=Storage deleted",
    "# {0} - oldName",
    "# {1} - newName",
    "MSG.DataEventUtil.Renamed=Storage renamed ''{0}'' => ''{1}''.",
    "MSG.DataEventUtil.DataImport=Data imported.",
    "MSG.DataEventUtil.DataDelete=Data deleted."
})
public class DataEventUtil extends AbstractAuditEvent implements DataEvent {
    
    public static void fireCreated(DataSourceDataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        fireEvent(new Created(ao, ds));
    }
    
    private static void fireEvent(DataEvent evt) {
        EventBusManager.getDefault().publish(evt);
    }
    
    static void fireDeleted(DataSourceDataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        fireEvent(new Deleted(ao, ds));
    }
    
    static void fireRenamed(DataSourceDataObject obj, String oldPath) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        fireEvent(new Renamed(ao, ds, oldPath));
    }
    
    static void fireDataImported(DataSourceDataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        fireEvent(new DataImport(ao, ds));
    }
    
    static void fireDataDeleted(DataSourceDataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        DataSource ds = obj.getLookup().lookup(DataSource.class);
        fireEvent(new DataDeletion(ao, ds));
    }
    
    private DataSource ds;
    
    private DataEventUtil(AuditedObject ao, DataSource ds, String change) {
        super(ao, change);
        this.ds = ds;
    }

    @Override
    public DataSource getDataSource() {
        return ds;
    }
    
    private static class Created extends DataEventUtil implements DataEvent.Created {
        private Created(AuditedObject ao, DataSource ds) {
            super(ao, ds, Bundle.MSG_DataEventUtil_Created());
        }
        
        @Override
        public String toString() {
            return "DataSource created: "+super.getComponent();
        }
    }
    
    private static class Deleted extends DataEventUtil implements DataEvent.Deleted {
        private Deleted(AuditedObject ao, DataSource ds) {
            super(ao, ds, Bundle.MSG_DataEventUtil_Deleted());
        }
        
        @Override
        public String toString() {
            return "DataSource deleted: "+super.getComponent();
        }
    }
    
    private static class DataImport extends DataEventUtil implements DataEvent.DataImport {
        private DataImport(AuditedObject ao, DataSource ds) {
            super(ao, ds, Bundle.MSG_DataEventUtil_DataImport());
        }
        
        @Override
        public String toString() {
            return "Data imported: "+super.getComponent();
        }
    }
    
    private static class DataDeletion extends DataEventUtil implements DataEvent.DataDeletion {
        private DataDeletion(AuditedObject ao, DataSource ds) {
            super(ao, ds, Bundle.MSG_DataEventUtil_DataDelete());
        }
        
        @Override
        public String toString() {
            return "Data deleted: "+super.getComponent();
        }
    }
    
    private static class Renamed extends DataEventUtil implements DataEvent.Renamed {
        private final String oldPath;
        
        private Renamed(AuditedObject ao, DataSource ds, String oldPath) {
            super(ao, ds, Bundle.MSG_DataEventUtil_Renamed(oldPath, ds.getPath()));
            this.oldPath = oldPath;
        }

        @Override
        public String getOldPath() {
            return oldPath;
        }
        
        @Override
        public String toString() {
            return "DataSource renamed: "+oldPath+" => " + super.getComponent();
        }
    }

}
