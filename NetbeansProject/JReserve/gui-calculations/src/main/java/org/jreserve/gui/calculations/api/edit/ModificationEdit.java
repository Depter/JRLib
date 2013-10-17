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

import java.util.HashMap;
import java.util.Map;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.jreserve.gui.calculations.api.EditableCalculationModifier;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - description",
    "MSG.ModificationEdit.Undo=Undo change: {0}",
    "# {0} - description",
    "MSG.ModificationEdit.Redo=Re change: {0}"
})
class ModificationEdit implements UndoableEdit {

    private EditableCalculationModifier modifier;
    private Map preState;
    private boolean undone = false;
    
    public ModificationEdit(EditableCalculationModifier modifier, Map preState) {
        this.modifier = modifier;
        this.preState = preState;
    }
    
    EditableCalculationModifier getModifier() {
        return modifier;
    }
    
    @Override
    public boolean canUndo() {
        return !undone;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        if(undone || modifier == null)
            throw new CannotUndoException();
        switchState(modifier);
    }
    
    private void switchState(final EditableCalculationModifier ecm) {
        synchronized(ecm) {
            Map<Object, Object> state = new HashMap<Object, Object>(preState.size());
            ecm.getState(state);
            ecm.loadState(preState);
            preState = state;
        }
    }

    @Override
    public boolean canRedo() {
        return undone;
    }

    @Override
    public void redo() throws CannotRedoException {
        if(!undone || modifier == null)
            throw new CannotRedoException();
        switchState(modifier);
    }

    @Override
    public void die() {
        modifier = null;
        preState = null;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return modifier.getDescription();
    }

    @Override
    public String getUndoPresentationName() {
        String desc = modifier==null? "" : modifier.getDescription();
        return Bundle.MSG_ModificationEdit_Undo(desc);
    }

    @Override
    public String getRedoPresentationName() {
        String desc = modifier==null? "" : modifier.getDescription();
        return Bundle.MSG_ModificationEdit_Redo(desc);
    }    
}
