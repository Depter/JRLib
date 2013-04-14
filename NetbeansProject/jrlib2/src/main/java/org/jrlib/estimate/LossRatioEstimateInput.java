package org.jrlib.estimate;

import org.jrlib.AbstractMultiSourceCalculationData;
import org.jrlib.CalculationData;
import org.jrlib.Copyable;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.vector.Vector;

/**
 * LossRatioEstimateInput is the common calculation source for 
 * {@link Estimate Estimates} based on loss-ratio principles. It's simply
 * bundles the {@link LinkRatio LinkRatios} and the {@link Vector Vectos}
 * containing the exposure and the loss-ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class LossRatioEstimateInput extends AbstractMultiSourceCalculationData<CalculationData> implements Copyable<LossRatioEstimateInput> {

    private final LinkRatio lrs;
    private final Vector exposure;
    private final Vector lossRatio;
    
    /**
     * Creates an instance with the given input.
     * 
     * @throws NullPointerException if either of the parameters is null.
     */
    public LossRatioEstimateInput(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        super(lrs, exposure, lossRatio);
        this.lrs = lrs;
        this.exposure = exposure;
        this.lossRatio = lossRatio;
    }
    
    /**
     * Returns the number of development periods.
     * 
     * @see LinkRatio#getLength() 
     */
    public int getDevelopmentCount() {
        return lrs.getLength();
    }
    
    /**
     * Returns the {@link Vector Vector} containing the exposures
     * for each accident period.
     */
    public Vector getSourceExposure() {
        return exposure;
    }
    
    /**
     * Returns the exposure for the given accident period.
     * 
     * @see Vector#getValue(int) 
     */
    public double getExposure(int accident) {
        return exposure.getValue(accident);
    }
    
    /**
     * Returns the {@link LinkRatio LinkRatio} containing the link-ratios.
     */
    public LinkRatio getSourceLinkRatio() {
        return lrs;
    }
    
    /**
     * Returns the link-ratio for the given development period.
     * 
     * @see LinkRatio#getValue(int) 
     */
    public double getLinkRatio(int development) {
        return lrs.getValue(development);
    }
    
    /**
     * Returns the {@link Vector Vector} containing the loss-ratios
     * for each accident period.
     */
    public Vector getSourceLossRatio() {
        return lossRatio;
    }
    
    /**
     * Returns the loss-ratio for the given accident period.
     * 
     * @see Vector#getValue(int) 
     */
    public double getLossRatio(int accident) {
        return lossRatio.getValue(accident);
    }
    
    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public LossRatioEstimateInput copy() {
        return new LossRatioEstimateInput(
                lrs.copy(), 
                exposure.copy(),
                lossRatio.copy()
                );
    }
}