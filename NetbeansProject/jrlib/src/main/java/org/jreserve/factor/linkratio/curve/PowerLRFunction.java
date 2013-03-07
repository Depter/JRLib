package org.jreserve.factor.linkratio.curve;

import static java.lang.Math.*;
import org.jreserve.factor.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the power curve:
 * <i>a ^ (b ^ t)</i>.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PowerLRFunction extends AbstractLinkRatioFunction {
    
    @Override
    public void fit(LinkRatio lr) {
        super.fit(lr);
        pa = exp(exp(intercept));
        pb = exp(slope);
    }

    @Override
    protected double getY(double lr) {
        if(Double.isNaN(lr) || !(lr > 1d))
            return Double.NaN;
        return log(log(lr));
    }

    @Override
    public double getValue(int development) {
        double x = (double) (development + 1);
        return pow(pa, pow(pb, x));
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
