package org.jreserve.util;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SigmaFilter implements Filter {
    
    public final static double DEFAULT_TRESHOLD = 1.5;
    
    private double treshold;
    
    public SigmaFilter() {
        this(DEFAULT_TRESHOLD);
    }
    
    public SigmaFilter(double treshold) {
        initTreshold(treshold);
    }
    
    private void initTreshold(double treshold) {
        if(Double.isNaN(treshold))
            throw new IllegalArgumentException("Treshold can not be NaN!");
        this.treshold = (treshold<0d)? 0d : treshold;
    }
    
    public double getTreshold() {
        return treshold;
    }
    
    public void setTreshold(double treshold) {
        initTreshold(treshold);
    }
    
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
