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
 * Link ratio smoothing, based on the inverse power curve.
 * The formula for the link ratio is:
 *      lr(d) = 1 + a * t ^ (-b),
 *      t = d + 1
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InversePowerLRCurve extends AbstractLinkRatioCurve {
    
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
        int devs = lr.getLength();
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
        return (o instanceof InversePowerLRCurve);
    }
    
    @Override
    public int hashCode() {
        return InversePowerLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "InversePowerLRCurve [LR(t) = 1 + %.5f * t ^ %.5f]",
            pa, pb);
    }
}