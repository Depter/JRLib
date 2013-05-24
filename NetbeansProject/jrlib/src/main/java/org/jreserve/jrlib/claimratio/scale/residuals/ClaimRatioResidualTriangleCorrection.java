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
package org.jreserve.jrlib.claimratio.scale.residuals;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioResidualTriangleCorrection 
    extends TriangleCorrection<CRResidualTriangle> 
    implements ModifiedCRResidualTriangle {

    public ClaimRatioResidualTriangleCorrection(CRResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    public ClaimRatioResidualTriangleCorrection(CRResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    public CRResidualTriangle getSourceResidualTriangle() {
        return source;
    }

    public ClaimRatioScale getSourceClaimRatioScales() {
        return source.getSourceClaimRatioScales();
    }

    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }

    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }

    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
}
