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
 * Calculates the link-ratio for development period `d` as
 * simply takin the smallest non NaN development factor from
 * the development period. This method has a Mack-alpha
 * parameter of 1.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MinLRMethod extends AbstractLRMethod {
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
        double min = Double.NaN;
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            if(Double.isNaN(min) || (!Double.isNaN(factor) && factor < min))
                min = factor;
        }
        return min;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MinLRMethod);
    }
    
    @Override
    public int hashCode() {
        return MinLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MinLRMethod";
    }
}