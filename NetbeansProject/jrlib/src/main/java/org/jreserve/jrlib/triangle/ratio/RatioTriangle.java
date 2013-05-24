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
package org.jreserve.jrlib.triangle.ratio;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * A RatioTriangle calculates the ratios from it's source 
 * numerator and denominator triangles. If the dimensions of the two 
 * input triangles does not match, than implementations should represent
 * an empty triangle (no accident periods, and development periods).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioTriangle extends Triangle, MutableSource<RatioTriangleInput> {

    /**
     * Returns the bundle used as input.
     */
    public RatioTriangleInput getSourceInput();
    
    /**
     * Returns the triangle containing the numerator claims.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle containing the denumerator claims.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
