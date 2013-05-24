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
package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * A LinkRatioScale instance calculates the scale parameter &sigma; 
 * (sigma) for development period `d`.
 * 
 * The formula for calculating the scale parameter `s(d)` for 
 * development period `d` is:
 *                   sum(w(a,d) * (f(a,d) - lr(d))^2
 *      &sigma;(d) = -------------------------------
 *                                n - 1
 * where:
 * -   `w(a,d)` are the weights returned by {@link LinkRatio#getWeight(int, int) getWeight()}
 *     for accident period `a` and development period `d`.
 * -   `f(a,d)` are the development factors from {@link LinkRatio#getSourceFactors() getSourceFactors()}
 *     for accident period `a` and development period `d`.
 * -   `lr(d)` are the estimated ink-ratios returned by {@link LinkRatio#getValue(int) getValue()}
 *     for development period `d`.
 * -   if either `w(a,d)`, `f(a,d)` or `lr(d)` is NaN, then the cell is ingored
 * -   `n` is the number of not ignored cells in development period `d`. If n
 *     is less then 2, then `s(d)` is NaN.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends Scale<LinkRatioScaleInput> {
    
    /**
     * Returns the input that serves the data for the scale
     * calculations.
     */
    public LinkRatioScaleInput getSourceInput();
    
    /**
     * Returns the link-ratios used as input for this calculation.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the development factors used as input for 
     * the calculation of the link-ratios.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the claims used as input for the calculation of 
     * the development factors.
     */
    public ClaimTriangle getSourceTriangle();
}
