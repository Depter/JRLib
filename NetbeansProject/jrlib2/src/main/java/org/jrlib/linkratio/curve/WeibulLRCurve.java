package org.jrlib.linkratio.curve;

import static java.lang.Math.*;
import org.jrlib.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the Weibul curve.
 * The formula for the link ratio is:
 *                       1
 *       lr(d) = -------------------
 *               1-exp(-a * (b ^ t))
 *       t = d + 1
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeibulLRCurve extends AbstractLinkRatioCurve {
    
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
        return (o instanceof WeibulLRCurve);
    }
    
    @Override
    public int hashCode() {
        return WeibulLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "WeibulLRCurve [LR(t) = 1 / (1 - e^(-%.5f * t^%.5f))]",
            pa, pb);
    }
    
    @Override
    public WeibulLRCurve copy() {
        WeibulLRCurve copy = new WeibulLRCurve();
        copy.copyStateFrom(this);
        return copy;
    }
}
