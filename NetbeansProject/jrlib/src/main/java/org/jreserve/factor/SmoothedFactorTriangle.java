package org.jreserve.factor;

import org.jreserve.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedFactorTriangle extends AbstractFactorTriangleModification {

    private final TriangleSmoothing smoothing;
    private double[][] values;
    
    public SmoothedFactorTriangle(FactorTriangle source, TriangleSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        this.values = smoothing.smooth(source);
    }
    
    public TriangleSmoothing getSmoothing() {
        return smoothing;
    }
    
    @Override
    protected void recalculateLayer() {
        this.values = smoothing.smooth(source);
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
}
