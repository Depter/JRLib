package org.jreserve.jrlib.vector.smoothing;

/**
 * Smoothes the input by replacing the values by
 * their geometric moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 * <pre>
 *      s[i] = (x[i-l+1] * ... * x[l])^(1/l) , for i %gt;= l-1, and
 *      s[i] = x[i]                          , for i &lt; l-1
 * </pre>
 * where `x[i]` is the input value at the ith position.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometricMovingAverageMethod extends AbstractMovingAverageMethod {
    
    private final double power;
    
    /**
     * Creates an instance for the given length for the moving 
     * average.
     * 
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public GeometricMovingAverageMethod(int length) {
        super(length);
        this.power = 1d/(double)length;
    }
    
    @Override
    protected double mean(double[] input, int index) {
        double product = 1d;
        for(int i=index-maLength+1; i<=index; i++)
            product *= input[i];
        return Math.pow(product, power);
    }
    
}
