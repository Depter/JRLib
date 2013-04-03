package org.jreserve.estimate;

import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LossRatioEstimateInput extends AbstractMultiSourceCalculationData<CalculationData> implements Copyable<LossRatioEstimateInput> {

    private final LinkRatio lrs;
    private final Vector exposure;
    private final Vector lossRatio;
    
    public LossRatioEstimateInput(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        super(lrs, exposure, lossRatio);
        this.lrs = lrs;
        this.exposure = exposure;
        this.lossRatio = lossRatio;
    }
    
    public Vector getSourceExposure() {
        return exposure;
    }
    
    public double getExposure(int accident) {
        return exposure.getValue(accident);
    }
    
    public LinkRatio getSourceLinkRatio() {
        return lrs;
    }
    
    public int getDevelopmentCount() {
        return lrs.getDevelopmentCount();
    }
    
    public double getLinkRatio(int development) {
        return lrs.getValue(development);
    }
    
    public Vector getSourceLossRatio() {
        return lossRatio;
    }
    
    public double getLossRatio(int accident) {
        return lossRatio.getValue(accident);
    }
    
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
