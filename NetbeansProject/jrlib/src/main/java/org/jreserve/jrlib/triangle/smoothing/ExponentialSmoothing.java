package org.jreserve.jrlib.triangle.smoothing;

/**
 * Smoothies the values using exponential smoothing.
 * 
 * The class creates the smoothed values `s[i]` from input values
 * `x[i]`-s as follows:
 *     s[0] = x[0]
 *     s[i|i>0] = alpha * x[i] + (1-alpha) * s[i-1]
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothing extends AbstractVectorSmoothing {
    
    private double alpha;
    private double alpha2;
    
    /**
     * Creates an instance for the given cells and alpha parameter.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `alpha` is  not in the `[0;1]` 
     * interval.
     */
    public ExponentialSmoothing(SmoothingCell[] cells, double alpha) {
        super(cells);
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0; 1], but it was "+alpha+"!");
        this.alpha = alpha;
        this.alpha2 = 1d-alpha;
    }
    
    private ExponentialSmoothing() {
    }

    @Override
    protected void smooth(double[] input) {
        int size = input.length;
        if(size < 2)
            return;
        
        double s = input[0];
        for(int i=1; i<size; i++) {
            s = alpha * input[i] + alpha2 * s;
            input[i] = s;
        }
    }
}
