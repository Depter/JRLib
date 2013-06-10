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
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * SimpleClaimRatio is a basic implementation for the 
 * {@link ClaimRatio ClaimRatio} interface. It is uses only one method
 * to calculate all claim ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleClaimRatio extends AbstractSimpleMethodSelection<ClaimRatio, ClaimRatioMethod> implements ClaimRatio {
    
    /**
     * Creates an instance for the given input, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public SimpleClaimRatio(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(numerator, denominator, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given input, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(ClaimTriangle numerator, ClaimTriangle denominator, ClaimRatioMethod method) {
        this(new DefaultRatioTriangle(numerator, denominator), method);
    }
    
    /**
     * Creates an instance for the given source, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatio(RatioTriangleInput source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(RatioTriangleInput source, ClaimRatioMethod method) {
        this(new DefaultRatioTriangle(source), method);
    }
    
    /**
     * Creates an instance for the given source, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatio(RatioTriangle source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(RatioTriangle source, ClaimRatioMethod method) {
        this(new ClaimRatioCalculator(source), method);
    }
    
    /**
     * Creates an instance for the given source, using the given method to
     * fill NaN values.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(ClaimRatio source, ClaimRatioMethod method) {
        super(source, 
              (method instanceof DefaultCRMethod)? method : new DefaultCRMethod(),
              method);
        this.length = source.getLength();
        super.recalculateLayer();
    }
    
    @Override
    public void setSource(RatioTriangle ratioTriangle) {
        source.setSource(ratioTriangle);
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
     * Does nothing.
     */
    @Override
    protected void initCalculation() {
        //developments = source.getLength();
    }
    
    /**
     * Sets the length for the claim ratios. If 'developments' is less 
     * then 0, 0 will be used isntead. Calling this method fires a 
     * change event.
     */
    public void setDevelopmentCount(int developments) {
        setState(CalculationState.INVALID);
        this.length = developments<0? 0 : developments;
        super.recalculateLayer();
        setState(CalculationState.VALID);
    }
}