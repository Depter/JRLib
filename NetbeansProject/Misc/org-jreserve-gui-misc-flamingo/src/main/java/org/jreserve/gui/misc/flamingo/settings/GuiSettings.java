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
package org.jreserve.gui.misc.flamingo.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SkinInfo;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GuiSettings {
    
    private final static String PROP_SKIN = "guiSettings.skin";
    
    private final static String DEFAULT_SKIN = "org.pushingpixels.substance.api.skin.OfficeBlack2007Skin";
    
    public synchronized static String getSkinClass() {
        return getString(PROP_SKIN, DEFAULT_SKIN);
    }
    
    private static String getString(String key, String def) {
        return getPreferences().get(key, def);
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(GuiSettings.class);
    }
    
    public synchronized static void setSkinClass(String className) {
        if(className == null || className.length() == 0)
            setSkinClass(DEFAULT_SKIN);
        if(!skinExists(className))
            throw new IllegalArgumentException(String.format("Skin '%s' not found!", className));
        setString(PROP_SKIN, className);
    }
    
    private static boolean skinExists(String name) {
        for(SkinInfo info : SubstanceLookAndFeel.getAllSkins().values())
            if(info.getClassName().equals(name))
                return true;
        return false;
    }
    
    private static void setString(String key, String value) {
        if(value == null || value.length()==0)
            getPreferences().remove(key);
        else
            getPreferences().put(key, value);
    }
    
    public synchronized static void clear() {
        setSkinClass(null);
    }
    
    private GuiSettings() {}
}
