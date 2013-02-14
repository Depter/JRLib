package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 * This interface represents methods, that can calculate the 
 * link ratios for a triangle of development factors. As The
 * implementations of this interface are mainly used by
 * implementations of {@link LinkRatioSelection LinkRatioSelection},
 * it is highly recommended to override the <i>equals()</i> and
 * <i>hashCode</i> methods, to enable caching of results.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioMethod {
    
    /**
     * Calculates the link ratios from the given development factors and weights.
     * The returned array should be the same lengths as
     * the {@link Triangle#getDevelopmentCount() developmentCount}
     * of the input factors.
     * 
     * @throws NullPointerException when <i>factors</i> or <i>weights</i> 
     * is null.
     */
    public double[] getLinkRatios(Triangle factors, Triangle weights);
}
