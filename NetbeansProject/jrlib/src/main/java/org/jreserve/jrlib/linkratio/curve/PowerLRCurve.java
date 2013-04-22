package org.jreserve.jrlib.linkratio.curve;

import static java.lang.Math.*;
import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 * Link ratio smoothing, based on the power curve.
 * The formula for the link ratio is:
 *      lr(d) = a ^ (b ^ t)
 *      t = d + 1
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PowerLRCurve extends AbstractLinkRatioCurve {
    
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
        return (o instanceof PowerLRCurve);
    }
    
    @Override
    public int hashCode() {
        return PowerLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "PowerLRCurve [LR(t) = %.5f ^ %.5f ^ t]",
            pa, pb);
    }
}