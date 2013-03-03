package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HarmonicMovingAverage extends AbstractMovingAverage {

    public HarmonicMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells, maLength);
    }

    @Override
    protected double mean(double[] input, int index) {
        double sum = 0d;
        for(int i=index-maLength+1; i<=index; i++)
            sum += 1d/input[i];
        return (double)maLength/sum;
    }
}
