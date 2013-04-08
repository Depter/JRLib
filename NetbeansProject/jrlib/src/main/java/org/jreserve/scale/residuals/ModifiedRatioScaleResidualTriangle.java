package org.jreserve.scale.residuals;

import org.jreserve.scale.RatioScaleInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedRatioScaleResidualTriangle<T extends RatioScaleInput> extends RatioScaleResidualTriangle<T> {
    
    public RatioScaleResidualTriangle<T> getSourceResidualTriangle();

}
