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
package org.jreserve.gui.calculations.claimtriangle.modifications;

import java.util.List;
import java.util.concurrent.Callable;
import org.jreserve.gui.calculations.api.edit.Excludeable;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.jrlib.triangle.Cell;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ClaimTriangleExcludeable.Progress.Title=Excluding cells"
})
public class ClaimTriangleExcludeable implements Excludeable {

    @Override
    public boolean canExclude(Lookup context) {
        TriangleSelection selection = context.lookup(TriangleSelection.class);
        if(selection == null || selection.getCellCount() == 0)
            return false;
        
        return context.lookup(ClaimTriangleCalculationImpl.class) != null;
    }

    @Override
    public void exclude(Lookup context) {
        ExcludeTask task = new ExcludeTask(context);
        String title = Bundle.LBL_ClaimTriangleExcludeable_Progress_Title();
        TaskUtil.execute(task, null, title);
    }
    
    private static class ExcludeTask implements Callable<Void> {
        
        private final ClaimTriangleCalculationImpl calculation;
        private final List<Cell> cells;
        
        private ExcludeTask(Lookup context) {
            calculation = context.lookup(ClaimTriangleCalculationImpl.class);
            cells = context.lookup(TriangleSelection.class).getCells();
        }

        @Override
        public Void call() throws Exception {
            synchronized(calculation) {
                for(Cell cell : cells) {
                    int accident = cell.getAccident();
                    int development = cell.getDevelopment();
                    calculation.addModification(new ClaimTriangleExcludeModifier(accident, development));
                }
            }
            return null;
        }
    }
    
}
