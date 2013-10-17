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

package org.jreserve.gui.calculations.api.edit;

import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.EditableCalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.jrlib.CalculationData;
import org.openide.awt.UndoRedo;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UndoUtil<C extends CalculationData> {

    private final UndoRedo.Manager manager;
    private ModifiableCalculationProvider<C> calculation;

    public UndoUtil(UndoRedo.Manager manager, ModifiableCalculationProvider<C> calculation) {
        this.manager = manager;
        this.calculation = calculation;
        EventBusManager.getDefault().subscribe(this);
    }
    
    public ModifiableCalculationProvider<C> getCalculation() {
        return calculation;
    }
    
    public void setModification(int index, CalculationModifier<C> modifier) {
        CalculationModifier<C> original = calculation.getModificationAt(index);
        calculation.setModification(index, original);
        addEdit(new SetCalculationModificationEdit(calculation, index, original, modifier));
    }
//    
//    public void addEdit(final UndoableEdit edit) {
//        if(SwingUtilities.isEventDispatchThread()) {
//            UndoableEditEvent evt = new UndoableEditEvent(this, edit);
//            manager.undoableEditHappened(evt);
//        } else {
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    UndoableEditEvent evt = new UndoableEditEvent(this, edit);
//                    manager.undoableEditHappened(evt);
//                }
//            });
//        }
//    }
//    
    public void addModification(CalculationModifier<C> modifier) {
        addModification(calculation.getModificationCount(), modifier);
    }
    
    public void addModification(int index, CalculationModifier<C> modifier) {
        calculation.addModification(index, modifier);
        addEdit(new AddCalculationModificationEdit(calculation, index, modifier));
    }
    
    public void deleteModification(CalculationModifier<C> modifier) {
        int index = calculation.indexOfModification(modifier);
        if(index < 0)
            throw new IllegalArgumentException("Modification is not added to the calculation!");
        calculation.deleteModification(index);
        addEdit(new DeleteCalculationModificationEdit(calculation, index, modifier));
    }
    
    public void modificationChanged(EditableCalculationModifier<C> modifier, Map preState) {
        addEdit(new ModificationEdit(modifier, preState));
    }
    
    @EventBusListener(forceEDT = true)
    public void calculationSaved(CalculationEvent.Saved evt) {
        synchronized(manager) {
            if(this.calculation == evt.getCalculationProvider())
                manager.discardAllEdits();
        }
    }
//    
//    @EventBusListener
//    public void modificationChange(CalculationEvent.ModificationChange evt) {
//        synchronized(manager) {
//            if(this.calculation != evt.getCalculationProvider())
//                return;
//            
//            if(evt instanceof CalculationEvent.ModificationAdded) {
//                CalculationEvent.ModificationAdded e = (CalculationEvent.ModificationAdded) evt;
//                modificationAdded(e.getModifiedIndex(), e.getModifier(), e.getRemovedModifier());
//            } else if(evt instanceof CalculationEvent.ModificationDeleted) {
//                CalculationEvent.ModificationDeleted e = (CalculationEvent.ModificationDeleted) evt;
//                addEdit(new DeleteCalculationModificationEdit(calculation, e.getModifiedIndex(), e.getModifier()));
//            } else if(evt instanceof CalculationEvent.ModificationChanged) {
//                CalculationEvent.ModificationChanged e = (CalculationEvent.ModificationChanged) evt;
//                addEdit(new ModificationEdit(e.getModifier(), e.getPreState()));
//            }
//        }
//    }
//    
//    private void modificationAdded(int index, CalculationModifier added, CalculationModifier removed) {
//        if(removed == null) {
//            addEdit(new AddCalculationModificationEdit(calculation, index, added));
//        } else {
//            addEdit(new SetCalculationModificationEdit(calculation, index, added, removed));
//        }
//    }

    private void addEdit(final UndoableEdit edit) {
        if(SwingUtilities.isEventDispatchThread()) {
            UndoableEditEvent evt = new UndoableEditEvent(this, edit);
            manager.undoableEditHappened(evt);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    UndoableEditEvent evt = new UndoableEditEvent(this, edit);
                    manager.undoableEditHappened(evt);
                }
            });
        }
    }
}
