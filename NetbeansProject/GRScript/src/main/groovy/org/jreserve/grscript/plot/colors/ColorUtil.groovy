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

package org.jreserve.grscript.plot.colors

import java.awt.Color
import org.jreserve.grscript.util.MapUtil
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ColorUtil {

    private static MapUtil mapUtil = MapUtil.getInstance();
    
    static Color getColor(int r, int g, int b) {
        return new Color(escape(r), escape(g), escape(b))
    }
    
    private static escape(int c) {
        if(c < 0) return 0
        if(c > 255) return 255
        return c
    }
    
    static Color getColor(Map map) {
        int r = mapUtil.getInt(map, "r", "red")
        int g = mapUtil.getInt(map, "g", "green")
        int b = mapUtil.getInt(map, "b", "blue")
        getColor(r, g, b)
    }
    
    static Color getColor(List rgb) {
        int r = rgb.get(0)
        int g = rgb.get(1)
        int b = rgb.get(2)
        getColor(r, g, b)
    }
    
    static Color(int[] components) {
        int r = components[0]
        int g = components[1]
        int b = components[2]
        getColor(r, g, b)
    }
    
    static Color getColor(String str) {
        return isHexadecimal(str)?
                getHexColor(str) :
                getColorByName(str)
    }
    
    private static boolean isHexadecimal(String str) {
        return str &&
               str.length() == 7 &&
               str.charAt(0) == '#'
    }
    
    private static Color getHexColor(String str) {
        int r = getHexComponent(str, 1, 3)
        int g = getHexComponent(str, 3, 5)
        int b = getHexComponent(str, 5, 7)
        getColor(r, g, b)
    }
    
    private static int getHexComponent(String str, int begin, int end) {
        Integer.valueOf(str.substring(begin, end), 16)
    }
     
    static Color getColorByName(String name) {
        switch(name?.toLowerCase()) {
            case "black":
                return Color.BLACK
            case "blue":
                return Color.BLUE
            case "cyan":
                return Color.CYAN
            case "dark gray":
            case "dark-gray":
            case "dark grey":
            case "dark-grey":
                return Color.DARK_GRAY
            case "gray":
            case "grey":
                return Color.GRAY
            case "green":
                return Color.GREEN
            case "light gray":
            case "light-gray":
            case "light grey":
            case "light-grey":
                return Color.LIGHT_GRAY
            case "magenta":
                return Color.MAGENTA
            case "orange":
                return Color.ORANGE
            case "pink":
                return Color.PINK
            case "red":
                return Color.RED
            case "white":
                return Color.WHITE
            case "yellow":
                return Color.YELLOW
            default:
                return Color.RED
        }
    }
    
    private ColorUtil() {}
}

