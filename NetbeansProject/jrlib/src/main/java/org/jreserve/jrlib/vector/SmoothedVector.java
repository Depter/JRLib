package org.jreserve.jrlib.vector;

import org.jreserve.jrlib.vector.smoothing.VectorSmoothing;

/**
 * Modifes a vector by applying some smoothing method on the values
 * of the vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedVector extends AbstractVectorModification {
    
    protected final VectorSmoothing smoothing;
    private double[] values;
    
    /**
     * Creates an instance, with the given source and smoothing.
     * 
     * @throws NullPointerException if `source` or `smoothing` is null.
     */
    public SmoothedVector(Vector source, VectorSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        
        doRecalculate();
    }

    @Override
    public double getValue(int index) {
        return withinBonds(index)? values[index] : Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
       doRecalculate();
    }
    
    private void doRecalculate() {
        this.values = smoothing.smooth(source);
    }
}
