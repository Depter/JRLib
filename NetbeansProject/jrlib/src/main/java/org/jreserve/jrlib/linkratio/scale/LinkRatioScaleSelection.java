package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.scale.ScaleSelection;

/**
 * Implementations of this interface enables to use different methods
 * to calculate link-ratios for different development periods (for example
 * to manually set a value for a given development period).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScaleSelection 
    extends LinkRatioScale, ScaleSelection<LinkRatioScaleInput> {
}
