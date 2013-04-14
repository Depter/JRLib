package org.jrlib.estimate;

import org.jrlib.AbstractMultiSourceCalculationData;
import org.jrlib.CalculationData;
import org.jrlib.Copyable;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.vector.Vector;

/**
 * Utility class for {@link CapeCodeEstimate CapeCodeEstimates}. This class
 * wrapes the input calculations used by the estimate.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstimateInput extends AbstractMultiSourceCalculationData<CalculationData> implements Copyable<CapeCodEstimateInput> {

    private LinkRatio lrs;
    private Vector exposure;
    
    /**
     * Creates in instance with the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public CapeCodEstimateInput(LinkRatio lrs, Vector exposure) {
        super(lrs, exposure);
        this.lrs = lrs;
        this.exposure = exposure;
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
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public CapeCodEstimateInput copy() {
        return new CapeCodEstimateInput(
                lrs.copy(), 
                exposure.copy()
                );
    }
}