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
package org.jreserve.gui.calculations.api;

import java.awt.Color;
import java.util.prefs.Preferences;
import org.jreserve.gui.calculations.util.DefaultColorRegistry;
import org.jreserve.gui.misc.utils.widgets.ColorUtil;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultColor {
    
    public static Color getColor(String id) {
        String hex = getPreferences().get(id, null);
        if(hex != null && hex.length() > 0)
            return ColorUtil.parseColor(hex);
        return DefaultColorRegistry.getColor(id);
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(DefaultColor.class);
    }
    
    public static void setColor(String id, Color color) {
        if(color == null) {
            getPreferences().remove(id);
        } else {
            getPreferences().put(id, ColorUtil.toHex(color));
        }
    }
    
    public static @interface Registration {
        public String id();
    
        public String displayName();
    
        public String color();
    
        public int position() default Integer.MAX_VALUE;
    }
    
    public static @interface Registrations {
        DefaultColor.Registration[] value();
    }
}
