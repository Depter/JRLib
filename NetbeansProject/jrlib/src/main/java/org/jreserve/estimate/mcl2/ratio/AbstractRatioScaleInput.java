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
    
    public abstract boolean isPaidInput();
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public int getAccidentCount() {
        return ratios.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return ratios.getDevelopmentCount(accident);
    }
}
