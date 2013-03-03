package org.jreserve.factor.curve;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.util.RegressionUtil;

/**
 * Link ratio smoothing, based on the inverse power curve:
 * <i>1 + a * t ^ (-b)</i>.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InversePowerLRFunction implements LinkRatioFunction {
    
    private double pa = 1d;
    private double pb = 1d;
    
    @Override
    public void fit(LinkRatio lr) {
        int devs = lr.getDevelopmentCount();
        double[] x = getXs(devs);
        double[] y = getYs(lr.toArray(), devs);
        double[] params = RegressionUtil.linearRegression(x, y);
        pa = Math.exp(params[RegressionUtil.SLOPE]);
        pb = params[RegressionUtil.INTERCEPT];
    }
    
    private double[] getXs(int devs) {
        double[] x = new double[devs];
        for(int i=0; i<devs; i++)
            x[i] = Math.log((double) (i+1));
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
        development++;
        return 1d + pa * Math.pow(development, pb);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof InversePowerLRFunction);
    }
    
    @Override
    public int hashCode() {
        return InversePowerLRFunction.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "InversePowerLR [LR(t) = 1 + %.5f * t ^ %.5f]",
            pa, pb);
    }
}
