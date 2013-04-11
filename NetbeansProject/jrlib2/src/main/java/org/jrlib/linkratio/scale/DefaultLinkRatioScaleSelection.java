package org.jrlib.linkratio.scale;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.scale.DefaultScaleCalculator;
import org.jrlib.scale.DefaultScaleEstimator;
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

    @Override
    public DefaultLinkRatioScaleSelection copy() {
        return new DefaultLinkRatioScaleSelection(this);
    }
}
