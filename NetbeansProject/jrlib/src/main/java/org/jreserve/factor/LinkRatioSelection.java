package org.jreserve.factor;

import org.jreserve.triangle.Triangle;
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
public interface LinkRatioSelection extends LinkRatio, MethodSelection<Triangle, LinkRatioMethod> {
}
