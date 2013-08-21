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

package org.jreserve.gui.poi.read;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TableFactory<T> {

    public void numberFound(int row, short column, double value) throws Exception;
    
    public void stringFound(int row, short column, String value) throws Exception;
    
    public void booleanFound(int row, short column, boolean value) throws Exception;
    
    public void errorFound(int row, short column, String value) throws Exception;
    
    public T getTable() throws Exception;
}
