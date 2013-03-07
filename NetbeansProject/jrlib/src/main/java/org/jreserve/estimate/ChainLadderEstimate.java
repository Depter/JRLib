package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChainLadderEstimate extends AbstractEstimate {

    private LinkRatio lrs;
    private Triangle ciks;

    public ChainLadderEstimate(LinkRatio lrs) {
        initLinkRatios(lrs);
        doRecalculate();
    }
    
    private void initLinkRatios(LinkRatio lrs) {
        attachSource(lrs);
        this.lrs = lrs;
        this.ciks = lrs.getSourceFactors().getSourceTriangle();
    }

    @Override
    protected int getObservedDevelopmentCount(int accident) {
        return ciks.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateSource() {
        recalculateSource(lrs);
    }

    @Override
    protected void detachSource() {
        detachSource(lrs);
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
}
