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

package org.jreserve.dummy.plot;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotEntry<T extends Comparable> implements Comparable<PlotEntry<T>> {

    private final T key;
    private final double value;

    PlotEntry(T key, double value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }
    
    @Override
    public int compareTo(PlotEntry<T> o) {
        return key.compareTo(o.key);
    }

}
