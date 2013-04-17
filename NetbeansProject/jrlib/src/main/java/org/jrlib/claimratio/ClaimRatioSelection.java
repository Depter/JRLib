package org.jrlib.claimratio;

import org.jrlib.util.method.MethodSelection;

/**
 * A ClaimRatioSelection allows the use of different calculation methods
 * for different development periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioSelection extends ClaimRatio, MethodSelection<ClaimRatio, ClaimRatioMethod> {
    
    /**
     * Returns the claim-ratios used by this instance.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Sets the number of development periods, for which an
     * estimate is maid.
     */
    public void setDevelopmentCount(int developments);

}
