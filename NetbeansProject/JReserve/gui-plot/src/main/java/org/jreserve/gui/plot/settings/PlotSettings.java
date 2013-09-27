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
package org.jreserve.gui.plot.settings;

import java.awt.Color;
import java.util.prefs.Preferences;
import org.jreserve.gui.plot.ColorGenerator;
import org.jreserve.gui.plot.colors.AbstractColorGeneratorAdapter;
import org.jreserve.gui.plot.colors.ColorGeneratorRegistry;
import org.jreserve.gui.plot.colors.ColorUtil;
import org.jreserve.gui.plot.colors.DefaultColorGenerator;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotSettings {
    private final static String KEY_COLOR_GENERATOR = "color.generator";
    private final static String KEY_COLOR_BACKGROUND = "color.background";
    private final static String KEY_COLOR_FOREGROUND = "color.foreground";
    private final static String KEY_COLOR_GRID = "color.grid";
    
    private final static Color FORE_COLOR = Color.BLACK;
    private final static Color BACKGROUND = Color.WHITE;
    private final static Color GRID_COLOR = Color.LIGHT_GRAY;
    
    public synchronized static Color getForeground() {
        return getColor(KEY_COLOR_FOREGROUND, FORE_COLOR);
    }
    
    private static Color getColor(String key, Color defaultColor) {
        String color = getString(key, null);
        if(color == null || color.length()==0)
            return defaultColor;
        Color result = ColorUtil.parseColor(color);
        return result==null? defaultColor : result;
    }
    
    public synchronized static void setForeground(Color color) {
        setColor(KEY_COLOR_FOREGROUND, color);
    }
    
    private static void setColor(String key, Color color) {
        setString(key, color==null? null : ColorUtil.toHex(color));
    }
    
    public synchronized static Color getBackground() {
        return getColor(KEY_COLOR_BACKGROUND, BACKGROUND);
    }
    
    public synchronized static void setBackground(Color color) {
        setColor(KEY_COLOR_BACKGROUND, color);
    }
    
    public synchronized static Color getGridColor() {
        return getColor(KEY_COLOR_GRID, GRID_COLOR);
    }
    
    public synchronized static void setGridColor(Color color) {
        setColor(KEY_COLOR_GRID, color);
    }

    public synchronized static ColorGenerator getColorGenerator() {
        String id = getColorGeneratorId();
        if(id == null)
            return new DefaultColorGenerator();
        AbstractColorGeneratorAdapter a = ColorGeneratorRegistry.getAdapter(id);
        if(a == null)
            return new DefaultColorGenerator();
        return a.createColorGenerator();
    }
    
    private static String getString(String key, String def) {
        return getPreferences().get(key, def);
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(PlotSettings.class);
    }

    public synchronized static String getColorGeneratorId() {
        return getString(KEY_COLOR_GENERATOR, null);
    }
    
    public synchronized static void setColorGeneratorId(String id) {
        setString(KEY_COLOR_GENERATOR, id);
    }
    
    private static void setString(String key, String value) {
        if(value == null || value.length()==0)
            getPreferences().remove(key);
        else
            getPreferences().put(key, value);
    }
}
