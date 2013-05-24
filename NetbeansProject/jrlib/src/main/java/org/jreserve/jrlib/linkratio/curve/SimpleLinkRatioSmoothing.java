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

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * SimpleLinkRatioSmoothing allows to set the length of the tail factor
 * and use one curve to calculate the tail values.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSmoothing extends AbstractSimpleMethodSelection<LinkRatio, LinkRatioCurve> implements LinkRatioSmoothing {

    private int developments;
    
    /**
     * Creates an instance, which calculates the tail factors with the given 
     * method. The original length of this instance will be equal to
     * {@link FactorTriangle#getDevelopmentCount() source.getDevelopmentCount()}
     * (thus calculating no tail factors).
     * 
     * @see AbstractSimpleMethodSelection
     * @see SimpleLinkRatio#SimpleLinkRatio(ClaimTriangle) 
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(ClaimTriangle source, LinkRatioCurve method) {
        this(new SimpleLinkRatio(source), method);
    }
    
    /**
     * Creates an instance, which calculates the tail factors for the given 
     * length with the given method. If `developments` is less then 0,
     * the length of the instance will be 0.
     * 
     * @see AbstractSimpleMethodSelection
     * @see SimpleLinkRatio#SimpleLinkRatio(ClaimTriangle) 
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(ClaimTriangle source, LinkRatioCurve method, int developments) {
        this(new SimpleLinkRatio(source), method, developments);
    }
    
    /**
     * Creates an instance, which calculates the tail factors with the given 
     * method. The original length of this instance will be equal to
     * {@link FactorTriangle#getDevelopmentCount() source.getDevelopmentCount()}
     * (thus calculating no tail factors).
     * 
     * @see AbstractSimpleMethodSelection
     * @see SimpleLinkRatio#SimpleLinkRatio(FactorTriangle)  
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(FactorTriangle source, LinkRatioCurve method) {
        this(source, method, source.getDevelopmentCount());
    }
    
    /**
     * Creates an instance, which calculates the tail factors for the given 
     * length with the given method. If `developments` is less then 0,
     * the length of the instance will be 0.
     * 
     * @see AbstractSimpleMethodSelection
     * @see SimpleLinkRatio#SimpleLinkRatio(FactorTriangle)  
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(FactorTriangle source, LinkRatioCurve method, int developments) {
        this(new SimpleLinkRatio(source), method, developments);
    }
    
    /**
     * Creates an instance, which calculates the tail factors with the given 
     * method. The original length of this instance will be the same as it's
     * sources length (thus calculating no tail factors).
     * 
     * @see AbstractSimpleMethodSelection
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(LinkRatio source, LinkRatioCurve method) {
        this(source, method, source.getLength());
    }
    
    /**
     * Creates an instance, which calculates the tail factors for the given 
     * length with the given method. If `developments` is less then 0,
     * the length of the instance will be 0.
     * 
     * @see AbstractSimpleMethodSelection
     * @throws NullPointerException if `source` or `method` is null.
     */
    public SimpleLinkRatioSmoothing(LinkRatio source, LinkRatioCurve method, int developments) {
        super(source, new DefaultLRCurve(), method);
        this.developments = (developments<0)? 0 : developments;
        super.recalculateLayer();
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
        this.developments = (developments<0)? 0 : developments;
        super.recalculateLayer();
        fireChange();
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
    protected void initCalculation() {
    }

    @Override
    public void setSource(FactorTriangle source) {
        this.source.setSource(source);
    }
}
