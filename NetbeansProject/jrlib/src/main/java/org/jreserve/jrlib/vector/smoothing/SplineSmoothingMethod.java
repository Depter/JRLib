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

import org.jreserve.jrlib.splines.SplineCurve;
import org.jreserve.jrlib.splines.Splines;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SplineSmoothingMethod implements VectorSmoothingMethod {

    private final double lambda;
    
    public SplineSmoothingMethod(double lambda) {
        this.lambda = lambda;
    }
    
    @Override
    public void smooth(double[] input) {
        int length = input.length;
        if(length > 2) {
            SplineCurve sc = Splines.smoothSpline(input, lambda);
            for(int i=0; i<length; i++) {
                double x = i+1;
                input[i] = sc.valueAt(x);
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("SplineSmoothingMethod [lambda=%f]", lambda);
    }
}
