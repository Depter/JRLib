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
package org.jreserve.jrlib.linkratio;

/**
 * Calculates the development factors according to the suggestion of Mack. 
 * The formula used to calculate the link-ratio for development 
 * period `d`:
 *      
 *              sum(c(i,k)^2 * f(i,k))
 *      lr(d) = ----------------------
 *                  sum(c(i,k)^2)
 * where:
 * -   `f(a,d)` is the development factor for accident period `a` and 
 *     development period `d`.
 * -   `c(a,d)` is the claim for accident period `a` and development 
 *     period `d`.
 * -   if either `f(a,d)` or `c(a,d)` is NaN, then the cell is ignored.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MackRegressionLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 2d;
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
        double ss = 0d;
        double s = 0d;
        for(int a=0; a<accidents; a++) {
            double c2 = Math.pow(cik.getValue(a, dev), MACK_ALPHA);
            double f = factors.getValue(a, dev);
            if(!Double.isNaN(c2) && !Double.isNaN(f)) {
                ss += (c2 * f);
                s += c2;
            }
        }
        return s==0d? Double.NaN : ss/s;
    }

    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MackRegressionLRMethod);
    }
    
    @Override
    public int hashCode() {
        return MackRegressionLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MackRegressionLRMethod";
    }
}