package org.jrlib.linkratio;

import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.MethodSelection;

/**
 * This interface represents the calculation of link ratios, from
 * a triangle of development factors.
 * 
 * The class enables the users to use different calculation 
 * methods ({@link LinkRatioMethod LinkRatioMethod}) for different 
 * development periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSelection extends LinkRatio, MethodSelection<FactorTriangle, LinkRatioMethod> {

    @Override
    public LinkRatioSelection copy();
}
