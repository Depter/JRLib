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
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CRResidualTriangle extends Triangle {

    /**
     * Returns the {@link ClaimRatioScale ClaimRatioScale} used
     * to calculate the residuals.
     */
    public ClaimRatioScale getSourceClaimRatioScales();
    
    /**
     * Returns the {@link ClaimRatio ClaimRatio} used
     * to calculate the scales.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Returns the {@link RatioTriangle RatioTriangle} used
     * to calculate the claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used
     * as numerator to calculate the ratios.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used
     * as denominator to calculate the ratios.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
