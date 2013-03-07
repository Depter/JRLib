package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the inverse power curve:
 * <i>1 + a * t ^ (-b)</i>.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InversePowerLRFunction extends AbstractLinkRatioFunction {
    
    @Override
    public void fit(LinkRatio lr) {
        super.fit(lr);
        pa = Math.exp(slope);
        pb = intercept;
    }

    @Override
    protected double getY(double lr) {
        if(Double.isNaN(lr) || !(lr > 1d))
            return Double.NaN;
        return Math.log(lr - 1d);
    }
    
    @Override
    protected double[] getXs(LinkRatio lr) {
        int devs = lr.getDevelopmentCount();
        double[] x = new double[devs];
        for(int i=0; i<devs; i++)
            x[i] = Math.log((double) (i+1));
        return x;
    }

    @Override
    public double getValue(int development) {
        double x = (double) (development + 1);
        return 1d + pa * Math.pow(x, pb);
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
