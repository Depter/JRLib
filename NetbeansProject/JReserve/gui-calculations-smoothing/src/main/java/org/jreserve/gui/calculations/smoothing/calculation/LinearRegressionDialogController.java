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
package org.jreserve.gui.calculations.smoothing.calculation;

import java.util.List;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.smoothing.LinearRegressionSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.LinearRegressionDialogController.Title=Linear Regression"
})
public abstract class LinearRegressionDialogController<C extends CalculationData> 
    extends AbstractRegressionDialogController<C> {
    
    public LinearRegressionDialogController(C data, TriangleGeometry geometry, List<Cell> cells) {
        super(Bundle.LBL_LinearRegressionDialogController_Title(), data, geometry, cells);
    }
    
    @Override
    protected LinearRegressionSmoothing createSmoothing() {
        List<SmoothingCell> cells = createCells();
        SmoothingCell[] arr = cells.toArray(new SmoothingCell[cells.size()]);
        return new LinearRegressionSmoothing(arr, hasIntercept());
    }
}
