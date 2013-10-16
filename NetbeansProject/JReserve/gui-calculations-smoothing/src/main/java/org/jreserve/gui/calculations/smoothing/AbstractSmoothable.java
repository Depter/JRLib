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
package org.jreserve.gui.calculations.smoothing;

import java.util.concurrent.Callable;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.edit.UndoUtil;
import org.jreserve.gui.calculations.smoothing.dialog.AbstractSmoothDialog;
import org.jreserve.gui.calculations.smoothing.dialog.SmoothDialogController;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.AbstractSmoothable.PH.Title=Apply smoothing"
})
public abstract class AbstractSmoothable<T extends Triangle> implements Smoothable {

    @Override
    public boolean canSmooth(Lookup context) {
        if(getCalculation(context) == null)
            return false;
        
        TriangleSelection selection = context.lookup(TriangleSelection.class);
        if(selection == null)
            return false;
        
        if(selection.getCellCount() < getMinCellCount())
            return false;
        
        boolean horizontal = true;
        boolean vertical = true;
        Cell prev = null;
        for(Cell cell : selection.getCells()) {
            if(prev != null) {
                if(horizontal)
                    if(cell.getDevelopment() - prev.getDevelopment() != 1)
                        horizontal = false;
                if(vertical)
                    if(cell.getAccident() - prev.getAccident() != 1)
                        vertical = false;
            }
            prev = cell;
        }
        
        if(vertical && handlesVertical())
            return true;
        if(horizontal && handlesHorizontal())
            return true;
        return false;
    }
    
    protected abstract ModifiableCalculationProvider<T> getCalculation(Lookup context);
    
    protected abstract int getMinCellCount();
    
    protected boolean handlesVertical() {
        return true;
    }
    
    protected boolean handlesHorizontal() {
        return false;
    }
    
    @Override
    public void smooth(Lookup context) {
        ModifiableCalculationProvider<T> calculation = getCalculation(context);
        
        UndoUtil<T> undoUtil = context.lookup(UndoUtil.class);
        CalculationModifier<T> modifier = createSmoothing(context);
        if(modifier != null && (calculation != null || undoUtil != null)) {
            AddTask task = new AddTask(calculation, modifier, undoUtil);
            String title = Bundle.MSG_AbstractSmoothable_PH_Title();
            TaskUtil.execute(task, null, title);
        }
    }
    
    protected CalculationModifier<T> createSmoothing(Lookup context) {
        SmoothDialogController<T> controller = createController(context);
        return AbstractSmoothDialog.createModifier(controller);
    }

    protected abstract SmoothDialogController<T> createController(Lookup context);
    
    private class AddTask implements Callable<Void> {
        
        private final ModifiableCalculationProvider<T> calc;
        private final CalculationModifier<T> mod;
        private final UndoUtil<T> undoUtil;
        
        private AddTask(ModifiableCalculationProvider<T> calc, CalculationModifier<T> mod, UndoUtil<T> undoUtil) {
            this.calc = calc;
            this.mod = mod;
            this.undoUtil = undoUtil;
        }
        
        @Override
        public Void call() throws Exception {
            if(undoUtil != null) {
                undoUtil.addModification(mod);
            } else {
                calc.addModification(mod);
            }
            return null;
        }
    
    }
}
