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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDataProvider implements DataProvider {
    
    private final Object lock = new Object();
    private final DataType dataType;
    private Set<DataEntry> entries;
    
    protected AbstractDataProvider(DataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    @Override
    public final DataType getDataType() {
        return dataType;
    }

    @Override
    public final List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        if(filter == null)
            throw new NullPointerException("Filter is null!");
            
        List<DataEntry> result = new ArrayList<DataEntry>();
        synchronized(lock) {
            for(DataEntry entry : getLoadedEntries())
                if(filter.acceptsEntry(entry))
                    result.add(entry);
        }
        return result;
    }
    
    private Set<DataEntry> getLoadedEntries() throws Exception {
        if(entries == null) {
            try {
                entries = loadEntries();
            } catch (Exception ex) {
                //TODO log error
                throw ex;
            }
        }
        return entries;
    }
    
    protected abstract Set<DataEntry> loadEntries() throws Exception;

    @Override
    public final void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        if(entries == null)
            throw new NullPointerException("Entries is null!");
        if(saveType == null)
            throw new NullPointerException("SaveType is null!");
        
        synchronized(lock) {
            boolean changed = false;
            for(DataEntry entry : entries)
                if(addEntry(entry, saveType))
                    changed = true;
            
            if(changed) {
                saveEntries(entries);
                //TODO publish change event
            }
        }
    }
    
    private boolean addEntry(DataEntry newEntry, SaveType saveType) throws Exception {
        DataEntry oldEntry = getMatchingEntry(newEntry);
        if(oldEntry != null) {
            if(SaveType.SAVE_NEW == saveType) {
                entries.remove(oldEntry);
                //TODO log event
                //TODO publish audit event
                entries.add(newEntry);
                //TODO log event
                //TODO publish audit event
                return true;
            }
            return false;
        } else {
            entries.add(newEntry);
            //TODO log event
            //TODO publish audit event
            return true;
        }
    }
    
    private DataEntry getMatchingEntry(DataEntry entry) throws Exception {
        for(DataEntry e : getLoadedEntries())
            if(e.equals(entry))
                return e;
        return null;
    }
    
    protected abstract void saveEntries(Set<DataEntry> entries) throws Exception;
    
    @Override
    public final void deleteEntries(Set<DataEntry> entries) throws Exception {
        if(entries == null)
            throw new NullPointerException("Entries is null!");
        
        synchronized(lock) {
            getLoadedEntries();
            boolean changed = false;
            for(DataEntry entry : entries) {
                if(this.entries.remove(entry)) {
                    changed = true;
                    //TODO log remove
                    //TODO publis audit event
                }
            }
            
            if(changed) {
                saveEntries(this.entries);
                //TODO publish change event
            }
        }
    }
}
