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
    "LBL.CalculationEventUtil2.Renamed=Renamed from ''{0}'' to ''{1}''.",
    "# {0} - modification",
    "LBL.CalculationEventUtil2.Modification.Added=Modification added. {0}.",
    "# {0} - modification",
    "LBL.CalculationEventUtil2.Modification.Deleted=Modification deleted. {0}.",
    "# {0} - modification",
    "LBL.CalculationEventUtil2.Modification.Changed=Modification changed. {0}."
})
public class CalculationEventUtil {

    private final AbstractCalculationProvider calculation;
    private final Queue<AbstractAuditEvent> auditCache = new LinkedList<AbstractAuditEvent>();
    
    CalculationEventUtil(AbstractCalculationProvider calculation) {
        this.calculation = calculation;
    }
    
    public void fireCreated() {
        fireEvent(new Created());
        fireEvent(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Created()));
    }
    
    public void fireEvent(Object evt) {
        EventBusManager.getDefault().publish(evt);
    }
    
    public void fireDeleted() {
        fireEvent(new Deleted());
        fireEvent(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Deleted()));
    }
    
    public void fireRenamed(String oldPath) {
        fireEvent(new Renamed(oldPath));
        String path = calculation.getPath();
        fireEvent(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Renamed(oldPath, path)));
    }
    
    public void fireChange() {
        fireEvent(new Changed());
    }
    
    public void fireChange(String change) {
        fireEvent(new Changed());
        auditCache.add(new AbstractAuditEvent(calculation, change));
    }
    
    public void fireModificationAdded(CalculationModifier modifier) {
        fireEvent(new ModificationAdded(modifier));
        String description = modifier.getDescription();
        auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Added(description)));
    }
    
    public void fireModificationDeleted(CalculationModifier modifier) {
        fireEvent(new ModificationDeleted(modifier));
        String description = modifier.getDescription();
        auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Deleted(description)));
    }
    
    public void fireModificationChanged(CalculationModifier modifier) {
        fireEvent(new ModificationChanged(modifier));
        String description = modifier.getDescription();
        auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Changed(description)));
    }
    
    public void clearAuditCache() {
        auditCache.clear();
    }
    
    public void flushAuditCache() {
        AbstractAuditEvent evt;
        while((evt = auditCache.poll()) != null)
            fireEvent(evt);
    }
    
    public void fireSave() {
        fireEvent(new Saved());
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
    private class Saved extends Event implements CalculationEvent.Saved {}
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
    
    private class ModificationChange extends Event implements CalculationEvent.ModificationChange {
        
        private final CalculationModifier modifier;

        private ModificationChange(CalculationModifier modifier) {
            this.modifier = modifier;
        }
        
        @Override
        public CalculationModifier getModifier() {
            return modifier;
        }
    }
    
    private class ModificationAdded extends ModificationChange implements CalculationEvent.ModificationAdded {
        private ModificationAdded(CalculationModifier modifier) {
            super(modifier);
        }
    }
    
    private class ModificationDeleted extends ModificationChange implements CalculationEvent.ModificationDeleted {
        private ModificationDeleted(CalculationModifier modifier) {
            super(modifier);
        }
    }
    
    private class ModificationChanged extends ModificationChange implements CalculationEvent.ModificationChanged {
        private ModificationChanged(CalculationModifier modifier) {
            super(modifier);
        }
    }
}
