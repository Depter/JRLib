package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMovingAverage extends AbstractVectorSmoothing {

    protected final int maLength;
    
    protected AbstractMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells);
        if(maLength <= 0)
            throw new IllegalArgumentException("Length must be at least 1, but was "+maLength+"!");
        this.maLength = maLength;
    }

    @Override
    protected void smooth(double[] input) {
        int size = input.length;
        if(size <= maLength)
            return;
        
        double[] temp = new double[size];
        for(int i=(maLength-1); i<size; i++)
            temp[i] = mean(input, i);
        
        System.arraycopy(temp, maLength-1, input, maLength-1, size-maLength+1);
    }

    protected abstract double mean(double[] input, int index);
}
