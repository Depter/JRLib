package org.jreserve.estimate.mcl2.ratio;

import org.jreserve.AbstractCalculationData;
import org.jreserve.scale.RatioScaleInput;
import org.jreserve.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRatioScaleInput extends AbstractCalculationData<Ratio> implements RatioScaleInput {
    
    protected RatioTriangle ratios;
    
    protected AbstractRatioScaleInput(Ratio source) {
        super(source);
        ratios = source.getSourceRatioTriangle();
    }
    
    @Override
    protected void recalculateLayer() {
    }

    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    public int getAccidentCount(int development) {
        return ratios.getAccidentCount();
    }
}
