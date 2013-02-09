package org.jreserve.factor.curve;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;
import org.jreserve.factor.LinkRatio;

/**
 * Link ratio smoothing, based on the power curve:
 * <i>a ^ b ^ t</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PowerLRFunction implements LinkRatioFuncton, ParametricUnivariateFunction {
    
    private final static int INDEX_A = 0;
    private final static int INDEX_B = 1;
    
    private double pa = 1d;
    private double pb = 1d;
    
    @Override
    public void fit(LinkRatio lr) {
        CurveFitter fitter = createFitter(lr);
        double[] params = fitter.fit(this, new double[]{pa, pb});
        pa = params[INDEX_A];
        pb = params[INDEX_B];
    }
    
    private CurveFitter createFitter(LinkRatio lr) {
        CurveFitter fitter = new CurveFitter(new LevenbergMarquardtOptimizer());
        double[] lrs = lr.toArray();
        int size = lrs.length;
        for(int i=0; i<size; i++)
            fitter.addObservedPoint(i+1, lrs[i]);
        return fitter;
    } 
    
    public double getA() {
        return pa;
    }
    
    public double getB() {
        return pb;
    }

    @Override
    public double getValue(int development) {
        return pow(pa, pow(pb, (double) development));
    }

    @Override
    public double value(double x, double... parameters) {
        double a = parameters[INDEX_A];
        double b = parameters[INDEX_B];
        return pow(a, pow(b, x));
    }

    @Override
    public double[] gradient(double x, double... parameters) {
        double a = parameters[INDEX_A];
        double b = parameters[INDEX_B];
        
        double[] gradient = new double[2];
        //(b^x) * a ^ (b^x-1)
        gradient[INDEX_A] = pow(b, x) * pow(a, pow(b, x) - 1d);
        // x*b^(x-1)*ln(a)*a^(b^x)
        gradient[INDEX_B] = x*pow(b, x-1d) * log(a) * pow(a, pow(b, x));
        
        return gradient;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof PowerLRFunction);
    }
    
    @Override
    public int hashCode() {
        return PowerLRFunction.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "PowerLR [LR(t) = %.5f ^ %.5f ^ t]",
            pa, pb);
    }
}
