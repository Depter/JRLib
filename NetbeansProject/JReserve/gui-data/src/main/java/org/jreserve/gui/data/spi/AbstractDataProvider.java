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

import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.gui.data.api.inport.SaveType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.AbstractDataProvider.Imported.OnlyNew=Data imported into ''{0}'', keeping old values!",
    "# {0} - path",
    "MSG.AbstractDataProvider.Imported.Overwrite=Data imported into ''{0}'', overwriting old values!",
    "# {0} - path",
    "MSG.AbstractDataProvider.Deleted=Data deleted from ''{0}''!"
})
public abstract class AbstractDataProvider implements DataProvider {
    
    private final DataProvider.Factory factory;
    private DataSource ds;
    private Set<DataEntry> entries;
    
    protected AbstractDataProvider(DataProvider.Factory factory) {
        if(factory == null)
            throw new NullPointerException("Factory is null!");
        this.factory = factory;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return Collections.EMPTY_MAP;
    }
    
    @Override
    public synchronized void setDataSource(DataSource dataSource) {
        if(dataSource == null)
            throw new NullPointerException("DataSource is null!");
        if(ds != null)
            throw new IllegalStateException("DataSource already set!");
        this.ds = dataSource;
    }
    
    protected synchronized DataSource getDataSource() {
        return ds;
    }
    
    @Override
    public final DataProvider.Factory getFactory() {
        return factory;
    }

    @Override
    public synchronized final List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
        if(ds == null)
            throw new IllegalStateException("DataSource not set!");
        if(filter == null)
            filter = DataEntryFilter.ALL;
        
        List<DataEntry> result = new ArrayList<DataEntry>();
        for(DataEntry entry : getLoadedEntries())
            if(filter.acceptsEntry(entry))
                result.add(entry);
        return result;
    }
    
    private Set<DataEntry> getLoadedEntries() throws Exception {
        if(entries == null)
            entries = new TreeSet<DataEntry>(loadEntries());
        return entries;
    }
    
    protected abstract Set<DataEntry> loadEntries() throws Exception;

    @Override
    public synchronized final void addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
        if(entries == null)
            throw new NullPointerException("Entries is null!");
        if(saveType == null)
            throw new NullPointerException("SaveType is null!");
        if(ds == null)
            throw new IllegalStateException("DataSource not set!");
        
        boolean changed = false;
        for(DataEntry entry : entries)
            if(addEntry(entry, saveType))
                changed = true;
            
        if(changed) {
            saveEntries(entries);
            String path = this.getDataSource().getPath();
            String msg = SaveType.SAVE_NEW == saveType?
                    Bundle.MSG_AbstractDataProvider_Imported_OnlyNew(path) :
                    Bundle.MSG_AbstractDataProvider_Imported_Overwrite(path);
            publishDataChange(msg);
        }
    }
    
    private void publishDataChange(String msg) {
        DataEvent.DataChangeEvent evt = new DataEvent.DataChangeEvent(getDataSource(), msg);
        EventBusManager.getDefault().publish(evt);
    }
    
    private boolean addEntry(DataEntry newEntry, SaveType saveType) throws Exception {
        DataEntry oldEntry = getMatchingEntry(newEntry);
        if(oldEntry != null) {
            if(SaveType.SAVE_NEW == saveType) {
                entries.remove(oldEntry);
                entries.add(newEntry);
                return true;
            }
            return false;
        } else {
            entries.add(newEntry);
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
    public synchronized final void deleteEntries(Set<DataEntry> entries) throws Exception {
        if(entries == null)
            throw new NullPointerException("Entries is null!");
        if(ds == null)
            throw new IllegalStateException("DataSource not set!");
        
        getLoadedEntries();
        boolean changed = false;
        for(DataEntry entry : entries) {
            if(this.entries.remove(entry)) {
                changed = true;
            }
        }
            
        if(changed) {
            saveEntries(this.entries);
            String path = getDataSource().getPath();
            publishDataChange(Bundle.MSG_AbstractDataProvider_Deleted(path));
        }
    }
}