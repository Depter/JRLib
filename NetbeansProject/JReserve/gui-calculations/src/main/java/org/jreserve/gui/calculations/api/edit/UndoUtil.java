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

import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.CalculationModifier;
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

    private UndoRedo.Manager manager;
    private ModifiableCalculationProvider<C> calculation;

    public UndoUtil(UndoRedo.Manager manager, ModifiableCalculationProvider<C> calculation) {
        this.manager = manager;
        this.calculation = calculation;
        EventBusManager.getDefault().subscribe(this);
    }
    
    public void setModification(int index, CalculationModifier<C> modifier) {
        if(index == calculation.getModificationCount()) {
            addModification(modifier);
        } else {
            CalculationModifier<C> original = calculation.getModificationAt(index);
            calculation.setModification(index, original);
            addEdit(new SetCalculationModificationEdit(index, calculation, original, modifier));
        }
    }
    
    public void addEdit(final UndoableEdit edit) {
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
    
    public void addModification(CalculationModifier<C> modifier) {
        calculation.addModification(modifier);
        addEdit(new AddCalculationModificationEdit(calculation, modifier));
    }
    
    public void addModification(int index, CalculationModifier<C> modifier) {
        calculation.addModification(index, modifier);
        addEdit(new AddCalculationModificationEdit(calculation, modifier));
    }
    
    public void deleteModification(CalculationModifier<C> modifier) {
        int index = indexOf(modifier);
        if(index < 0)
            throw new IllegalArgumentException("Modification is not added to the calculation!");
        calculation.deleteModification(index);
        addEdit(new DeleteCalculationModificationEdit(calculation, modifier, index));
    }
    
    private int indexOf(CalculationModifier<C> modifier) {
        int size = calculation.getModificationCount();
        for(int i=0; i<size; i++)
            if(calculation.getModificationAt(i) == modifier)
                return i;
        return -1;
    }
    
    @EventBusListener(forceEDT = true)
    public void calculationSaved(CalculationEvent.Saved evt) {
        if(this.calculation == evt.getCalculationProvider())
            manager.discardAllEdits();
    }
}
