package org.jreserve.jrlib.vector.smoothing;

/**
 * Abstract class for smoothing data based on some kind of moving average
 * (arithmetic, harmonic, etc.).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMovingAverageMethod implements VectorSmoothingMethod {
    
    protected int maLength;
    
    /**
     * Creates an instance for the given length for the moving 
     * average.
     * 
     * @throws IllegalArgumentException if `length` is ;ess then 1.
     */
    protected AbstractMovingAverageMethod(int maLength) {
        if(maLength <= 0)
            throw new IllegalArgumentException("Length must be at least 1, but was "+maLength+"!");
        this.maLength = maLength;
    }
    
    @Override
    public void smooth(double[] input) {
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
