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

import org.jreserve.jrlib.linkratio.LinkRatio;

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