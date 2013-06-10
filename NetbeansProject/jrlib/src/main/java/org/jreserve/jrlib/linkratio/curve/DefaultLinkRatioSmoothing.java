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
package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractMethodSelection;

/**
 * DefaultLinkRatioSmoothing allows to use different 
 * {@link LinkRatioCurve LinkRatioCurve} for different development
 * periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSmoothing extends AbstractMethodSelection<LinkRatio, LinkRatioCurve> implements LinkRatioSmoothingSelection {
    
    private int developments;
    private double[] values;
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultLRCurve DefaultLRCurve} as it's default method.
     * 
     * @throws NullPointerException if `claims` is null.
     */
    public DefaultLinkRatioSmoothing(ClaimTriangle claims) {
        this(new SimpleLinkRatio(claims));
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioCurve LinkRatioCurve} as it's 
     * default method.
     * 
     * @throws NullPointerException if `claims` is null.
     * @throws NullPointerException if `defaultFunction` is null.
     */
    public DefaultLinkRatioSmoothing(ClaimTriangle claims, LinkRatioCurve defaultFunction) {
        this(new SimpleLinkRatio(claims), defaultFunction);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultLRCurve DefaultLRCurve} as it's default method.
     * 
     * @throws NullPointerException if `factors` is null.
     */
    public DefaultLinkRatioSmoothing(FactorTriangle factors) {
        this(new SimpleLinkRatio(factors));
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioCurve LinkRatioCurve} as it's 
     * default method.
     * 
     * @throws NullPointerException if `factors` is null.
     * @throws NullPointerException if `defaultFunction` is null.
     */
    public DefaultLinkRatioSmoothing(FactorTriangle factors, LinkRatioCurve defaultFunction) {
        this(new SimpleLinkRatio(factors), defaultFunction);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultLRCurve DefaultLRCurve} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultLinkRatioSmoothing(LinkRatio source) {
        this(source, new DefaultLRCurve());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioCurve LinkRatioCurve} as it's 
     * default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultFunction` is null.
     */
    public DefaultLinkRatioSmoothing(LinkRatio source, LinkRatioCurve defaultFunction) {
        super(source, defaultFunction);
        developments = (source==null)? 0 : source.getLength();
        doRecalculate();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    /**
     * If the given index is within the bounds of this class
     * (i.e. `0&lt;=development && development &lt; getLength()`),
     * then returns the value from the source link-ratios, otherwise
     * returns NaN.
     * 
     * @see LinkRatio#getMackAlpha(int).
     */
    @Override
    public double getMackAlpha(int development) {
        return withinBounds(development)?
                source.getMackAlpha(development) :
                Double.NaN;
    }
    
    private boolean withinBounds(int development) {
        return 0<=development && development < developments;
    }

    /**
     * If the given index is within the bounds of this class
     * (i.e. `0&lt;=development && development &lt; getLength()`),
     * then returns the value from the source link-ratios, otherwise
     * returns NaN.
     * 
     * @see LinkRatio#getWeight(int, int).
     */
    @Override
    public double getWeight(int accident, int development) {
        return withinBounds(development)?
                source.getWeight(accident, development) :
                Double.NaN;
    }

    @Override
    public int getLength() {
        return developments;
    }
    
    /**
     * Sets the number of development periods. If the 
     * input value is less then 0, then 0 will be used insted.
     * 
     * Calling this method recalculates this instance and 
     * fires a change event.
     */
    @Override
    public void setDevelopmentCount(int developments) {
        setState(CalculationState.INVALID);
        this.developments = (developments<0)? 0 : developments;
        doRecalculate();
        setState(CalculationState.VALID);
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
    public void setSource(FactorTriangle source) {
        this.source.setSource(source);
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
