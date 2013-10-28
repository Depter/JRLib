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

import org.jreserve.gui.calculations.api.method.CalculationMethod;
import org.jreserve.gui.calculations.api.modification.EditableCalculationModifier;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import java.util.LinkedList;
import java.util.Map;
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
    "# {0} - index",
    "# {1} - modification",
    "LBL.CalculationEventUtil2.Modification.Added=Modification added at index {0}. {1}.",
    "# {0} - index",
    "# {1} - removed",
    "# {2} - added",
    "LBL.CalculationEventUtil2.Modification.Replaced=Modification added at index {0}. {1} => {2}.",
    "# {0} - index",
    "# {1} - modification",
    "LBL.CalculationEventUtil2.Modification.Deleted=Modification deleted from index {0}. {1}.",
    "# {0} - index",
    "# {1} - modification",
    "LBL.CalculationEventUtil2.Modification.Changed=Modification changed at position {0}. {1}.",
    "# {0} - index",
    "# {1} - oldMethod",
    "# {2} - newMethod",
    "LBL.CalculationEventUtil2.Method.Changed=Method at position {0} changed from ''{1}'' to ''{2}''."
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
    
    private void fireEvent(Object evt) {
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
    
    public void fireValueChanged() {
        fireEvent(new ValueChanged());
    }
    
    public void fireChange(String change) {
        fireEvent(new Changed());
        auditCache.add(new AbstractAuditEvent(calculation, change));
    }
    
    public void fireModificationAdded(int index, CalculationModifier modifier) {
        fireModificationAdded(index, modifier, null);
    }
    
    public void fireModificationAdded(int index, CalculationModifier modifier, CalculationModifier removed) {
        fireEvent(new ModificationAdded(index, modifier, removed));
        String description = modifier.getDescription();
        if(removed == null)
            auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Added(index+1, description)));
        else
            auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Replaced(index+1, removed.getDescription(), description)));
    }
    
    public void fireModificationDeleted(int index, CalculationModifier modifier) {
        fireEvent(new ModificationDeleted(index, modifier));
        String description = modifier.getDescription();
        auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Deleted(index+1, description)));
    }
    
    public void fireModificationChanged(int index, EditableCalculationModifier modifier, Map preState) {
        fireEvent(new ModificationChanged(index, modifier, preState));
        String description = modifier.getDescription();
        auditCache.add(new AbstractAuditEvent(calculation, Bundle.LBL_CalculationEventUtil2_Modification_Changed(index+1, description)));
    }

    public void fireMethodChanged(int index, CalculationMethod oldMentod, CalculationMethod newMethod) {
        String auditMsg = Bundle.LBL_CalculationEventUtil2_Method_Changed(index+1, oldMentod, newMethod);
        fireMethodChanged(index, oldMentod, auditMsg);
    }
    
    public void fireMethodChanged(int index, CalculationMethod oldMethod, String auditMsg) {
        fireEvent(new MethodChanged(index, oldMethod));
        auditCache.add(new AbstractAuditEvent(calculation, auditMsg));
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
    private class ValueChanged extends Event implements CalculationEvent.ValueChanged{}
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
        private final int index;
        
        private ModificationChange(int index, CalculationModifier modifier) {
            this.modifier = modifier;
            this.index = index;
        }
        
        @Override
        public CalculationModifier getModifier() {
            return modifier;
        }

        @Override
        public int getModifiedIndex() {
            return index;
        }
    }
    
    private class ModificationAdded extends ModificationChange implements CalculationEvent.ModificationAdded {
        
        private final CalculationModifier removed;
        
        private ModificationAdded(int index, CalculationModifier modifier, CalculationModifier removed) {
            super(index, modifier);
            this.removed = removed;
        }

        @Override
        public CalculationModifier getRemovedModifier() {
            return removed;
        }
    }
    
    private class ModificationDeleted extends ModificationChange implements CalculationEvent.ModificationDeleted {
        private ModificationDeleted(int index, CalculationModifier modifier) {
            super(index, modifier);
        }
    }
    
    private class ModificationChanged extends ModificationChange implements CalculationEvent.ModificationChanged {
        
        private final Map preState;

        private ModificationChanged(int index, EditableCalculationModifier modifier, Map preState) {
            super(index, modifier);
            this.preState = preState;
        }

        @Override
        public Map getPreState() {
            return preState;
        }

        @Override
        public EditableCalculationModifier getModifier() {
            return (EditableCalculationModifier) super.getModifier();
        }
    }
    
    private class MethodChanged extends Event implements CalculationEvent.MethodChange {
        
        private int index;
        private CalculationMethod oldMethod;

        private MethodChanged(int index, CalculationMethod oldMethod) {
            this.index = index;
            this.oldMethod = oldMethod;
        }
        
        @Override
        public int getMethodIndex() {
            return index;
        }

        @Override
        public CalculationMethod getOldMethod() {
            return oldMethod;
        }
    }
}
