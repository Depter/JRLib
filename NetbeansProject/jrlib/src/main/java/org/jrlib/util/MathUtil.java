package org.jrlib.util;

import static java.lang.Double.isNaN;

/**
 * This class provides utility methods for mathematical calculations.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MathUtil {
    
    /**
     * Returns the highest element of `x`, NaN values are ignored.
     * 
     * @throws NullPointerException if `x` is null.
     */
    public static double max(double[] x) {
        return max(x, true);
    }
    
    /**
     * Returns the highest element of `x`. If `ignoreNaN` is false, 
     * and `x` contains a NaN value, then the result is NaN.
     * 
     * @throws NullPointerException if `x` is null.
     */
    public static double max(double[] x, boolean ignoreNaN) {
        int size = x.length;
        if(size == 0)
            return Double.NaN;
        
        double max = x[0];
        for(int i=1; i<size; i++) {
            double v = x[i];
            if(Double.isNaN(v)) {
                if(!ignoreNaN) return Double.NaN;
            } else {
                if(v > max) max = v;
            }
        }
        return max;
    }
    
    /**
     * Returns the lowest element of `x`, NaN values are ignored.
     * 
     * @throws NullPointerException if `x` is null.
     */
    public static double min(double[] x) {
        return min(x, true);
    }
    
    /**
     * Returns the lowest element of `x`. If `ignoreNaN` is false, 
     * and `x` contains a NaN value, then the result is NaN.
     * 
     * @throws NullPointerException if `x` is null.
     */
    public static double min(double[] x, boolean ignoreNaN) {
        int size = x.length;
        if(size == 0)
            return Double.NaN;
        
        double min = x[0];
        for(int i=1; i<size; i++) {
            double v = x[i];
            if(Double.isNaN(v)) {
                if(!ignoreNaN) return Double.NaN;
            } else {
                if(v < min) min = v;
            }
        }
        return min;
    }
    
    /**
     * Calculates the sum of the elements in the array, 
     * ignoring NaN values.
     */
    public static double sum(double[] x) {
        return sum(x, true);
    }
    
    /**
     * Calculates the sum of the elements in x. If NaN's are
     * not ignored, and x contains a NaN value, then the
     * result will be NaN.
     */
    public static double sum(double[] x, boolean ignoreNaN) {
        int size = x.length;
        double sum = 0d;
        
        for(int i=0; i<size; i++) {
            double v = x[i];
            if(Double.isNaN(v)) {
                if(!ignoreNaN)
                    return Double.NaN;
            } else {
                sum += v;
            }
        }
        return sum;
    }
    
    /**
     * Calculates the arithmetic mean of the input array. If the input
     * does not contain any non NaN values, then `Double.NaN` is returned.
     * 
     * @throws NullPointerException when x is `null`.
     */
    public static double mean(double[] x) {
        int size = x.length;
        double sum = 0d;
        int n = 0;
        
        for(int i=0; i<size; i++) {
            if(!isNaN(x[i])) {
                n++;
                double w = 1d / (double) n;
                sum = (1d - w) * sum + w * x[i];
            }
        }
        
        return n==0? Double.NaN : sum;
    }
    
    public static double standardDeviation(double[] x) {
        double mean = mean(x);
        return standardDeviation(x, mean);
    }
    
    /**
     * Calculates the standard deviation of x, with the given mean.
     * If the given mean is `NaN` or `x` does not contains at 
     * least two non `NaN` elements, than `NaN` is returned.
     * 
     * @throws NullPointerException when x is `null`.
     */
    public static double standardDeviation(double[] x, double mean) {
        if(isNaN(mean))
            return Double.NaN;
        
        int size = x.length;
        double sum = 0d;
        int n = 0;
        
        for(int i=0; i<size; i++) {
            double v = x[i];
            if(!isNaN(v)) {
                n++;
                sum += (v - mean) * (v - mean);
            }
        }
        
        return (--n<1)? Double.NaN : Math.sqrt(sum/(double)n);
    }

}
