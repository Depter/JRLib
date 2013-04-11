package org.jrlib.linkratio.scale;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.scale.DefaultScaleSelection;
import org.jrlib.scale.Scale;
import org.jrlib.scale.ScaleEstimator;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioScaleSelection extends DefaultScaleSelection<LinkRatioScaleInput> implements LinkRatioScaleSelection {

    private LinkRatioScaleInput sourceInput;

    public DefaultLinkRatioScaleSelection(Scale<LinkRatioScaleInput> source, ScaleEstimator<LinkRatioScaleInput> method) {
        super(source, method);
    }
    
    private DefaultLinkRatioScaleSelection(DefaultLinkRatioScaleSelection original) {
        super(original);
        this.sourceInput = super.source.getSourceInput();
    }
    
    @Override
    public LinkRatioScaleInput getSourceInput() {
        return sourceInput;
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return sourceInput.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return sourceInput.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return sourceInput.getSourceTriangle();
    }

    @Override
    public DefaultLinkRatioScaleSelection copy() {
        return new DefaultLinkRatioScaleSelection(this);
    }
}
