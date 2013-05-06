package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 * Modifes a triangle by applying some smoothing method on the values
 * of the triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedTriangle<T extends Triangle> extends AbstractTriangleModification<T> {

    protected final TriangleSmoothing smoothing;
    private double[][] values;
    
    /**
     * Creates an instance, with the given source and smoothing.
     * 
     * @throws NullPointerException if `source` or `smoothing` is null.
     */
    public SmoothedTriangle(T source, TriangleSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        doRecalculate();
    }
    
    /**
     * Returns the smoothing used by this instance.
     */
    public TriangleSmoothing getSmoothing() {
        return smoothing;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        this.values = smoothing.smooth(source);
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
}
