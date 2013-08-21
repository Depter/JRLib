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
package org.jreserve.gui.data.api;

import org.jreserve.gui.data.api.inport.SaveType;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import java.util.List;
import java.util.Set;
import org.jreserve.gui.data.spi.DataProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataSource extends DataItem {
    
    public final static String MIME_TYPE = "application/jreserve-data-source";
    
    public DataType getDataType();

    public DataProvider getDataProvider();
    
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception;
    
    public void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception;
    
    public void deleteEntries(Set<DataEntry> entries) throws Exception;
}
