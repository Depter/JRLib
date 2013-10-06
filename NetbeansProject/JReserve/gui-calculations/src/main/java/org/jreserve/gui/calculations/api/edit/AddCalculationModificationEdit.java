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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - description",
    "MSG.AddCalculationModificationEdit.Add=Add: {0}",
    "# {0} - description",
    "MSG.AddCalculationModificationEdit.Remove=Remove: {0}"
})
public class AddCalculationModificationEdit<C extends ModifiableCalculationProvider, M extends CalculationModifier>
    extends AbstractCalculationModificationEdit<C, M> {

    private int originalIndex;
    
    public AddCalculationModificationEdit(C calculation, M modifier) {
        super(calculation, modifier);
        originalIndex = super.indexOf();
    }

    @Override
    public void undo() throws CannotUndoException {
        synchronized(calculation) {
            calculation.deleteModification(originalIndex);
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        synchronized(calculation) {
            if(originalIndex == calculation.getModificationCount())
                calculation.addModification(modifier);
            else
                calculation.setModification(originalIndex, modifier);
        }
    }

    @Override
    public String getUndoPresentationName() {
        return Bundle.MSG_AddCalculationModificationEdit_Remove(getPresentationName());
    }

    @Override
    public String getRedoPresentationName() {
        return Bundle.MSG_AddCalculationModificationEdit_Add(getPresentationName());
    }
}
