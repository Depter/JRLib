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
package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.util.method.AbstractMethodSelection;

/**
 * DefaultClaimRatioSelection allows to use different 
 * {@link ClaimRatioMethod ClaimRatioMethods} for different development
 * periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultClaimRatioSelection extends AbstractMethodSelection<ClaimRatio, ClaimRatioMethod> implements ClaimRatioSelection {
    
    private int developments;
    private double[] values;
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(numerator, denominator, new DefaultCRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator, int developments) {
        this(numerator, denominator, new DefaultCRMethod(), developments);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if any of the parameters is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator, ClaimRatioMethod defaultMethod) {
        this(new DefaultRatioTriangle(numerator, denominator), defaultMethod);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if any of the parameters is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator, ClaimRatioMethod defaultMethod, int developments) {
        this(new DefaultRatioTriangle(numerator, denominator), defaultMethod, developments);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source, int developments) {
        this(source, new DefaultCRMethod(), developments);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source, ClaimRatioMethod defaultMethod) {
        this(new DefaultRatioTriangle(source), defaultMethod);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source, ClaimRatioMethod defaultMethod, int developments) {
        this(new DefaultRatioTriangle(source), defaultMethod, developments);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default 
     * method. The instance will have the same development count
     * as the `source`.
     * 
     * @see #DefaultClaimRatioSelection(RatioTriangle, int) 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source) {
        this(source, source.getDevelopmentCount());
    }
    
    /**
     * Creates a instance for the given input and length, which 
     * uses the {@link DefaultCRMethod DefaultCRMethod} as it's 
     * default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source, int developments) {
        this(source, new DefaultCRMethod(), developments);
    }
    
    /**
     * Creates a instance for the given input, which uses 
     * the supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method. The instance will have the
     * same length as `source`.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source, ClaimRatioMethod defaultMethod) {
        this(source, defaultMethod, source.getDevelopmentCount());
    }

    /**
     * Creates a instance for the given input and length, which 
     * uses the supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source, ClaimRatioMethod defaultMethod, int developments) {
        this(new ClaimRatioCalculator(source), defaultMethod, developments);
    }

    /**
     * Creates a instance for the given input and length, which 
     * uses the supplied {@link ClaimRatioMethod ClaimRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(ClaimRatio source, ClaimRatioMethod defaultMethod, int developments) {
        super(source, defaultMethod);
        this.developments = developments<0? 0 : developments;
        doRecalculate();
    }
    
    @Override
    public void setSource(RatioTriangle ratioTriangle) {
        source.setSource(ratioTriangle);
    }
    
    @Override
    public void setDevelopmentCount(int developments) {
        setState(CalculationState.INVALID);
        this.developments = (developments<0)? 0 : developments;
        doRecalculate();
        setState(CalculationState.VALID);
    }    

    @Override
    public ClaimRatio getSourceClaimRatios() {
        return source;
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceRatioTriangleInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }

    /**
     * Length of the link-ratios is equals to the number of
     * development periods in the triangle of development factors.
     * 
     * @see org.jreserve.jrlib.triangle.factor.FactorTriangle#getDevelopmentCount() 
     */
    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        return (0 <= development && development < developments)? 
                values[development] : 
                Double.NaN;
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
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
}
