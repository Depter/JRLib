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
package org.jreserve.gui.calculations.api.smoothing;

import java.util.List;
import java.util.concurrent.Callable;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.Cell;
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
public abstract class AbstractSmoothable<C extends CalculationData> implements Smoothable {

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
    
    protected abstract ModifiableCalculationProvider<C> getCalculation(Lookup context);
    
    protected abstract int getMinCellCount();
    
    protected abstract boolean handlesVertical();
    
    protected abstract boolean handlesHorizontal();
    
    @Override
    public void smooth(Lookup context) {
        ModifiableCalculationProvider<C> calculation = getCalculation(context);
        
        CalculationModifier<C> modifier = createSmoothing(context);
        if(modifier != null) {
            AddTask task = new AddTask(calculation, modifier);
            String title = Bundle.MSG_AbstractSmoothable_PH_Title();
            TaskUtil.execute(task, null, title);
        }
    }
    
    protected abstract CalculationModifier<C> createSmoothing(Lookup context);

    
    private class AddTask implements Callable<Void> {
        
        private final ModifiableCalculationProvider<C> calc;
        private final CalculationModifier<C> mod;
        
        private AddTask(ModifiableCalculationProvider<C> calc, CalculationModifier<C> mod) {
            this.calc = calc;
            this.mod = mod;
        }
        
        @Override
        public Void call() throws Exception {
            synchronized(calc) {
                calc.addModification(mod);
                return null;
            }
        }
    
    }
}
