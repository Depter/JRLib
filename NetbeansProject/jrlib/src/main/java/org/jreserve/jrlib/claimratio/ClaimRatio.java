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
package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.vector.Vector;

/**
 * ClaimRatio's represent the estimated ratio between the values of two claim 
 * triangles within given development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatio extends Vector, MutableSource<RatioTriangle> {
    
    /**
     * Returns the ratio triangle used to calculate the expected ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the input used to calculate the ratio triangle.
     */
    public RatioTriangleInput getSourceRatioTriangleInput();
    
    /**
     * Returns the triangle used as the numerator.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle used as the denominator.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}