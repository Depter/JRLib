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

package org.jreserve.gui.data.settings;

import java.util.prefs.Preferences;
import org.jreserve.gui.data.inport.ImportDataProviderAdapter;
import org.jreserve.gui.data.inport.ImportDataProviderRegistry;
import org.jreserve.gui.data.spi.SaveType;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ImportSettings {
    
    private final static SaveType DEFAULT_SAVE_TYPE = SaveType.SAVE_NEW;
    private final static String DEFAULT_IMPORT_PROVIDER = null;
    
    private final static String KEY_SAVE_TYPE = "save.type";
    private final static String KEY_IMPORT_PROVIDER = "import.provider";
    private static boolean adapterLoaded = false;
    private static ImportDataProviderAdapter adapter;
    
    public synchronized static SaveType getSaveType() {
        String name = getString(KEY_SAVE_TYPE, DEFAULT_SAVE_TYPE.name());
        return SaveType.valueOf(name);
    }
    
    private static String getString(String key, String def) {
        return getPreferences().get(key, def);
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(ImportSettings.class);
    }
    
    public synchronized static void setSaveType(SaveType saveType) {
        setString(KEY_SAVE_TYPE, saveType==null? null : saveType.name());
    }
    
    private static void setString(String key, String value) {
        if(value == null || value.length()==0)
            getPreferences().remove(key);
        else
            getPreferences().put(key, value);
    }
    
    public static synchronized ImportDataProviderAdapter getImportDataProvider() {
        if(!adapterLoaded) {
            adapter = loadAdapter();
            adapterLoaded = true;
        }
        return adapter;
    }
    
    private static ImportDataProviderAdapter loadAdapter() {
        String id = getString(KEY_IMPORT_PROVIDER, null);
        if(id == null)
            return null;
        for(ImportDataProviderAdapter a : ImportDataProviderRegistry.getAdapters())
            if(id.equals(a.getId()))
                return a;
        return null;
    }
    
    public static synchronized void setImportDataProvider(ImportDataProviderAdapter adapter) {
        adapterLoaded = true;
        ImportSettings.adapter = adapter;
        setString(KEY_IMPORT_PROVIDER, adapter==null? DEFAULT_IMPORT_PROVIDER : adapter.getId());
    }
    
    public static void clear() {
        setSaveType(DEFAULT_SAVE_TYPE);
        ImportSettings.adapter = null;
        ImportSettings.adapterLoaded = true;
        setString(KEY_IMPORT_PROVIDER, null);
    }
    
    private ImportSettings() {}
}
