/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
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