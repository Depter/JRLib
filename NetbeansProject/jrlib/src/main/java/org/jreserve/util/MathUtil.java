package org.jreserve.util;

import static java.lang.Double.isNaN;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MathUtil {
    
    public static boolean isReal(double v) {
        return !isNaN(v) &&
               !Double.isInfinite(v);
    }
    
    /**
     * Calculates the arithmetic mean of the input array. If the input
     * does not contain any non NaN values, then <i>Double.NaN</i> 
     * is returned.
     * 
     * @throws NullPointerException when x is <i>null</i>.
     */
    public static double mean(double[] x) {
        int size = x.length;
        double sum = 0d;
        double n = 0;

        for(int i=0; i<size; i++) {
            double v = x[i];
            if(!isNaN(v)) {
                n++;
                sum += v;
            }
        }
        
        return (n==0)? Double.NaN : sum/(double)n;
    }
    
    public static double standardDeviation(double[] x) {
        double mean = mean(x);
        return standardDeviation(x, mean);
    }
    
    /**
     * Calculates the standard deviation of x, with the given mean.
     * If the given mean is <i>NaN</i> or x does not contains at 
     * least two non <i>NaN</i> elements, than <i>NaN</i> is returned.
     * 
     * @throws NullPointerException when x is <i>null</i>.
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
