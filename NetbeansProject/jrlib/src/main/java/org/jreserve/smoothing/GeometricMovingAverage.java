package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometricMovingAverage extends AbstractMovingAverage {
    
    private double power;
    
    public GeometricMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells, maLength);
        this.power = 1d/(double)maLength;
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
