package org.jreserve.factor.curve;

import org.jreserve.util.RegressionUtil;
import static java.lang.Math.*;
import org.jreserve.factor.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the power curve:
 * <i>a ^ (b ^ t)</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PowerLRFunction implements LinkRatioFunction {
    
    private double pa = 1d;
    private double pb = 1d;
    
    @Override
    public void fit(LinkRatio lr) {
        int devs = lr.getDevelopmentCount();
        double[] x = getXs(devs);
        double[] y = getYs(lr.toArray(), devs);
        double[] params = RegressionUtil.linearRegression(x, y);
        pa = exp(exp(params[RegressionUtil.INTERCEPT]));
        pb = exp(params[RegressionUtil.SLOPE]);
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
            lrs[i] = (Double.isNaN(lr) || !(lr > 1d))? Double.NaN : log(log(lr));
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
        development++;
        return pow(pa, pow(pb, (double) development));
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
