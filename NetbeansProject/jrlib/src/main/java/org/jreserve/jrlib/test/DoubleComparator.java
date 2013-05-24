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
package org.jreserve.jrlib.test;

import java.util.Comparator;

/**
 * The class is a {@link Comparator Comparator} for {@link Double Doubles}.
 * It logically compares the doubles, with the extension for null values.
 * Nulls are considered less than any non null values.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
class DoubleComparator implements Comparator<Double> {
    
    final static Comparator<Double> INSTANCE = new DoubleComparator();
    
    private DoubleComparator() {
    }
    
    @Override
    public int compare(Double o1, Double o2) {
        if(o1 == null)
            return o2 == null ? 0 : 1;
        return o2 == null ? -1 : comparePrimitive(o1, o2);
    }

    private int comparePrimitive(double d1, double d2) {
        if(Double.isNaN(d1))
            return Double.isNaN(d2) ? 0 : 1;
        if(Double.isNaN(d2))
            return -1;
        if(d1 == d2)
            return 0;
        return d1 < d2 ? -1 : 1;
    }
}