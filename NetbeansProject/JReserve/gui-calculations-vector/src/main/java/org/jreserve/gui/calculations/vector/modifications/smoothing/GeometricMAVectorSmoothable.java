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
package org.jreserve.gui.calculations.vector.modifications.smoothing;

import java.util.List;
import org.jreserve.gui.calculations.smoothing.Smoothable;
import org.jreserve.gui.calculations.smoothing.calculation.AbstractMASmoothable;
import org.jreserve.gui.calculations.smoothing.calculation.MASmoothingDialogController;
import org.jreserve.gui.calculations.smoothing.dialog.SmoothDialogController;
import org.jreserve.gui.calculations.vector.VectorCalculation;
import org.jreserve.gui.calculations.vector.impl.VectorCalculationImpl;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.AbstractVectorSmoothing;
import org.jreserve.jrlib.triangle.smoothing.GeometricMovingAverage;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.vector.Vector;
import org.jreserve.jrlib.vector.VectorTriangle;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Smoothable.Registration(
    category = VectorCalculationImpl.CATEGORY,
    displayName = "#LBL.GeometricMAVectorSmoothable.Name",
    position = 400
)
@Messages({
    "LBL.GeometricMAVectorSmoothable.Name=Geometric MA"
})
public class GeometricMAVectorSmoothable extends AbstractMASmoothable<Vector> {
    
    @Override
    protected VectorCalculation getCalculation(Lookup context) {
        return context.lookup(VectorCalculation.class);
    }

    @Override
    protected SmoothDialogController<Vector> createController(Lookup context) {
        VectorCalculation calc = getCalculation(context);
        TriangleGeometry geometry = calc.getGeometry();
        
        TriangleSelection selection = context.lookup(TriangleSelection.class);
        List<Cell> cells = selection.getCells();
        Triangle triangle = selection.getTriangle();
        Vector vector = ((VectorTriangle)triangle).getSource();
        
        return new DialogController(vector, geometry, cells);
    }
    
    static class DialogController extends MASmoothingDialogController<Vector> {

        public DialogController(Vector vector, TriangleGeometry geometry, List<Cell> cells) {
            super(vector, geometry, cells, Bundle.LBL_GeometricMAVectorSmoothable_Name());
        }
        
        @Override
        public GeometricMAModifier createModifier() {
            List<SmoothingCell> cells = createCells();
            int length = getLength();
            return new GeometricMAModifier(cells, length);
        }

        @Override
        protected AbstractVectorSmoothing createSmoothing() {
            return new GeometricMovingAverage(createCellsArr(), getLength());
        }
    }
}
