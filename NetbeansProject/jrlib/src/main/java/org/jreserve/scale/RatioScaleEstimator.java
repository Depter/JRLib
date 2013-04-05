package org.jreserve.scale;

import org.jreserve.util.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioScaleEstimator<T extends RatioScaleInput> extends SelectableMethod<RatioScale<T>> {
    
    @Override
    public RatioScaleEstimator<T> copy();
}
