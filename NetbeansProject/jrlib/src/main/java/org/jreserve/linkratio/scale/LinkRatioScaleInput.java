package org.jreserve.linkratio.scale;

import org.jreserve.AbstractCalculationData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScaleInput;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleInput extends AbstractCalculationData<LinkRatio> implements RatioScaleInput {
    
    private FactorTriangle factors;
    private ClaimTriangle cik;
    
    protected LinkRatioScaleInput(LinkRatio source) {
        super(source);
        this.factors = source.getSourceFactors();
        this.cik = factors.getSourceTriangle();
    }
    
    public LinkRatio getSourceLinkRatios() {
        return source;
    }
    
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public int getAccidentCount() {
        return factors.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return factors.getDevelopmentCount(accident);
    }

    @Override
    public double getRatio(int development) {
        return source.getValue(development);
    }

    @Override
    public double getRatio(int accident, int development) {
        return factors.getValue(accident, development);
    }

    @Override
    public double getWeight(int accident, int development) {
        return cik.getValue(accident, development);
    }

    @Override
    public LinkRatioScaleInput copy() {
        return new LinkRatioScaleInput(source.copy());
    }

    @Override
    protected void recalculateLayer() {
    }
}
