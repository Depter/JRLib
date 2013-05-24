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
package org.jreserve.jrlib.util;

import static java.lang.Math.*;

/**
 * Utility class for the normal distribution.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class NormalUtil {
    
    private final static double sqrt2PI = sqrt(2d * PI);
    private final static double DELTA = 0.0000000001;
    private final static double LOWER_BOUND = -8d;
    private final static double UPPER_BOUND = 8d;
    
    /**
     * Calculates the value of the standard normal density function at x.
     */
    public static double normDF(double x) {
        return exp(-x * x / 2d) / sqrt2PI;
    }
    
    /**
     * Calculates the value of the normal density function at x for the
     * distribution `N(mean, sigma)`.
     */
    public static double normDF(double x, double mean, double sigma) {
        return normDF((x - mean) / sigma) / sigma;
    }
    
    public static double normCDF(double x) {
        if(x < LOWER_BOUND) return 0d;
        if(x > UPPER_BOUND) return 1d;
        double s = x;
        double t = 0;
        double b = x;
        double q = x * x;
        double i=1;
        while(s != t)  {
            t = s;
            i += 2d;
            b *= (q/i);
            s = t+b;
        }
        return 0.5 + s * exp(-0.5 * q - 0.91893853320467274178);
    }
    
    public static double normCDF(double x, double mean, double sigma) {
        return normCDF((x-mean) / sigma);
    }
    
    public static double invNormCDF(double x) {
        return invNormCDF(x, DELTA, LOWER_BOUND, UPPER_BOUND);
    }
    
    public static double invNormCDF(double x, double delta, double min, double max) {
        double middle = min + (max - min) / 2d;
        if(abs(max - min) < delta)
            return middle;
        return normCDF(middle) > x? 
                invNormCDF(x, delta, min, middle) :
                invNormCDF(x, delta, middle, max);
    }
    
    public static double invNormCDF(double x, double mean, double sigma) {
        double result = invNormCDF(x);
        return result * sigma + mean;
    }
    
    private NormalUtil() {
    }
}
