package org.jreserve.scale.residuals;

import org.jreserve.scale.RatioScale;
import org.jreserve.scale.RatioScaleInput;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioScaleResidualTriangleCorrection<T extends RatioScaleInput>
    extends TriangleCorrection<RatioScaleResidualTriangle<T>> 
    implements ModifiedRatioScaleResidualTriangle<T> {

    public RatioScaleResidualTriangleCorrection(RatioScaleResidualTriangle<T> source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    public RatioScaleResidualTriangleCorrection(RatioScaleResidualTriangle<T> source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    @Override
    public RatioScaleResidualTriangle<T> getSourceResidualTriangle() {
        return source;
    }

    @Override
    public RatioScale<T> getSourceRatioScales() {
        return source.getSourceRatioScales();
    }

    @Override
    public T getSourceInput() {
        return source.getSourceInput();
    }
}
