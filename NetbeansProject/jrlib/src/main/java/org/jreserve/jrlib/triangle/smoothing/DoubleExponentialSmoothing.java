package org.jreserve.jrlib.triangle.smoothing;

/**
 * Smoothies the values using double exponential smoothing.
 * 
 * The class creates the smoothed values `s[i]` from input values
 * `x[i]`-s as follows:
 *      b[1] = x[1] - x[0]
 *      b[i|i>1] = beta * (s[i]-x[i-1]) + (1-beta) * b[i-1]
 * 
 *      s[0] = x[0]
 *      s[i|i>0] = alpha * x[i-1] + (1-alpha)*(s[i-1] + b[i-1])
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleExponentialSmoothing extends AbstractVectorSmoothing {

    private double alpha;
    private double alpha2;
    private double beta;
    private double beta2;
    
    /**
     * Creates an instance for the given cells and parameters
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `alpha` or `beta` is  not in the 
     * `[0;1]` interval.
     */
    public DoubleExponentialSmoothing(SmoothingCell[] cells, double alpha, double beta) {
        super(cells);
        
        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within [0; 1], but it was "+alpha+"!");
        this.alpha = alpha;
        this.alpha2 = 1d-alpha;
        
        if(beta < 0d || beta > 1d)
            throw new IllegalArgumentException("Beta must be within [0; 1], but it was "+beta+"!");
        this.beta = beta;
        this.beta2 = 1d-beta;
    }
    
    private DoubleExponentialSmoothing() {
    }
    
    @Override
    protected void smooth(double[] input) {
        int size = input.length;
        if(size < 2)
            return;
        
        double s = input[0];
        double b = input[1] - input[0];
        for(int i=1; i<size; i++) {
            s = alpha * input[i] + alpha2 * (s + b);
            b = beta * (s - input[i-1]) + beta2 * b;
            input[i] = s;
        }
    }
}
