package org.jreserve.triangle;

import org.jreserve.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedTriangle<T extends Triangle> extends AbstractTriangleModification<T> {

    protected final TriangleSmoothing smoothing;
    private double[][] values;
    
    public SmoothedTriangle(T source, TriangleSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        this.values = smoothing.smooth(source);
        doRecalculate();
    }
    
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
