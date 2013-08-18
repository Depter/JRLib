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
package org.jreserve.gui.data.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.api.inport.SaveType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyDataProvider implements DataProvider {
    
    private final static String ID = "org.jreserve.gui.data.api.impl.EmptyDataProvider";    //NOI18
    final static DataProvider INSTANCE = new EmptyDataProvider();
    
    final static DataProvider.Factory FACTORY = new DataProvider.Factory() {
        @Override 
        public String getId() {
            return ID;
        }

        @Override
        public DataProvider createProvider(Map<String, String> properties) {
            return INSTANCE;
        }
    };
    
    private final static Logger logger = Logger.getLogger(EmptyDataProvider.class.getName());
    
    
    private EmptyDataProvider() {
    }
    
    @Override
    public DataProvider.Factory getFactory() {
        return FACTORY;
    }
    
    @Override
    public void delete() {
    }
    
    @Override
    public void rename(String newName) {
    }
    
    @Override
    public void move(DataCategory newParent) {
    }
    
    @Override
    public Map<String, String> getProperties() {
        logger.warning("Accessing dummy data provider!");
        return Collections.EMPTY_MAP;
    }

    @Override
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        logger.warning("Accessing dummy data provider!");
        return Collections.EMPTY_LIST;
    }

    @Override
    public void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        logger.warning("Accessing dummy data provider!");
    }

    @Override
    public void deleteEntries(Set<DataEntry> entries) throws Exception {
        logger.warning("Accessing dummy data provider!");
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        logger.warning("Accessing dummy data provider!");
    }
}
