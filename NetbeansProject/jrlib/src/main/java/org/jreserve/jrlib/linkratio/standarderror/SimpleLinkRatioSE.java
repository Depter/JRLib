package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSE 
    extends AbstractSimpleMethodSelection<LinkRatioSE, LinkRatioSEFunction> 
    implements LinkRatioSE {

    
    public SimpleLinkRatioSE(LinkRatioScale scale) {
        this(scale, new LogLinearRatioSEFunction());
    }
    
    public SimpleLinkRatioSE(LinkRatioScale scales, LinkRatioSEFunction estimator) {
        super(new LinkRatioSECalculator(scales), new DefaultLinkRatioSEFunction(), estimator);
    }

    @Override
    public LinkRatioScale getSourceLRScales() {
        return source.getSourceLRScales();
    }

    @Override
    public LinkRatioScaleInput getSourceLrScaleInput() {
        return source.getSourceLrScaleInput();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
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
        return source.getLength();
    }
    
    @Override
    protected void initCalculation() {
    }
}
