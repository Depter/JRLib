package org.jreserve.jrlib.triangle.smoothing;

/**
 * Smoothes the input by replacing the values by
 * their arithmetic moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 *      s[i] = sum(x[i-l+1], ..., x[l])/l , for i %gt;= l-1, and
 *      s[i] = x[i]                       , for i &lt; l-1
 * where `x[i]` is the input value at the ith position.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ArithmeticMovingAverage extends AbstractMovingAverage {
    
    /**
     * Creates an instance for the given cells and length for the moving 
     * average.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public ArithmeticMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells, maLength);
    }

    private ArithmeticMovingAverage() {
    }
    
    @Override
    protected double mean(double[] input, int index) {
        double sum = 0d;
        for(int i=index-maLength+1; i<=index; i++)
            sum += input[i];
        return sum/(double)maLength;
    }
}
