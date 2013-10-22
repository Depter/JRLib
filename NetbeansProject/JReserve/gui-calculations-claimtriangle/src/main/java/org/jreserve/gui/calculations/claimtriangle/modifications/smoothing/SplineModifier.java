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

import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.calculations.api.ModifiableCalculationProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.claimtriangle.modifications.smoothing.SplineClaimTriangleSmoothable.DialogController;
import org.jreserve.gui.calculations.smoothing.calculation.SplineSmoothingModifier;
import org.jreserve.gui.calculations.smoothing.dialog.AbstractSmoothDialog;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.SmoothedClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SplineModifier
    extends SplineSmoothingModifier<ClaimTriangle>
    implements ClaimTriangleModifier {

    public SplineModifier(List<SmoothingCell> cells, double lambda) {
        super(cells, ClaimTriangle.class, lambda);
    }
    
    @Override
    public ClaimTriangle createCalculation(ClaimTriangle sourceCalculation) {
        return new SmoothedClaimTriangle(sourceCalculation, createSmoothing());        
    }

    @Override
    public List<Cell> getAffectedCells() {
        return new ArrayList<Cell>(getCells());
    }
    
    
    @Override
    public void edit(ModifiableCalculationProvider<ClaimTriangle> calculation) {
        DialogController controller = createController(calculation);
        controller.setLambda(super.getLambda());
        controller.setAllowsModifyCells(false);
        SplineModifier em = (SplineModifier) AbstractSmoothDialog.createModifier(controller);
        
        if(em != null)
            super.updateFrom(em);
    }
    
    private DialogController createController(ModifiableCalculationProvider<ClaimTriangle> calculation) {
        ClaimTriangle source = super.getSource(calculation);
        TriangleGeometry geometry = ((ClaimTriangleCalculationImpl) calculation).getGeometry();
        List<Cell> cells = getAffectedCells();
        return new DialogController(source, geometry, cells);
    }
}
