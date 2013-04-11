package org.jrlib.claimratio.scale;

import org.jrlib.scale.ScaleSelection;

/**
 * Implementations of this interface enables to use different methods
 * to calculate rhos for different development periods (for example
 * to manually set a value for a given development period).
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioScaleSelection 
    extends ClaimRatioScale, ScaleSelection<ClaimRatioScaleInput> {
    
    @Override
    public ClaimRatioScaleSelection copy();
}
