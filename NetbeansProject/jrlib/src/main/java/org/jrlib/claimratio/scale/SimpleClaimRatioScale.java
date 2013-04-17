package org.jrlib.claimratio.scale;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.scale.Scale;
import org.jrlib.scale.ScaleEstimator;
import org.jrlib.scale.SimpleScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;

/**
 * Basic implementation for the {@link ClaimRatioScale ClaimRatioScale} interface
 * which fills in the missing values (NaNs) with a preset
 * {@link ScaleEstimator ScaleEstimator}.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleClaimRatioScale extends SimpleScale<ClaimRatioScaleInput> implements ClaimRatioScale {

    private ClaimRatioScaleInput sourceInput;
    
    /**
     * Creates an instance for the given source.
     * 
     * @see ClaimRatioScaleInput#ClaimRatioScaleInput(ClaimRatio) 
     * @see #SimpleClaimRatioScale(ClaimRatioScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(ClaimRatio source) {
        this(new ClaimRatioScaleInput(source));
    }
    
    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see ClaimRatioScaleInput#ClaimRatioScaleInput(ClaimRatio) 
     * @see #SimpleClaimRatioScale(ClaimRatioScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(ClaimRatio source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        this(new ClaimRatioScaleInput(source), estimator);
    }
    
    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jrlib.scale.ScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(ClaimRatioScaleInput source) {
        super(source);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(ScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(ClaimRatioScaleInput source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jrlib.scale.Scale) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatioScale(Scale<ClaimRatioScaleInput> source) {
        super(source);
        this.sourceInput = source.getSourceInput();
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(Scale, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleClaimRatioScale(Scale<ClaimRatioScaleInput> source, ScaleEstimator<ClaimRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source.getSourceInput();
    }
    
    /**
     * Do not get the length from the source.
     */
    @Override
    protected void initCalculation() {
    }
    
    @Override
    public int getLength() {
        return source.getLength();
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
