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
package org.jreserve.jrlib.util.filter;

import org.jreserve.jrlib.util.MathUtil;

/**
 * A SigmaFilter filters the elements of it's input based on their
 * distance from their mean. The distance is measured relative to the
 * standard deviation of the input values (the treshold).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SigmaFilter implements Filter {
    
    public final static double DEFAULT_TRESHOLD = 1.5;
    
    private double treshold;
    
    /**
     * Creates a filter, with the treshold set to 
     * {@link #DEFAULT_TRESHOLD default}.
     */
    public SigmaFilter() {
        this(DEFAULT_TRESHOLD);
    }
    
    /**
     * Creates a filter, with the given treshold. A value
     * less then 0 will be set to 0.
     * 
     * @throws IllegalArgumentException if `treshold` is `NaN`.
     */
    public SigmaFilter(double treshold) {
        initTreshold(treshold);
    }
    
    private void initTreshold(double treshold) {
        if(Double.isNaN(treshold))
            throw new IllegalArgumentException("Treshold can not be NaN!");
        this.treshold = (treshold<0d)? 0d : treshold;
    }
    
    /**
     * Returns the treshold used by the filter.
     */
    public double getTreshold() {
        return treshold;
    }
    
    /**
     * Sets the treshold, used by this filter. A value
     * less then 0 will be set to 0.
     * 
     * @throws IllegalArgumentException if `treshold` is `NaN`.
     */
    public void setTreshold(double treshold) {
        initTreshold(treshold);
    }
    
    /**
     * Filters the given input. Returns a boolean array with the 
     * same length as `x`. If the mean or the standard deviation
     * of `x` can not be calculated, then all elements of the
     * returned array will be `false`.
     * 
     * An element `x[i]` is filtered if:
     * -   x[i] is `NaN`.
     * -   `|x[i] - mean(x)| > treshold * sd(x)`, where sd is the standard
     *    deviation of x.
     * 
     * @throws NullPointerException if `x` is `null`.
     */
    public boolean[] filter(double[] x) {
        int size = x.length;
        boolean[] result = new boolean[size];
        
        double mean = MathUtil.mean(x);
        if(Double.isNaN(mean))
            return result;
        
        double sigma = MathUtil.standardDeviation(x, mean);
        if(Double.isNaN(sigma))
            return result;
        
        double bound = treshold * sigma;
        for(int i=0; i<size; i++)
            result[i] = isOutlier(x[i], mean, bound);
        return result;
    }
    
    private boolean isOutlier(double v, double mean, double bound) {
        if(Double.isNaN(v))
            return false;
        v -= mean;
        return (v < -bound) || (v > bound);
    }
    
    @Override
    public String toString() {
        return String.format("SigmaFilter [%f]", treshold);
    }
}
