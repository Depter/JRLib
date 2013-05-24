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

import org.jreserve.jrlib.vector.smoothing.ArithmeticMovingAverageMethod;

/**
 * Smoothes the input by replacing the values by
 * their arithmetic moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 *      s[i] = sum(x[i-l+1], ..., x[l])/l , for i %gt;= l-1, and
 *      s[i] = x[i]                       , for i &lt; l-1
 * where `x[i]` is the input value at the ith position.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ArithmeticMovingAverage extends AbstractVectorSmoothing {
    
    /**
     * Creates an instance for the given cells and length for the moving 
     * average.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public ArithmeticMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells, new ArithmeticMovingAverageMethod(maLength));
    }
}
