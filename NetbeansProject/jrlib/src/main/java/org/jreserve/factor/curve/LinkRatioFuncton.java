package org.jreserve.factor.curve;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.jreserve.factor.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioFuncton {

    public void fit(LinkRatio lr);
    
    public double getValue(int development);
}
