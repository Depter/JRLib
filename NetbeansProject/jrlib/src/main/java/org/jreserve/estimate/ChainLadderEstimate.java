package org.jreserve.estimate;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChainLadderEstimate extends AbstractEstimate<LinkRatio> {

    private LinkRatio lrs;
    private ClaimTriangle ciks;

    public ChainLadderEstimate(LinkRatio lrs) {
        super(lrs);
        this.lrs = lrs;
        this.ciks = lrs.getSourceTriangle();
        doRecalculate();
    }

    @Override
    public int getObservedDevelopmentCount(int accident) {
        return ciks.getDevelopmentCount(accident);
    }

    public LinkRatio getSourceLinkRatios() {
        return lrs;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        accidents = ciks.getAccidentCount();
        developments = lrs.getDevelopmentCount()+1;
        values = EstimateUtil.completeTriangle(ciks, lrs);
    }
    
    @Override
    public ChainLadderEstimate copy() {
        return new ChainLadderEstimate(lrs.copy());
    }
}
