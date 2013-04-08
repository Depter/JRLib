package org.jrlib.claimratio;

import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.util.SelectableMethod;

/**
 * A claim-ratio method calculates the expected claim-ratios for a given 
 * development period, based on the input claim-ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioMethod extends SelectableMethod<RatioTriangle> {
    
    @Override
    public ClaimRatioMethod copy();
}
