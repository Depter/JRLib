package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.util.AbstractSimpleEstimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSmoothing extends AbstractSimpleEstimator<LinkRatio, LinkRatioFunction> implements LinkRatioSmoothing {

    public SimpleLinkRatioSmoothing(LinkRatio source, LinkRatioFunction function) {
        this(source, source.getDevelopmentCount(), function);
    }

    public SimpleLinkRatioSmoothing(LinkRatio source, int developments, LinkRatioFunction function) {
        super(source, function, developments);
    }
    
    @Override
    public void setDevelopmentCount(int developments) {
        super.setDevelopmentCount(developments);
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }
    
    @Override
    public void setSource(FactorTriangle factors) {
        source.setSource(factors);
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
    public double getMackAlpha(int development) {
        int maxDev = source.getDevelopmentCount();
        development = maxDev<development? maxDev : development;
        return source.getMackAlpha(development);
    }
    
    @Override
    public double getWeight(int accident, int development) {
        return source.getWeight(accident, development);
    }
    
    @Override
    public String toString() {
        return String.format("SimpleLinkRatioSmoothing [%s]", estimator);
    }
    
    @Override
    public SimpleLinkRatioSmoothing copy() {
        return new SimpleLinkRatioSmoothing(source.copy(), 
                developments, 
                estimator.copy()
                );
    }
    
    @Override
    protected void initCalculation() {
        values = null;
    }
}