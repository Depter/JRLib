package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.scale.*;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

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
}
