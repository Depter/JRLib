package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the exponential curve.
 * The formula for the link ratio is:
 *      lr(d) = 1 + a * exp(-b * t),
 *      t = d + 1
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialLRCurve extends AbstractLinkRatioCurve {
    
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
        return (o instanceof ExponentialLRCurve);
    }
    
    @Override
    public int hashCode() {
        return ExponentialLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "ExponentialLR [LR(t) = 1 + %.5f * e ^ (-%.5f * t)]",
            intercept, slope);
    }
}