package org.jreserve.jrlib.triangle.smoothing;

/**
 * Abstract class for smoothing data based on some kind of moving average
 * (arithmetic, harmonic, etc.).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMovingAverage extends AbstractVectorSmoothing {

    protected int maLength;
    
    /**
     * Creates an instance for the given cells and length for the moving 
     * average.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `length` is ;ess then 1.
     */
    protected AbstractMovingAverage(SmoothingCell[] cells, int maLength) {
        super(cells);
        if(maLength <= 0)
            throw new IllegalArgumentException("Length must be at least 1, but was "+maLength+"!");
        this.maLength = maLength;
    }

    protected AbstractMovingAverage() {
    }
    
    @Override
    protected void smooth(double[] input) {
        int size = input.length;
        if(size < maLength)
            return;
        
        double[] temp = new double[size];
        for(int i=(maLength-1); i<size; i++)
            temp[i] = mean(input, i);
        
        System.arraycopy(temp, maLength-1, input, maLength-1, size-maLength+1);
    }
    
    /**
     * Extending classes should calculate a mean for the given location.
     */
    protected abstract double mean(double[] input, int index);
}
