package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.scale.ScaleEstimator;
import org.jreserve.jrlib.scale.SimpleScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

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
     * Creates an instance for the given source. The instance will
     * use a {@link org.jreserve.jrlib.scale.MinMaxScaleEstimator MinMaxScaleEstimator}
     * to fill in NaN values.
     * 
     * @see LinkRatioScaleInput#LinkRatioScaleInput(LinkRatio) 
     * @see #SimpleLinkRatioScale(LinkRatioScaleInput) 
     * @see org.jreserve.jrlib.scale.MinMaxScaleEstimator
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
     * Creates an instance for the given source. The instance will
     * use a {@link org.jreserve.jrlib.scale.MinMaxScaleEstimator MinMaxScaleEstimator}
     * to fill in NaN values.
     * 
     * @see SimpleScale#SimpleScale(org.jreserve.jrlib.scale.ScaleInput) 
     * @see org.jreserve.jrlib.scale.MinMaxScaleEstimator
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
     * Creates an instance for the given source. The instance will
     * use a {@link org.jreserve.jrlib.scale.MinMaxScaleEstimator MinMaxScaleEstimator}
     * to fill in NaN values.
     * 
     * @see SimpleScale#SimpleScale(org.jreserve.jrlib.scale.Scale) 
     * @see org.jreserve.jrlib.scale.MinMaxScaleEstimator
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
