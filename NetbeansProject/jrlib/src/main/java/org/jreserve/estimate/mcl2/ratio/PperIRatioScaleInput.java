package org.jreserve.estimate.mcl2.ratio;

import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PperIRatioScaleInput extends AbstractRatioScaleInput {
    
    private ClaimTriangle iik;
    
    public PperIRatioScaleInput(Ratio ratio) {
        super(ratio);
        iik = ratio.getSourceIncurredTriangle();
    }
    
    @Override
    public boolean isPaidInput() {
        return false;
    }

    @Override
    public double getRatio(int development) {
        return source.getPperI(development);
    }

    @Override
    public double getRatio(int accident, int development) {
        return ratios.getPperI(accident, development);
    }

    @Override
    public double getWeight(int accident, int development) {
        return iik.getValue(accident, development);
    }

    @Override
    public PperIRatioScaleInput copy() {
        return new PperIRatioScaleInput(source.copy());
    }

    @Override
    protected void recalculateLayer() {
    }
}
