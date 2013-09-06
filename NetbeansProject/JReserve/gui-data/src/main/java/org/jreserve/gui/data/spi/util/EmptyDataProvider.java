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
package org.jreserve.gui.data.spi.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.SaveType;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.openide.filesystems.FileObject;
import org.openide.loaders.MultiDataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyDataProvider implements DataProvider {
    private final static EmptyDataProvider INSTANCE = new EmptyDataProvider();

    public final static DataProvider.Factory FACTORY = new DataProvider.Factory() {
        @Override
        public DataProvider createProvider(FileObject primaryFile, Map<String, String> properties) {
            return INSTANCE;
        }
    };
    
    private EmptyDataProvider() {
    }
    
    @Override
    public Map<String, String> getProperties() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        throw new Exception("Can not add entries to EmptyDataProvider!");
    }

    @Override
    public boolean deleteEntries(Set<DataEntry> entries) throws Exception {
        throw new Exception("Can not delete entries from EmptyDataProvider!");
    }

    @Override
    public Set<MultiDataObject.Entry> getSecondaryEntries(MultiDataObject mdo) throws IOException {
        return Collections.EMPTY_SET;
    }
}
