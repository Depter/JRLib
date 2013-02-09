package org.jreserve.factor.curve;

import static java.lang.Math.*;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;
import org.jreserve.factor.LinkRatio;

/**
 * Link ratio smoothing, based on the Weibul curve:
 * <i>1 / (1-e^(-a * x ^ b))</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeibulLRFunction implements LinkRatioFuncton, ParametricUnivariateFunction {
    
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
        return 1d / (1d - exp(-pa * pow((double)development, pb)));
    }

    @Override
    public double value(double x, double... parameters) {
        double a = parameters[INDEX_A];
        double b = parameters[INDEX_B];
        return 1d / (1d - exp(-a * pow(x, b)));
    }

    @Override
    public double[] gradient(double x, double... parameters) {
        double a = parameters[INDEX_A];
        double b = parameters[INDEX_B];
        
        double[] gradient = new double[2];
        double xPb = pow(x, b);
        double exPb = exp(a * xPb);
        double exPb2 = pow(exPb - 1d , 2d);
        //-(x^b * e^(a * x^b))/(e^(a * x^b)-1)^2
        gradient[INDEX_A] = -(xPb * exPb) / exPb2;
        // -(a * x^b * log(x)* e^(a* x^b))/(e^(a* x^b)-1)^2
        gradient[INDEX_B] = -(a * xPb * log(x) * exPb / exPb2);
        
        return gradient;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof WeibulLRFunction);
    }
    
    @Override
    public int hashCode() {
        return WeibulLRFunction.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "WeibulLR [LR(t) = 1 / (1 - e^(-%.5f * t^%.5f))]",
            pa, pb);
    }
}
