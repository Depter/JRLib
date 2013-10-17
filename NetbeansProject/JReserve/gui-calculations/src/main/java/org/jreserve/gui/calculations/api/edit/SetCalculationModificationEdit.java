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
    "# {0} - from",
    "# {1} - to",
    "# {2} - at",
    "MSG.SetCalculationModificationEdit.Operation=Change ''{0}'' to ''{1}'' at {2}."
})
class SetCalculationModificationEdit<C extends ModifiableCalculationProvider, M extends CalculationModifier>
    extends AbstractCalculationModificationEdit<C, M> {

    private final M original;

    SetCalculationModificationEdit(C calculation, int index, M original, M modifier) {
        super(calculation, index, modifier);
        this.original = original;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        synchronized(calculation) {
            calculation.setModification(index, original);
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        synchronized(calculation) {
            calculation.setModification(index, modifier);
        }
    }

    @Override
    public String getUndoPresentationName() {
        return getName(modifier, original);
    }
    
    private String getName(CalculationModifier from, CalculationModifier to) {
        return Bundle.MSG_SetCalculationModificationEdit_Operation(from, to, index+1);
    }

    @Override
    public String getRedoPresentationName() {
        return getName(original, modifier);
    }
}
