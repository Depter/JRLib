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
package org.jreserve.gui.calculations.api.modification;

import java.awt.Color;
import java.util.prefs.Preferences;
import org.jreserve.gui.calculations.util.DefaultColorAdapter;
import org.jreserve.gui.calculations.util.DefaultColorRegistry;
import org.jreserve.gui.misc.utils.widgets.ColorUtil;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultColor {
    
    private final static String BACKGROUND = ".background";
    private final static String FOREGROUDN = ".foreground";
    
    public static Color getBackground(String id) {
        return getColor(id, false);
    }
    
    public static Color getForeground(String id) {
        return getColor(id, true);
    }
    
    private static Color getColor(String id, boolean fg) {
        String hex = getPreferences().get(id+(fg? FOREGROUDN : BACKGROUND), null);
        if(hex != null && hex.length() > 0)
            return ColorUtil.parseColor(hex);
        
        DefaultColorAdapter dfa = DefaultColorRegistry.getColor(id);
        if(dfa == null)
            return fg? Color.BLACK : Color.WHITE;
        return fg? dfa.getForeground() : dfa.getBackground();
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(DefaultColor.class);
    }
    
    public static void setBackground(String id, Color color) {
        setColor(id+BACKGROUND, color);
    }
    
    public static void setForeground(String id, Color color) {
        setColor(id+FOREGROUDN, color);
    }
    
    private static void setColor(String id, Color color) {
        if(color == null) {
            getPreferences().remove(id);
        } else {
            getPreferences().put(id, ColorUtil.toHex(color));
        }
    }
    
    public static @interface Registration {
        public String id();
    
        public String displayName();
    
        public String background() default "FFFFFF";
    
        public String foreground() default "000000";
    
        public int position() default Integer.MAX_VALUE;
    }
    
    public static @interface Registrations {
        DefaultColor.Registration[] value();
    }
}
