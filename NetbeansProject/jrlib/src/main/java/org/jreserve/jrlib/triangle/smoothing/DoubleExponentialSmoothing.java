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
package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.vector.smoothing.DoubleExponentialSmoothingMethod;

/**
 * Smoothies the values using double exponential smoothing.
 * 
 * The class creates the smoothed values `s[i]` from input values
 * `x[i]`-s as follows:
 *      b[1] = x[1] - x[0]
 *      b[i|i>1] = beta * (s[i]-x[i-1]) + (1-beta) * b[i-1]
 * 
 *      s[0] = x[0]
 *      s[i|i>0] = alpha * x[i-1] + (1-alpha)*(s[i-1] + b[i-1])
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleExponentialSmoothing extends AbstractVectorSmoothing {
    
    /**
     * Creates an instance for the given cells and parameters
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `alpha` or `beta` is  not in the 
     * `[0;1]` interval.
     */
    public DoubleExponentialSmoothing(SmoothingCell[] cells, double alpha, double beta) {
        super(cells, new DoubleExponentialSmoothingMethod(alpha, beta));
    }
}
