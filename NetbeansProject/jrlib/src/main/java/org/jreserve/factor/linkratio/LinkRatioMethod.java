package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;

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
     * Calculates the link ratios from the given development factors.
     * 
     * @throws NullPointerException when <i>factors</i> is null.
     */
    public void fit(FactorTriangle factors);
    
    /**
     * Returns the link ratio for the given development period, or Double.NaN
     * if it can not be calculated.
     */
    public double getValue(int development);
    
    /**
     * Mack's alpha parameter is used to calculate the scale parameter (sigma)
     * for the variance of the link ratios.
     * 
     * <p>
     * Sigma is calculated as follows:
     * <pre>
     *              sum(i, D(i, k))
     * sigma(k)^2 = ---------------
     *                   n - 1
     * where: 
     *   <i>n</i> is the number of link ratios in development period <i>k</i>,
     *   D(i,k) = w(i,k) * C(i,k)^alpha*(C(i,k+1)/C(i,k)-LR(k))^2
     * </pre>
     * For more information see <i>Mack [1999]: The standard error of chain-ladder 
     * reserve estimates: Recursive calculation and inclusion of a tail factor.</i>
     * </p>
     */
    public double getMackAlpha();
}
