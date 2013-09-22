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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jreserve.gui.data.spi.inport.SaveType;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.DataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.EmptyDataSource.Triangle.Name=Unknown Storage",
    "LBL.EmptyDataSource.Vector.Name=Unknown Storage"
})
class EmptyDataSource implements DataSource {

    private final DataType dataType;

    EmptyDataSource(DataType dataType) {
        this.dataType = dataType;
    }
    
    @Override
    public String getName() {
        if(DataType.TRIANGLE == dataType)
            return Bundle.LBL_EmptyDataSource_Triangle_Name();
        return Bundle.LBL_EmptyDataSource_Vector_Name();
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        return false;
    }

    @Override
    public boolean deleteEntries(Set<DataEntry> entries) throws Exception {
        return false;
    }
}
