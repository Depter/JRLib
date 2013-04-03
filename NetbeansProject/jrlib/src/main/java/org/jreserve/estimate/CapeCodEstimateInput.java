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
public class CapeCodEstimateInput extends AbstractMultiSourceCalculationData<CalculationData> implements Copyable<CapeCodEstimateInput> {

    private LinkRatio lrs;
    private Vector exposure;
    
    public CapeCodEstimateInput(LinkRatio lrs, Vector exposure) {
        super(lrs, exposure);
        this.lrs = lrs;
        this.exposure = exposure;
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
