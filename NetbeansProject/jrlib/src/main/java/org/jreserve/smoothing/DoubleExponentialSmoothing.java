package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleExponentialSmoothing extends AbstractVectorSmoothing {

    private final double alpha;
    private final double alpha2;
    private final double beta;
    private final double beta2;
    
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