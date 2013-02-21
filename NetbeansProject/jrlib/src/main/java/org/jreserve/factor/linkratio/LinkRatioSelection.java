package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.util.MethodSelection;

/**
 * This interface represents the calculation of link ratios, from
 * a triangle of development factors.
 * 
 * <p>The class enables the users to use different calculation 
 * methods ({@link LinkRatioMethod LinkRatioMethod}) for different 
 * development periods.</p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSelection extends LinkRatio, MethodSelection<FactorTriangle, LinkRatioMethod> {
}
