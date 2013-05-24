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
 * Calculates the link-ratio for a development period as the
 * weighted average of the development factors in the given
 * development period.
 * 
 * The formula for the link ratio for development period `d`:
 *              sum(c(a,d) * f(a,d))
 *      lr(d) = --------------------
 *                   sum(c(a,d))
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
public class WeightedAverageLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 1d;
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
        double sum = 0d;
        double sw = 0d;
        
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            double weight = cik.getValue(a, dev);
            if(!Double.isNaN(factor) && !Double.isNaN(weight)) {
                sum += (factor * weight);
                sw += weight;
            }
        }
        
        return  (sw != 0d)? sum / sw : Double.NaN;
    }

    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof WeightedAverageLRMethod);
    }
    
    @Override
    public int hashCode() {
        return WeightedAverageLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "WeightedAverageLRMethod";
    }
}
