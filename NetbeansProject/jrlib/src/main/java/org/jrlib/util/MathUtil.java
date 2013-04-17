package org.jrlib.util;

import static java.lang.Double.isNaN;

/**
 * This class provides utility methods for mathematical calculations.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MathUtil {
//TODO check if used
//    
//    public static boolean isReal(double v) {
//        return !isNaN(v) &&
//               !Double.isInfinite(v);
//    }
    
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
        
//        int size = x.length;
//        double sum = 0d;
//        double n = 0;
//
//        for(int i=0; i<size; i++) {
//            double v = x[i];
//            if(!isNaN(v)) {
//                n++;
//                sum += v;
//            }
//        }
//        
//        return (n==0)? Double.NaN : sum/(double)n;
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
