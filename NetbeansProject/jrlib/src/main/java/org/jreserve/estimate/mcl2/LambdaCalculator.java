package org.jreserve.estimate.mcl2;

import org.jreserve.CalculationData;
import org.jreserve.estimate.mcl.MclRho;
import org.jreserve.estimate.mcl2.ratio.AbstractRatioScaleInput;
import org.jreserve.linkratio.standarderror.LinkRatioScaleInput;
import org.jreserve.scale.RatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LambdaCalculator extends CalculationData {

    public MclRho getSourceMclRho();
    
    public RatioScale<LinkRatioScaleInput> getSourceLinkRatioScale();
    
    public RatioScale<AbstractRatioScaleInput> getSourceRatioScale();
    
    public double getLambda();
}
