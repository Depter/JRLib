package org.jreserve.factor.linkratio;

import java.util.Arrays;
import org.jreserve.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRMethod extends AbstractVectorUserInput implements LinkRatioMethod {
    
    public final static double MACK_ALPHA = 1d;
    
    public UserInputLRMethod() {
    }
    
    public UserInputLRMethod(double[] values) {
        super(values);
    }
    
    @Override
    public double[] getLinkRatios(FactorTriangle factors) {
        int developments = factors.getDevelopmentCount();
        double[] result = createValues(developments);
        fillValues(result);
        return result;
    }
    
    private double[] createValues(int developments) {
        double[] result = new double[developments];
        Arrays.fill(result, Double.NaN);
        return result;
    }

    private void fillValues(double[] result) {
        int maxDev = Math.min(values.length, result.length);
        System.arraycopy(values, 0, result, 0, maxDev);
    }
    
    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }
}