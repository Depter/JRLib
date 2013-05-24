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
package org.jreserve.jrlib;

/**
 * Classes (calculations), marked wiht this interface allow to change their
 * source data after they are initialized.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface MutableSource<T> {
    
    /**
     * Sets the source for the given isntance. It depends on the 
     * implementations, how <i>null</i> is handled (ie. throwing
     * a NullPointerException, doing noting, etc.).
     * 
     * For {@link CalculationData CalculationDatas} calling this
     * method should fire a ChangeEvent.
     */
    public void setSource(T source);
}
