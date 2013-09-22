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

package org.jreserve.gui.calculations.api;

import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - source",
    "MSG.CalculationEventUtil.CreatedFrom=Created from ''{0}''.",
    "MSG.CalculationEventUtil.Created=Created.",
    "MSG.CalculationEventUtil.Deleted=Deleted.",
    "# {0} - oldName",
    "# {1} - newName",
    "MSG.CalculationEventUtil.Renamed=Renamed ''{0}'' => ''{1}''.",
    "MSG.CalculationEventUtil.Changed=Calculation changed."
})
public class CalculationEventUtil extends AbstractAuditEvent implements CalculationEvent {
    
    public static void fireCreated(DataObject obj, DataObject sourceObj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        CalculationProvider ds = obj.getLookup().lookup(CalculationProvider.class);
        CalculationProvider source = sourceObj.getLookup().lookup(CalculationProvider.class);
        fireEvent(new Created(ao, ds, source));
    }
    
    public static void fireCreated(DataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        CalculationProvider cp = obj.getLookup().lookup(CalculationProvider.class);
        fireCreated(ao, cp);
    }
    
    public static void fireCreated(AuditedObject ao, CalculationProvider cp) {
        fireEvent(new Created(ao, cp));
    }
    
    public static void fireEvent(CalculationEvent evt) {
        EventBusManager.getDefault().publish(evt);
    }
    
    public static void fireDeleted(DataObject obj) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        CalculationProvider cp = obj.getLookup().lookup(CalculationProvider.class);
        fireDeleted(ao, cp);
    }
    
    public static void fireDeleted(AuditedObject ao, CalculationProvider cp) {
        fireEvent(new Deleted(ao, cp));
    }
    
    public static void fireRenamed(DataObject obj, String oldPath) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        CalculationProvider cp = obj.getLookup().lookup(CalculationProvider.class);
        fireRenamed(ao, cp, oldPath);
    }
    
    public static void fireRenamed(AuditedObject ao, CalculationProvider cp, String oldPath) {
        fireEvent(new Renamed(ao, cp, oldPath));
    }
    
    public static void fireChanged(DataObject obj) {
        fireChanged(obj, Bundle.MSG_CalculationEventUtil_Changed());
    }
    
    public static void fireChanged(DataObject obj, String change) {
        AuditedObject ao = obj.getLookup().lookup(AuditedObject.class);
        CalculationProvider cp = obj.getLookup().lookup(CalculationProvider.class);
        fireChanged(ao, cp, change);
    }
    
    public static void fireChanged(AuditedObject ao, CalculationProvider cp, String change) {
        fireEvent(new Change(ao, cp, change));
    }

    private final CalculationProvider cp;
    
    private CalculationEventUtil(AuditedObject obj, CalculationProvider cp, String change) {
        super(obj, change);
        this.cp = cp;
    }

    @Override
    public CalculationProvider getCalculationProvider() {
        return cp;
    }

    
    private static class Created extends CalculationEventUtil implements CalculationEvent.Created {
        private Created(AuditedObject ao, CalculationProvider cp, CalculationProvider source) {
            super(ao, cp, Bundle.MSG_CalculationEventUtil_CreatedFrom(source.getPath()));
        }
        
        private Created(AuditedObject ao, CalculationProvider cp) {
            super(ao, cp, Bundle.MSG_CalculationEventUtil_Created());
        }
        
        @Override
        public String toString() {
            return "Calculation created: "+super.getComponent();
        }
    }
    
    private static class Deleted extends CalculationEventUtil implements CalculationEvent.Deleted {
        private Deleted(AuditedObject ao, CalculationProvider cp) {
            super(ao, cp, Bundle.MSG_CalculationEventUtil_Deleted());
        }
        
        @Override
        public String toString() {
            return "Calculation deleted: "+super.getComponent();
        }
    }
    
    private static class Renamed extends CalculationEventUtil implements CalculationEvent.Renamed {
        private final String oldPath;
        
        private Renamed(AuditedObject ao, CalculationProvider cp, String oldPath) {
            super(ao, cp, Bundle.MSG_CalculationEventUtil_Renamed(oldPath, cp.getPath()));
            this.oldPath = oldPath;
        }

        @Override
        public String getOldPath() {
            return oldPath;
        }
        
        @Override
        public String toString() {
            return "Calculation renamed: "+oldPath+" => " + super.getComponent();
        }
    }
    
    public static class Change extends CalculationEventUtil implements CalculationEvent.Change {
        protected Change(AuditedObject ao, CalculationProvider cp, String change) {
            super(ao, cp, change);
        }
        
        @Override
        public String toString() {
            return "Calculation changed: "+super.getComponent();
        }
    }
}