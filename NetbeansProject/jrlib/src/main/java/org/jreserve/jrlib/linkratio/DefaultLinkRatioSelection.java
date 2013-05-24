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

import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractMethodSelection;

/**
 * DefaultLinkRatioSelection allows to use different 
 * {@link LinkRatioMethod LinkRatioMethods} for different development
 * periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSelection extends AbstractMethodSelection<FactorTriangle, LinkRatioMethod> implements LinkRatioSelection {
    
    private int developments;
    private double[] values;
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link WeightedAverageLRMethod WeightedAverageLRMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `triangle` is null.
     */
    public DefaultLinkRatioSelection(ClaimTriangle triangle) {
        this(new DevelopmentFactors(triangle), new WeightedAverageLRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioMethod LinkRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `triangle` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultLinkRatioSelection(ClaimTriangle triangle, LinkRatioMethod defaultMethod) {
        this(new DevelopmentFactors(triangle), defaultMethod);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link WeightedAverageLRMethod WeightedAverageLRMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultLinkRatioSelection(FactorTriangle source) {
        this(source, new WeightedAverageLRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioMethod LinkRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultLinkRatioSelection(FactorTriangle source, LinkRatioMethod defaultMethod) {
        super(source, defaultMethod);
        developments = (source==null)? 0 : source.getDevelopmentCount();
        doRecalculate();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    /**
     * If the given index is within the bounds of the triangle of 
     * the development factors, then the value is obtained from the
     * {@link LinkRatioMethod LinkRatioMethod} used to calculate 
     * the link-ratio for the given index. If the index is outside
     * of the bounds of the development factors then NaN is returned.
     * 
     * @see LinkRatioMethod#getMackAlpha().
     */
    @Override
    public double getMackAlpha(int development) {
        return withinBounds(development)?
            getMethod(development).getMackAlpha() :
            Double.NaN;
    }
    
    private boolean withinBounds(int development) {
        return 0<=development && development < developments;
    }

    /**
     * If the given index is within the bounds of the triangle of 
     * the development factors, then the value is obtained from the
     * {@link LinkRatioMethod LinkRatioMethod} used to calculate 
     * the link-ratio for the given index. If the index is outside
     * of the bounds of the development factors then NaN is returned.
     * 
     * @see LinkRatioMethod#getWeight(int, int).
     */
    @Override
    public double getWeight(int accident, int development) {
        return withinBounds(development)?
            getMethod(development).getWeight(accident, development) :
            Double.NaN;
    }

    /**
     * Length of the link-ratios is equals to the number of
     * development periods in the triangle of development factors.
     * 
     * @see FactorTriangle#getDevelopmentCount().
     */
    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        return withinBounds(development)? values[development] : Double.NaN;
    }

    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
}
