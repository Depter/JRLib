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

package org.jreserve.gui.data.spi;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataProvider {
    
    public static enum DataType {
        TRIANGLE,
        VECTOR;
    }
    
    public static enum SaveType {
        OVERRIDE_EXISTING,
        SAVE_NEW;
    }
    
    public DataType getDataType();
    
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception;
    
    public void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception;
    
    public void deleteEntries(Set<DataEntry> entries) throws Exception;
}
