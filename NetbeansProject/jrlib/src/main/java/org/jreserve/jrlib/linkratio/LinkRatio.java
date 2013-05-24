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
package org.jreserve.jrlib.linkratio;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * Link ratios are the estimates for the development factors between
 * development period <i>d</> and <i>d+1</i>.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatio extends Vector, MutableSource<FactorTriangle> {
    
    /**
     * Returns the development factors, used for the calculation.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the claim triangle, which is the input for this calculation.
     */
    public ClaimTriangle getSourceTriangle();

    /**
     * Returns Mack's alpha parameter for the given development period. If
     * the given index falls outside of the bounds of this vector, then 
     * Double.NaN is returned.
     * 
     * <p>See {@link LinkRatioMethod#getMackAlpha() LinkRatioMethod}.</p>
     */
    public double getMackAlpha(int development);

    /**
     * Returns the weight for the development factor at the given location. If
     * the given index falls outside of the bounds of the source triangle, then 
     * Double.NaN is returned.
     * 
     * <p>See {@link LinkRatioMethod#getWeight(int, int) LinkRatioMethod.getWeight()}.
     */
    public double getWeight(int accident, int development);
}
