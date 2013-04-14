package org.jrlib.claimratio.scale;

import org.jrlib.AbstractCalculationData;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.scale.ScaleInput;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * This class represents the input for the calculation of the rho parameters
 * of the Munich Chain-Ladder methods. The calculations takes a 
 * {@link ClaimRatio ClaimRatio} as input and serves the following values for
 * the {@link org.jrlib.scale.Scale Scale} calculations:
 * 1.  the claim-ratio estimates as the expected ratios (`r(d)`).
 * 2.  the ratios from the source ratio triangle as the observed ratios 
 *     (`r(a,d)`).
 * 3.  the values from the denominator source triangle as (`w(a,d)`).
 * 
 * @see org.jrlib.scale.Scale
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioScaleInput extends AbstractCalculationData<ClaimRatio> implements ScaleInput{

    private RatioTriangle ratios;
    private ClaimTriangle weights;
    
    public ClaimRatioScaleInput(ClaimRatio source) {
        super(source);
        this.ratios = source.getSourceRatioTriangle();
        this.weights = ratios.getSourceDenominatorTriangle();
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
        return ratios;
    }
    
    /**
     * Returns the triangle used as numerator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return ratios.getSourceNumeratorTriangle();
    }
    
    /**
     * Returns the triangle used as denominator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return ratios.getSourceDenominatorTriangle();
    }
    
    /**
     * Returns the accident count of the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    @Override
    public int getAccidentCount() {
        return ratios.getAccidentCount();
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
        return ratios.getDevelopmentCount(accident);
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
        return ratios.getValue(accident, development);
    }

    /**
     * Returns the value from the triangle used as denominator for the given
     * accident and development period.
     * 
     * @see #getSourceDenominatorTriangle().
     */
    @Override
    public double getWeight(int accident, int development) {
        return weights.getValue(accident, development);
    }

    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public ScaleInput copy() {
        return new ClaimRatioScaleInput(source.copy());
    }
}
