package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the exponential curve:
 * <i>1 + a * e ^ (-b * t)</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialLRFunction extends AbstractLinkRatioFunction {
    
    @Override
    public void fit(LinkRatio lr) {
        super.fit(lr);
        pa = Math.exp(intercept);
        pb = -slope;
    }
    
    @Override
    protected double getY(double lr) {
        if(Double.isNaN(lr) || !(lr > 1d))
            return Double.NaN;
        return Math.log(lr - 1d);
    }

    @Override
    public double getValue(int development) {
        double x = (double) (development+1);
        return 1d + pa * Math.exp(-pb * x);
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
            intercept, slope);
    }
    
    @Override
    public ExponentialLRFunction copy() {
        ExponentialLRFunction copy = new ExponentialLRFunction();
        copy.copyStateFrom(this);
        return copy;
    }
}
