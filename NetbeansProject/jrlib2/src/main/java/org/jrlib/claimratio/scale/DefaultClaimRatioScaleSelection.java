package org.jrlib.claimratio.scale;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.scale.*;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultClaimRatioScaleSelection extends DefaultScaleSelection<ClaimRatioScaleInput> implements ClaimRatioScaleSelection {

    private ClaimRatioScaleInput sourceInput;

    public DefaultClaimRatioScaleSelection(ClaimRatio source) {
        this(new ClaimRatioScaleInput(source));
    }
    
    public DefaultClaimRatioScaleSelection(ClaimRatio source, ScaleEstimator<ClaimRatioScaleInput> method) {
        this(new ClaimRatioScaleInput(source), method);
    }

    public DefaultClaimRatioScaleSelection(ClaimRatioScaleInput source) {
        this(source, new DefaultScaleEstimator<ClaimRatioScaleInput>());
    }
    
    public DefaultClaimRatioScaleSelection(ClaimRatioScaleInput source, ScaleEstimator<ClaimRatioScaleInput> method) {
        this(new DefaultScaleCalculator<ClaimRatioScaleInput>(source), method);
    }

    public DefaultClaimRatioScaleSelection(Scale<ClaimRatioScaleInput> source) {
        this(source, new DefaultScaleEstimator<ClaimRatioScaleInput>());
    }

    public DefaultClaimRatioScaleSelection(Scale<ClaimRatioScaleInput> source, ScaleEstimator<ClaimRatioScaleInput> method) {
        super(source, method);
        this.sourceInput = source.getSourceInput();
    }
    
    private DefaultClaimRatioScaleSelection(DefaultClaimRatioScaleSelection original) {
        super(original);
        this.sourceInput = super.source.getSourceInput();
    }

    @Override
    public void setDevelopmentCount(int developments) {
        super.setLength(developments);
    }
    
    @Override
    public ClaimRatio getSourceClaimRatios() {
        return sourceInput.getSourceClaimRatios();
    }

    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return sourceInput.getSourceRatioTriangle();
    }

    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return sourceInput.getSourceRatioTriangle().getSourceInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return sourceInput.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return sourceInput.getSourceDenominatorTriangle();
    }

    @Override
    public DefaultClaimRatioScaleSelection copy() {
        return new DefaultClaimRatioScaleSelection(this);
    }
    
    /**
     * Do not recalculate the length.
     */
    @Override
    protected void recalculateLength() {
    }
}
