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
package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.scale.ScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 * This class represents the input for the calculation of the rho parameters
 * of the Munich Chain-Ladder methods. The calculations takes a 
 * {@link ClaimRatio ClaimRatio} as input and serves the following values for
 * the {@link org.jreserve.jrlib.scale.Scale Scale} calculations:
 * 1.  the claim-ratio estimates as the expected ratios (`r(d)`).
 * 2.  the ratios from the source ratio triangle as the observed ratios 
 *     (`r(a,d)`).
 * 3.  the values from the denominator source triangle as (`w(a,d)`).
 * 
 * @see org.jreserve.jrlib.scale.Scale
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioScaleInput extends AbstractCalculationData<ClaimRatio> implements ScaleInput{
    
    public ClaimRatioScaleInput(ClaimRatio source) {
        super(source);
    }
    
    /**
     * Returns the claim-ratios used as input for the calculation.
     */
    public ClaimRatio getSourceClaimRatios() {
        return source;
    }
    
    /**
     * Returns the ratio-triangle which is used to calculate the claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    /**
     * Returns the triangle used as numerator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }
    
    /**
     * Returns the triangle used as denominator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    /**
     * Returns the accident count of the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    @Override
    public int getAccidentCount() {
        return getSourceRatioTriangle().getAccidentCount();
    }
    
    /**
     * Returns the development count of the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    @Override
    public int getDevelopmentCount() {
        return source.getLength();
    }

    /**
     * Returns the development count from the ratio-triangle for the given
     * accident period.
     * 
     * @see #getSourceRatioTriangle().
     */
    @Override
    public int getDevelopmentCount(int accident) {
        return getSourceRatioTriangle().getDevelopmentCount(accident);
    }

    /**
     * Returns the expected claim-ratio for the given development period.
     * 
     * @see #getSourceClaimRatios().
     */
    @Override
    public double getRatio(int development) {
        return source.getValue(development);
    }

    /**
     * Returns the ratios from the ratio-triangle for the given
     * accident and development period.
     * 
     * @see #getSourceRatioTriangle().
     */
    @Override
    public double getRatio(int accident, int development) {
        return getSourceRatioTriangle().getValue(accident, development);
    }

    /**
     * Returns the value from the triangle used as denominator for the given
     * accident and development period.
     * 
     * @see #getSourceDenominatorTriangle().
     */
    @Override
    public double getWeight(int accident, int development) {
        return getSourceDenominatorTriangle().getValue(accident, development);
    }

    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }
    
    @Override
    public void setSource(ClaimRatio source) {
        super.setSource(source);
    }
}
