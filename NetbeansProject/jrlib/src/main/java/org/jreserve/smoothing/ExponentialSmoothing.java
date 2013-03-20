package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothing extends AbstractVectorSmoothing {
    
    private double alpha;
    private double alpha2;
    
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
    
    @Override
    public ExponentialSmoothing copy() {
        ExponentialSmoothing copy = new ExponentialSmoothing();
        copy.copyStateFrom(this);
        copy.alpha = this.alpha;
        copy.alpha2 = this.alpha2;
        return copy;
    }
}
