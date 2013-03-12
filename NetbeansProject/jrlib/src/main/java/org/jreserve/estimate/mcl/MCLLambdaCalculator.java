package org.jreserve.estimate.mcl;

import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MCLLambdaCalculator extends AbstractMultiSourceCalculationData<LinkRatioScale> {
    
    private final static int INCURRED = 0;
    private final static int PAID = 1;
    
    private LinkRatioScale incurredScale;
    private Triangle iCik;
    private LinkRatioScale paidScale;
    private Triangle pCik;
    
    MCLLambdaCalculator(LinkRatioScale incurredScale, LinkRatioScale paidScale) {
        super(incurredScale, paidScale);
        initState();
        doRecalculate();
    }
    
    private void initState() {
        this.incurredScale = sources[INCURRED];
        this.paidScale = sources[PAID];
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        
    
    }
}
