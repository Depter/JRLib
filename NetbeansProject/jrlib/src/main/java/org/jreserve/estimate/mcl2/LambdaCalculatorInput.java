package org.jreserve.estimate.mcl2;

import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.CalculationData;
import org.jreserve.estimate.mcl2.ratio.AbstractRatioScaleInput;
import org.jreserve.linkratio.standarderror.LinkRatioScaleInput;
import org.jreserve.scale.residuals.RatioScaleResidualTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LambdaCalculatorInput extends AbstractMultiSourceCalculationData<RatioScaleResidualTriangle> {
    
    private RatioScaleResidualTriangle<LinkRatioScaleInput> lrResiduals;
    private RatioScaleResidualTriangle<AbstractRatioScaleInput> ratioResiduals;
    
    public LambdaCalculatorInput(RatioScaleResidualTriangle<LinkRatioScaleInput> lrResiduals, RatioScaleResidualTriangle<AbstractRatioScaleInput> ratioResiduals) {
        super(lrResiduals, ratioResiduals);
        this.lrResiduals = lrResiduals;
        this.ratioResiduals = ratioResiduals;
    }
    
    public RatioScaleResidualTriangle<LinkRatioScaleInput> getSourceLRResiduals() {
        return lrResiduals;
    }
    
    public LinkRatioScaleInput getSourceLRInput() {
        return lrResiduals.getSourceInput();
    }
    
    public RatioScaleResidualTriangle<AbstractRatioScaleInput> getSourceRatioResiduals() {
        return ratioResiduals;
    }
    
    public AbstractRatioScaleInput getSourceRatioInput() {
        return ratioResiduals.getSourceInput();
    }
    
    public boolean isPaidInput() {
        return ratioResiduals.getSourceInput().isPaidInput();
    }
    
    @Override
    protected void recalculateLayer() {
    }
}
