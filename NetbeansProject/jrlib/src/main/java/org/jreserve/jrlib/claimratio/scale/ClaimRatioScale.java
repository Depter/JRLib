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
package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

/**
 * The class represents the calculation of the rho parameters for the
 * Munich Chain-Ladder method.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioScale extends Scale<ClaimRatioScaleInput>  {
    
    /**
     * Returns the claim-ratios used as input for the calculation.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Returns the ratio-triangle which is used to calculate the claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the input used to calculate the ratio triangle.
     */
    public RatioTriangleInput getSourceRatioTriangleInput();
    
    /**
     * Returns the triangle used as numerator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle used as denominator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
