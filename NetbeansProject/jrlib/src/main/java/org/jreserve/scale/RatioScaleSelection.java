package org.jreserve.scale;

import org.jreserve.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioScaleSelection<T extends RatioScaleInput> 
    extends RatioScale<T>, 
            MethodSelection<RatioScale<T>, RatioScaleEstimator<T>> {

}
