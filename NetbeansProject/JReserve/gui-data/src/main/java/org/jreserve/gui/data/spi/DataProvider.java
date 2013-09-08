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

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jreserve.gui.data.spi.inport.SaveType;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataProvider {
    
    public final static String PROP_DATA_TYPE = "data.type";
    public final static String PROP_FACTORY_ID = "factory.id";
    
    public Map<String, String> getProperties();
    
    public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception;
    
    public boolean addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception;
    
    public boolean deleteEntries(Set<DataEntry> entries) throws Exception;
    
    public Set<FileObject> getSecondryFiles(FileObject primary) throws IOException;
    
    public static interface Factory {
        
        public DataProvider createProvider(FileObject primaryFile, Map<String, String> properties);
    }
    
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public static @interface FactoryRegistration {
        public String id();
    }
}
