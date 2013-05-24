/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.linkratio;

import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.SelectableMethod;

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
public interface LinkRatioMethod extends SelectableMethod<FactorTriangle> {
    
    /**
     * Calculates the link ratios from the given development factors.
     * 
     * @throws NullPointerException when <i>factors</i> is null.
     */
    @Override
    public void fit(FactorTriangle factors);
    
    /**
     * Returns the link ratio for the given development period, or Double.NaN
     * if it can not be calculated.
     */
    @Override
    public double getValue(int development);
    
    /**
     * Mack's alpha parameter is used to calculate the scale parameter (sigma)
     * for the variance of the link ratios.
     * 
     * Sigma is calculated as follows:
     *                   sum(i, D(a,d))
     *     sigma(k)^2 = ---------------
     *                       n - 1
     * where: 
     *    <i>n</i> is the number of link ratios in development period <i>k</i>,
     * and
     *     D(a,d) = w(a,d) * c(a,d)^alpha*(c(a,d+1)/c(a,d)-lr(d))^2,
     *     w(a,d): the used weight for the development factor,
     *     c(a,d): the value from the claim triangle,
     *     lr(d): the link ratio for development period d.
     * 
     * For more information see <i>Mack [1999]: The standard error of chain-ladder 
     * reserve estimates: Recursive calculation and inclusion of a tail factor.</i>
     */
    public double getMackAlpha();
    
    /**
     * Returns the weight of the development factor at the given cell. In most 
     * of the cases the weight calculated as follows:
     * 
     *     Double.NaN if f(i,k) = Double.NaN, 
     *     c(i,k) ^ alpha, in other cases.
     * 
     * @see #getMackAlpha() 
     */
    public double getWeight(int accident, int development);
}
