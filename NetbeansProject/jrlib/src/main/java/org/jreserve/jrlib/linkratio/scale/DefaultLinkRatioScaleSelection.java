package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.scale.*;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @see DefaultScaleSelection
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioScaleSelection extends DefaultScaleSelection<LinkRatioScaleInput> implements LinkRatioScaleSelection {

    private LinkRatioScaleInput sourceInput;
    
    public DefaultLinkRatioScaleSelection(LinkRatio source) {
        this(new LinkRatioScaleInput(source));
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatio source, ScaleEstimator<LinkRatioScaleInput> method) {
        this(new LinkRatioScaleInput(source), method);
    }

    public DefaultLinkRatioScaleSelection(LinkRatioScaleInput source) {
        this(source, new DefaultScaleEstimator<LinkRatioScaleInput>());
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatioScaleInput source, ScaleEstimator<LinkRatioScaleInput> method) {
        this(new DefaultScaleCalculator<LinkRatioScaleInput>(source), method);
    }

    public DefaultLinkRatioScaleSelection(Scale<LinkRatioScaleInput> source) {
        this(source, new DefaultScaleEstimator<LinkRatioScaleInput>());
    }

    public DefaultLinkRatioScaleSelection(Scale<LinkRatioScaleInput> source, ScaleEstimator<LinkRatioScaleInput> method) {
        super(source, method);
        this.sourceInput = source.getSourceInput();
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
}
