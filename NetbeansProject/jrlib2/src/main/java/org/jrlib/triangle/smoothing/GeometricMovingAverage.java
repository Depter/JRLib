package org.jrlib.triangle.smoothing;

/**
 * Smoothes the input by replacing the values by
 * their geometric moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 *      s[i] = (x[i-l+1] * ... * x[l])^(1/l) , for i %gt;= l-1, and
 *      s[i] = x[i]                          , for i &lt; l-1
 * where `x[i]` is the input value at the ith position.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometricMovingAverage extends AbstractMovingAverage {
    
    private double power;
    
    /**
     * Creates an instance for the given cells and length for the moving 
     * average.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public GeometricMovingAverage(SmoothingCell[] cells, int length) {
        super(cells, length);
        this.power = 1d/(double)length;
    }
    
    private GeometricMovingAverage() {
    }
    
    @Override
    protected double mean(double[] input, int index) {
        double product = 1d;
        for(int i=index-maLength+1; i<=index; i++)
            product *= input[i];
        return Math.pow(product, power);
    }
    
    @Override
    public GeometricMovingAverage copy() {
        GeometricMovingAverage copy = new GeometricMovingAverage();
        copy.copyStateFrom(this);
        copy.power = this.power;
        return copy;
    }
}
