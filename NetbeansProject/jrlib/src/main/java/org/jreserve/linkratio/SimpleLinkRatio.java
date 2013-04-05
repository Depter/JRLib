package org.jreserve.linkratio;

import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.util.AbstractSimpleEstimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatio extends AbstractSimpleEstimator<FactorTriangle, LinkRatioMethod> implements LinkRatio {

    public SimpleLinkRatio(ClaimTriangle triangle) {
        this(new DevelopmentFactors(triangle), null);
    }

    public SimpleLinkRatio(ClaimTriangle triangle, LinkRatioMethod method) {
        this(new DevelopmentFactors(triangle), method);
    }
    
    public SimpleLinkRatio(FactorTriangle source) {
        this(source, null);
    }
    
    public SimpleLinkRatio(FactorTriangle source, LinkRatioMethod method) {
        super(source, method, source.getDevelopmentCount());
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public double getMackAlpha(int development) {
        return estimator.getMackAlpha();
    }
    
    @Override
    public double getWeight(int accident, int development) {
        return estimator.getWeight(accident, development);
    }
    
    @Override
    protected void initCalculation() {
        developments = source.getDevelopmentCount();
        values = null;
    }
    
    @Override
    public SimpleLinkRatio copy() {
        return new SimpleLinkRatio(source.copy(), estimator.copy());
    }
}