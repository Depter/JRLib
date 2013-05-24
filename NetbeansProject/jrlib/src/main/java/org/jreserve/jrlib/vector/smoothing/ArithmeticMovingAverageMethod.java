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
 * Smoothes the input by replacing the values by
 * their arithmetic moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 * <pre>
 *      s[i] = sum(x[i-l+1], ..., x[l])/l , for i %gt;= l-1, and
 *      s[i] = x[i]                       , for i &lt; l-1
 * </pre>
 * where `x[i]` is the input value at the ith position.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ArithmeticMovingAverageMethod extends AbstractMovingAverageMethod {
    
    /**
     * Creates an instance for the given length for the moving 
     * average.
     * 
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public ArithmeticMovingAverageMethod(int maLength) {
        super(maLength);
    }
    
    @Override
    protected double mean(double[] input, int index) {
        double sum = 0d;
        for(int i=index-maLength+1; i<=index; i++)
            sum += input[i];
        return sum/(double)maLength;
    }
}
