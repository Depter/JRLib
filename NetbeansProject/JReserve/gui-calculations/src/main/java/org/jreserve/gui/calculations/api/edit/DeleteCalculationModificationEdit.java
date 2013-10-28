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
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import org.jreserve.gui.calculations.api.modification.ModifiableCalculationProvider;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - description",
    "MSG.DeleteCalculationModificationEdit.Add=Add: {0}",
    "# {0} - description",
    "MSG.DeleteCalculationModificationEdit.Remove=Remove: {0}"
})
class DeleteCalculationModificationEdit<C extends ModifiableCalculationProvider, M extends CalculationModifier>
    extends AbstractCalculationModificationEdit<C, M> {

    DeleteCalculationModificationEdit(C calculation, int index, M modifier) {
        super(calculation, index, modifier);
    }
    
    @Override
    public void undo() throws CannotUndoException {
        synchronized(calculation) {
            calculation.addModification(index, modifier);
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        synchronized(calculation) {
            calculation.deleteModification(index);
        }
    }

    @Override
    public String getUndoPresentationName() {
        return Bundle.MSG_DeleteCalculationModificationEdit_Add(getPresentationName());
    }

    @Override
    public String getRedoPresentationName() {
        return Bundle.MSG_DeleteCalculationModificationEdit_Remove(getPresentationName());
    }

}
