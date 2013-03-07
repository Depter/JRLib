package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRMethod extends AbstractVectorUserInput implements LinkRatioMethod {
    
    public final static double MACK_ALPHA = 0d;
    
    public UserInputLRMethod() {
    }
    
    public UserInputLRMethod(double[] values) {
        super(values);
    }
    
    @Override
    public void fit(FactorTriangle factors) {
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= values.length)
            return Double.NaN;
        return values[development];
    }
    
    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }

    public double getWeight(int accident, int development) {
        return 1d;
    }
}