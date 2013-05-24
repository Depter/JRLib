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
package org.jreserve.jrlib.vector.smoothing;

/**
 * Smoothes the values using exponential smoothing.
 * 
 * The class creates the smoothed values `s[i]` from input values
 * `x[i]`-s as follows:
 * <pre>
 *     s[0] = x[0]
 *     s[i|i>0] = alpha * x[i] + (1-alpha) * s[i-1]
 * </pre>
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothingMethod implements VectorSmoothingMethod {
    
    private final double alpha;
    private final double alpha2;
    
    /**
     * Creates an instance for the given cells and alpha parameter.
     * 
     * @throws IllegalArgumentException if `alpha` is  not in the `[0;1]` 
     * interval.
     */
    public ExponentialSmoothingMethod(double alpha) {
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0; 1], but it was "+alpha+"!");
        this.alpha = alpha;
        this.alpha2 = 1d-alpha;
    }

    @Override
    public void smooth(double[] input) {
        int size = input.length;
        if(size < 2)
            return;
        
        double s = input[0];
        for(int i=1; i<size; i++) {
            s = alpha * input[i] + alpha2 * s;
            input[i] = s;
        }
    }
}
