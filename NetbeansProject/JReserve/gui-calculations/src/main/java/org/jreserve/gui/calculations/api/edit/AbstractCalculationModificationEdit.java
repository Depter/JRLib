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

import javax.swing.undo.UndoableEdit;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import org.jreserve.gui.calculations.api.modification.ModifiableCalculationProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractCalculationModificationEdit<C extends ModifiableCalculationProvider, M extends CalculationModifier> implements UndoableEdit {

    protected final C calculation;
    protected final M modifier;
    protected final int index;
    
    AbstractCalculationModificationEdit(C calculation, int index, M modifier) {
        this.index = index;
        this.calculation = calculation;
        this.modifier = modifier;
    }
    
    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {
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
}
