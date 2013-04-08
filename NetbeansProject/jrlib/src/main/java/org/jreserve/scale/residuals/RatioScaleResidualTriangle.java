package org.jreserve.scale.residuals;

import org.jreserve.scale.RatioScale;
import org.jreserve.scale.RatioScaleInput;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioScaleResidualTriangle<T extends RatioScaleInput> extends Triangle {

    public RatioScale<T> getSourceRatioScales();
    
    public T getSourceInput();
    
}
