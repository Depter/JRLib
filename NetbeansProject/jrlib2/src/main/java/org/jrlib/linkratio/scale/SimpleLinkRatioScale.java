package org.jrlib.linkratio.scale;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.scale.Scale;
import org.jrlib.scale.ScaleEstimator;
import org.jrlib.scale.SimpleScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 * Basic implementation for the {@link LinkRatioScale LinkRatioScale} interface
 * which fills in the missing values (NaNs) with a preset
 * {@link ScaleEstimator ScaleEstimator}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioScale extends SimpleScale<LinkRatioScaleInput> implements LinkRatioScale {

    private LinkRatioScaleInput sourceInput;
    
    /**
     * Creates an instance for the given source.
     * 
     * @see LinkRatioScaleInput#LinkRatioScaleInput(LinkRatio) 
     * @see #SimpleLinkRatioScale(LinkRatioScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleLinkRatioScale(LinkRatio source) {
        this(new LinkRatioScaleInput(source));
    }
    
    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see LinkRatioScaleInput#LinkRatioScaleInput(LinkRatio) 
     * @see #SimpleLinkRatioScale(LinkRatioScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleLinkRatioScale(LinkRatio source, ScaleEstimator<LinkRatioScaleInput> estimator) {
        this(new LinkRatioScaleInput(source), estimator);
    }
    
    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jrlib.scale.ScaleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleLinkRatioScale(LinkRatioScaleInput source) {
        super(source);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(ScaleInput, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleLinkRatioScale(LinkRatioScaleInput source, ScaleEstimator<LinkRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source;
    }

    /**
     * Creates an instance for the given source.
     * 
     * @see SimpleScale#SimpleScale(org.jrlib.scale.Scale) 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleLinkRatioScale(Scale<LinkRatioScaleInput> source) {
        super(source);
        this.sourceInput = source.getSourceInput();
    }

    /**
     * Creates an instance for the given source and estimator.
     * 
     * @see SimpleScale#SimpleScale(Scale, ScaleEstimator) 
     * @throws NullPointerException if `source` or `estimator` is null.
     */
    public SimpleLinkRatioScale(Scale<LinkRatioScaleInput> source, ScaleEstimator<LinkRatioScaleInput> estimator) {
        super(source, estimator);
        this.sourceInput = source.getSourceInput();
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
