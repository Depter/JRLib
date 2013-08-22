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
package org.jreserve.gui.data.api.util;

import java.util.List;
import java.util.concurrent.Callable;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataEntryLoader implements Callable<List<DataEntry>> {
    
    private final DataSource ds;
    private final DataEntryFilter filter;
    
    public DataEntryLoader(DataSource ds) {
        this(ds, null);
    }
    
    public DataEntryLoader(DataSource ds, DataEntryFilter filter) {
        if(ds == null)
            throw new NullPointerException("DataSource is null!");
        this.ds = ds;
        this.filter = filter==null? DataEntryFilter.ALL : filter;
    }
    
    
    @Override
    public List<DataEntry> call() throws Exception {
        return ds.getEntries(filter);
    }
}
