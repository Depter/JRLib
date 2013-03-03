package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ArithmeticMovingAverage extends AbstractMovingAverage {
    
    public ArithmeticMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells, maLength);
    }

    @Override
    protected double mean(double[] input, int index) {
        double sum = 0d;
        for(int i=index-maLength+1; i<=index; i++)
            sum += input[i];
        return sum/(double)maLength;
    }
}
