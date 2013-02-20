package org.jreserve.factor.curve;

import org.jreserve.util.RegressionUtil;
import org.jreserve.factor.LinkRatio;

/**
 * Link ratio smoothing, based on the exponential curve:
 * <i>1 + a * e ^ (-b * t)</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialLRFunction implements LinkRatioFunction {
    
    private double pa = 1d;
    private double pb = 1d;

    @Override
    public void fit(LinkRatio lr) {
        int devs = lr.getDevelopmentCount();
        double[] x = getXs(devs);
        double[] y = getYs(lr.toArray(), devs);
        double[] params = RegressionUtil.linearRegression(x, y);
        pa = Math.exp(params[RegressionUtil.INTERCEPT]);
        pb = -params[RegressionUtil.SLOPE];
    }
    
    private double[] getXs(int devs) {
        double[] x = new double[devs];
        for(int i=0; i<devs; i++)
            x[i] = (double) (i+1);
        return x;
    }
    
    private double[] getYs(double[] lrs, int devs) {
        for(int i=0; i<devs; i++) {
            double lr = lrs[i];
            lrs[i] = (Double.isNaN(lr) || !(lr > 1d))? Double.NaN : Math.log(lr - 1d);
        }
        return lrs;
    }
    
    public double getA() {
        return pa;
    }
    
    public double getB() {
        return pb;
    }

    @Override
    public double getValue(int development) {
        return 1d + pa * Math.exp(-pb * (double) development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ExponentialLRFunction);
    }
    
    @Override
    public int hashCode() {
        return ExponentialLRFunction.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "ExponentialLR [LR(t) = 1 + %.5f * e ^ (-%.5f * t)]",
            pa, pb);
    }
}
