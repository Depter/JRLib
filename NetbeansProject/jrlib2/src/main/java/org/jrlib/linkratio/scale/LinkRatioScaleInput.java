package org.jrlib.linkratio.scale;

import org.jrlib.AbstractCalculationData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.scale.ScaleInput;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

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
     * Returns the number of development periods form the factor triangle.
     * 
     * @see LinkRatio#getSourceFactors().
     */
    @Override
    public int getDevelopmentCount() {
        return factors.getDevelopmentCount();
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
     * @see LinkRatio#getValue(int,int).
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
    public LinkRatioScaleInput copy() {
        return new LinkRatioScaleInput(source.copy());
    }
    
    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }
}
