package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * The class calculates the standard chain-ladder reserve estimates. The
 * input claim triangle is completed as desribed ar
 * {@link EstimateUtil#completeTriangle(ClaimTriangle, LinkRatio) EstimateUtil.completeTriangle()}.
 * 
 * @see EstimateUtil#completeTriangle(ClaimTriangle, LinkRatio) 
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
        developments = lrs.getLength()+1;
        values = EstimateUtil.completeTriangle(ciks, lrs);
    }
}