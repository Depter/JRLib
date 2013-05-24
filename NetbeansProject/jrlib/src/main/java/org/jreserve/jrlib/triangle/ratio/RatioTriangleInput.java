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

import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * This class bundles two {@link ClaimTriangle ClaimTriangles} to be used
 * as input for a {@link RatioTriangle RatioTriangle}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioTriangleInput extends AbstractMultiSourceCalculationData<ClaimTriangle> {
    
    private ClaimTriangle numerator;
    private ClaimTriangle denominator;
    
    public RatioTriangleInput(ClaimTriangle numerator, ClaimTriangle denominator) {
        super(numerator, denominator);
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    /**
     * Returns the triangle containing the numerator claims.
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return numerator;
    }
    
    /**
     * Returns the triangle containing the denumerator claims.
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return denominator;
    }

    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }
}
