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
package org.jreserve.gui.plot.colors;

import java.awt.Color;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ColorUtil {
    
    private final static int HEX_LENGTH = 6;
    private final static String HEX_PATTERN = "[0-9A-Fa-f]+";
    
    public static boolean isHexColor(String color) {
        return color != null && 
               color.length() == HEX_LENGTH && 
               color.matches(HEX_PATTERN);
    }
    
    public static Color parseColor(String hex) {
        if(!isHexColor(hex))
            return null;
        
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }
    
    public static Color getContrastColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int yiq = ((r*299)+(g*587)+(b*114))/1000;
	return (yiq >= 128) ? Color.BLACK : Color.WHITE;
    }
    
    public static String toHex(Color color) {
        return (
                toHex(color.getRed()) +
                toHex(color.getGreen()) +
                toHex(color.getBlue())
               ).toUpperCase();
    }
    
    private static String toHex(int value) {
        String str = Integer.toHexString(value);
        if(str.length() < 2)
            str = "0"+str;
        return str;
    }
    
    private ColorUtil() {}
}
