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

import java.util.Collections;
import java.util.List;
import org.jreserve.gui.calculations.api.modification.TriangleCorrectionModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleModifier;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangleCorrection;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "LBL.ClaimTriangleCorrectionModifier.Description=Correction [{0}; {1}] = {2}",
    "LBL.ClaimTriangleCorrectionModifier.Name=Correction"
})
public class ClaimTriangleCorrectionModifier 
    extends TriangleCorrectionModifier<ClaimTriangle> implements ClaimTriangleModifier {

    public ClaimTriangleCorrectionModifier(int accident, int development, double value) {
        super(ClaimTriangle.class, accident, development, value);
    }
    
    @Override
    public ClaimTriangle createCalculation(ClaimTriangle sourceCalculation) {
        return new ClaimTriangleCorrection(
                sourceCalculation, 
                getAccident(), getDevelopment(), 
                getValue()
                );
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ClaimTriangleCorrectionModifier_Description(
                getAccident()+1, getDevelopment()+1, 
                getValue());
    }

    @Override
    public List<Cell> getAffectedCells() {
        return Collections.singletonList(
                new Cell(getAccident(), getDevelopment()));
    }

    @Override
    protected String getDisplayName() {
        return Bundle.LBL_ClaimTriangleCorrectionModifier_Name();
    }

    @Override
    protected boolean isEditInputCummulated() {
        return true;
    }
}
