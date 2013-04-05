package org.jreserve.estimate.mcl2.ratio;

import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IperPRatioScaleInput extends AbstractRatioScaleInput {
    
    private ClaimTriangle pik;
    
    public IperPRatioScaleInput(Ratio ratio) {
        super(ratio);
        pik = ratio.getSourcePaidTriangle();
    }

    @Override
    public double getRatio(int development) {
        return source.getIperP(development);
    }

    @Override
    public double getRatio(int accident, int development) {
        return ratios.getIperP(accident, development);
    }

    @Override
    public double getWeight(int accident, int development) {
        return pik.getValue(accident, development);
    }

    @Override
    public IperPRatioScaleInput copy() {
        return new IperPRatioScaleInput(source.copy());
    }

    @Override
    protected void recalculateLayer() {
    }
}
