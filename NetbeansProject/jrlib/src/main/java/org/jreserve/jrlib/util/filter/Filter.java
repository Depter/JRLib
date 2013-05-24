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
package org.jreserve.jrlib.util.filter;

/**
 * Implementations of the <b>Filter</b> interface are able to filter an input 
 * array on some predefined criteria.
 * 
 * Filtering can be used to mark outlieres in a triangle or in a vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Filter {
    
    /**
     * Returns a boolean array <b>b</b> whith the same length as <i>x</i>, 
     * where b[i] is true if x[i] is filtered.
     * 
     * @param x the input to filter. May contain NaNs, not null.
     * @return the filter array.
     * @throws NullPointerException When x is <i>null</i>.
     */
    public boolean[] filter(double[] x);

}
