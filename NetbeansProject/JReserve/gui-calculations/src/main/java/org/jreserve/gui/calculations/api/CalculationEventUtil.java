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

import java.util.LinkedList;
import java.util.Queue;
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
    "LBL.CalculationEventUtil2.Created=Created.",
    "LBL.CalculationEventUtil2.Deleted=Deleted.",
    "# {0} - oldPath",
    "# {1} - newPath",
    "LBL.CalculationEventUtil2.Renamed=Renamed from ''{0}'' to ''{1}''."
})
public class CalculationEventUtil {

    private final AuditedObject auditedObject;
    private final CalculationProvider calculation;
    private final Queue<AbstractAuditEvent> auditCache = new LinkedList<AbstractAuditEvent>();
    
    public CalculationEventUtil(AuditedObject auditedObject, CalculationProvider calculation) {
        this.auditedObject = auditedObject;
        this.calculation = calculation;
    }
    
    public void fireCreated() {
        synchronized(calculation) {
            fireEvent(new Created());
            fireEvent(new AbstractAuditEvent(auditedObject, Bundle.LBL_CalculationEventUtil2_Created()));
        }
    }
    
    private void fireEvent(Object evt) {
        EventBusManager.getDefault().publish(evt);
    }
    
    public void fireDeleted() {
        synchronized(calculation) {
            fireEvent(new Deleted());
            fireEvent(new AbstractAuditEvent(auditedObject, Bundle.LBL_CalculationEventUtil2_Deleted()));
        }
    }
    
    public void fireRenamed(String oldPath) {
        synchronized(calculation) {
            fireEvent(new Renamed(oldPath));
            String path = calculation.getPath();
            fireEvent(new AbstractAuditEvent(auditedObject, Bundle.LBL_CalculationEventUtil2_Renamed(oldPath, path)));
        }
    }
    
    public void fireChange(String change) {
        synchronized(calculation) {
            fireEvent(new Changed());
            auditCache.add(new AbstractAuditEvent(auditedObject, change));
        }
    }
    
    public void clearAuditCache() {
        synchronized(calculation) {
            auditCache.clear();
        }
    }
    
    public void flushAuditCache() {
        synchronized(calculation) {
            AbstractAuditEvent evt;
            while((evt = auditCache.poll()) != null)
                fireEvent(evt);
        }
    }
    
    private class Event implements CalculationEvent {
        @Override
        public CalculationProvider getCalculationProvider() {
            return calculation;
        }
    }
    
    private class Created extends Event implements CalculationEvent.Created {}
    private class Deleted extends Event implements CalculationEvent.Deleted {}
    private class Changed extends Event implements CalculationEvent.Change {}
    private class Renamed extends Event implements CalculationEvent.Renamed {
        private final String oldPath;

        private Renamed(String oldPath) {
            this.oldPath = oldPath;
        }
        
        @Override
        public String getOldPath() {
            return oldPath;
        }
    }
}
