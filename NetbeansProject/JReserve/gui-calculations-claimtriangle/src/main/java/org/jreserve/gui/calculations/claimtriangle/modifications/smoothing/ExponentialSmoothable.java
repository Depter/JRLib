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
package org.jreserve.gui.calculations.claimtriangle.modifications.smoothing;

import java.util.List;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.smoothing.AbstractSmoothDialog;
import org.jreserve.gui.calculations.api.smoothing.AbstractSmoothable;
import org.jreserve.gui.calculations.api.smoothing.Smoothable;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Smoothable.Registration(
    category = ClaimTriangleCalculationImpl.CATEGORY,
    displayName = "#LBL.ExponentialSmoothable.Name",
    position = 100
)
@Messages({
    "LBL.ExponentialSmoothable.Name=Exponential Smoothing"
})
public class ExponentialSmoothable extends AbstractSmoothable<ClaimTriangle> {

    private final static int MIN_CELL_COUNT = 2;
    
    @Override
    protected ClaimTriangleCalculationImpl getCalculation(Lookup context) {
        return context.lookup(ClaimTriangleCalculationImpl.class);
    }

    @Override
    protected int getMinCellCount() {
        return MIN_CELL_COUNT;
    }

    @Override
    protected boolean handlesVertical() {
        return true;
    }

    @Override
    protected boolean handlesHorizontal() {
        return false;
    }

    @Override
    protected CalculationModifier<ClaimTriangle> createSmoothing(Lookup context) {
        ClaimTriangleCalculationImpl calc = getCalculation(context);
        TriangleGeometry geometry = calc.getGeometry();
        
        TriangleSelection selection = context.lookup(TriangleSelection.class);
        List<Cell> cells = selection.getCells();
        Triangle triangle = selection.getTriangle();
        
        ExponentialSmoothingDialogController controller = new ExponentialSmoothingDialogController(triangle, geometry, cells);
        return AbstractSmoothDialog.createModifier(controller);
    }
}
