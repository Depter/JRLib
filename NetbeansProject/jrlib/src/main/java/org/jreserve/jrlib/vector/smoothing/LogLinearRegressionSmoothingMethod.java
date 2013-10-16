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

import java.util.Arrays;
import org.jreserve.jrlib.util.RegressionUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LogLinearRegressionSmoothingMethod implements VectorSmoothingMethod {

    private final boolean hasIntercept;

    public LogLinearRegressionSmoothingMethod(boolean hasIntercept) {
        this.hasIntercept = hasIntercept;
    }
    
    public void smooth(double[] input) {
        int length = input.length;
        if(length < 2)
            return;
        
        if(!getLogarithm(input, length)) {
            Arrays.fill(input, Double.NaN);
            return;
        }
        
        double[] params = RegressionUtil.linearRegression(input, hasIntercept);
        double intercept = params[RegressionUtil.INTERCEPT];
        double slope = params[RegressionUtil.SLOPE];
        
        for(int i=0; i<length; i++) {
            double x = i+1;
            input[i] = Math.exp(intercept + slope * x);
        }
    }
    
    private boolean getLogarithm(double[] input, int length) {
        for(int i=0; i<length; i++) {
            double v = input[i];
            if(v < 0d)
                return false;
            else
                input[i] = Math.log(v);
        }
        return true;
    }
}
