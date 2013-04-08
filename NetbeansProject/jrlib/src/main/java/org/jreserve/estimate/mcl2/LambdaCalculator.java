package org.jreserve.estimate.mcl2;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LambdaCalculator extends CalculationData {

    public LambdaCalculatorInput getSourceInput();
    
    public double getLambda();
}
