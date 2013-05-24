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
package org.jreserve.jrlib.util.method;

/**
 * This interface abstracts the capabality to include/exclude
 * specific elements from an input in a curve fitting process.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Excludeable {
    /**
     * Excludes/Includes the element at the given index from the fitting 
     * procedure.
     */
    public void setExcluded(int index, boolean excluded);
    
    /**
     * Returns `true` if the given element is included in the
     * fitting procedure.
     */
    public boolean isExcluded(int index);    
}
