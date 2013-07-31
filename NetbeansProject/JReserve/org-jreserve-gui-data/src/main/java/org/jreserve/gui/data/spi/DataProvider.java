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

import org.jreserve.gui.data.api.DataType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jreserve.gui.data.api.DataSource;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataProvider {
    
    public final static String PROP_DATA_TYPE = "data.type";
    public final static String PROP_FACTORY_TYPE = "instance.factory.type";
    public final static String PROP_INSTANCE_PATH = "instance.path";
    
    public void delete() throws Exception;
    
    public DataType getDataType();
    
    public DataProviderFactoryType getFactoryType();
    
    public String getInstancePath();
    
    public Map<String, String> getProperties();
    
    public void setProperties(DataSource source, Map<String, String> properties);
    
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception;
    
    public void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception;
    
    public void deleteEntries(Set<DataEntry> entries) throws Exception;

}
