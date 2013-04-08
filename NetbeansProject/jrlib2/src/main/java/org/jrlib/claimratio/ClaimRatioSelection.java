package org.jrlib.claimratio;

import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.util.MethodSelection;

/**
 * A ClaimRatioSelection allows the use of different calculation methods
 * for different development periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioSelection extends ClaimRatio, MethodSelection<RatioTriangle, ClaimRatioMethod> {

}
