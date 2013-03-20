package org.jreserve.linkratio.smoothing;

import static java.lang.Math.*;
import org.jreserve.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the Weibul curve:
 * <i>1 / (1-e^(-a * (b ^ t)))</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeibulLRFunction extends AbstractLinkRatioFunction {
    
    @Override
    public void fit(LinkRatio lr) {
        super.fit(lr);
        pa = exp(intercept);
        pb = exp(slope);
    }

    @Override
    protected double getY(double lr) {
        if(Double.isNaN(lr) || !(lr > 1d))
            return Double.NaN;
        return log(log(lr / (lr - 1d)));
    }

    @Override
    public double getValue(int development) {
        double x = (double) (development + 1);
        return 1d / (1d - exp(-pa * pow(pb, x)));
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
    
    @Override
    public WeibulLRFunction copy() {
        WeibulLRFunction copy = new WeibulLRFunction();
        copy.copyStateFrom(this);
        return copy;
    }
}
