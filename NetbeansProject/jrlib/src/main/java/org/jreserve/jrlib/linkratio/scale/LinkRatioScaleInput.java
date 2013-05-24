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
package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.scale.ScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * This class provides the input needed to calculate the sigma's for
 * Mack's method. It serves the values from it's source as defined by
 * the {@link LinkRatio LinkRatio} interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleInput extends AbstractCalculationData<LinkRatio> implements ScaleInput {

    private FactorTriangle factors;
    private ClaimTriangle claims;
    
    /**
     * Creates an instance with the given claims.
     * 
     * @see SimpleLinkRatio
     * @throws NullPointerException if `claims` is null.
     */
    public LinkRatioScaleInput(ClaimTriangle claims) {
        this(new SimpleLinkRatio(claims));
    }
    
    /**
     * Creates an instance with the given factors.
     * 
     * @see SimpleLinkRatio
     * @throws NullPointerException if `factors` is null.
     */
    public LinkRatioScaleInput(FactorTriangle factors) {
        this(new SimpleLinkRatio(factors));
    }
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public LinkRatioScaleInput(LinkRatio source) {
        super(source);
        this.factors = source.getSourceFactors();
        this.claims = factors.getSourceTriangle();
    } 
    
    /**
     * Returns the link-ratios used as input.
     */
    public LinkRatio getSourceLinkRatios() {
        return source;
    }
    
    /**
     * Returns the development factors used as input for 
     * the calculation of the link-ratios.
     */
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    /**
     * Returns the claims used as input for the calculation of 
     * the development factors.
     */
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    /**
     * Returns the number of accident periods form the factor triangle.
     * 
     * @see LinkRatio#getSourceFactors().
     */
    @Override
    public int getAccidentCount() {
        return factors.getAccidentCount();
    }

    /**
     * Returns the number of development periods form the source link-ratios.
     */
    @Override
    public int getDevelopmentCount() {
        return source.getLength();
    }

    /**
     * Returns the number of development periods for the given accident
     * period form the factor triangle.
     * 
     * @see LinkRatio#getSourceFactors().
     */
    @Override
    public int getDevelopmentCount(int accident) {
        return factors.getDevelopmentCount(accident);
    }

    /**
     * Returns the link-ratio for the given development.
     * 
     * @see LinkRatio#getValue(int) 
     */
    @Override
    public double getRatio(int development) {
        return source.getValue(development);
    }

    /**
     * Returns the development factor for the given accident
     * and development period form the factor triangle.
     * 
     * @see LinkRatio#getSourceFactors().
     */
    @Override
    public double getRatio(int accident, int development) {
        return factors.getValue(accident, development);
    }

    /**
     * Returns the claim for the given accident
     * and development period form the claim triangle.
     * 
     * @see LinkRatio#getSourceTriangle().
     */
    @Override
    public double getWeight(int accident, int development) {
        return claims.getValue(accident, development);
    }
    
    @Override
    protected void recalculateLayer() {
        this.factors = source.getSourceFactors();
        this.claims = factors.getSourceTriangle();
    }
}
