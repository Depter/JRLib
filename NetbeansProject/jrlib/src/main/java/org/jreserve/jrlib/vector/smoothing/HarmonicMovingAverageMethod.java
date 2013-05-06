package org.jreserve.jrlib.vector.smoothing;

/**
 * Smoothes the input by replacing the values by
 * their harmonic moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 * <pre>
 *      s[i] = 1/(1/x[i-l+1]+ ...+ 1/x[l]) , for i %gt;= l-1, and
 *      s[i] = x[i]                        , for i &lt; l-1
 * </pre>
 * where `x[i]` is the input value at the ith position.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HarmonicMovingAverageMethod extends AbstractMovingAverageMethod {

    /**
     * Creates an instance for the given length for the moving 
     * average.
     * 
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public HarmonicMovingAverageMethod(int length) {
        super(length);
    }
    
    @Override
    protected double mean(double[] input, int index) {
        double sum = 0d;
        for(int i=index-maLength+1; i<=index; i++)
            sum += 1d/input[i];
        return (double)maLength/sum;
    }
    
}
