package org.jreserve.smoothing;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleSmoothingCalculationData extends AbstractTriangleModification {

    private final TriangleSmoothing smoothing;
    private double[][] values;
    
    public TriangleSmoothingCalculationData(Triangle source, TriangleSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        this.values = smoothing.smooth(source);
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
