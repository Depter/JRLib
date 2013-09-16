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
package org.jreserve.gui.wrapper.jdom;

import java.io.IOException;
import org.jdom2.Element;

/**
 *
 * @author Peter Decsi
 */
public class JDomUtil {
    
    public static void addElement(Element root, String name, String value) {
        Element e = new Element(name);
        e.setText(value==null? "" : value);
        root.addContent(e);
    }
    
    public static void addElement(Element root, String name, Object value) {
        addElement(root, name, value==null? "" : value.toString());
    }
    
    public static void addElement(Element root, String name, byte value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, short value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, int value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, long value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, float value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, double value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, char value) {
        addElement(root, name, ""+value);
    }
    
    public static void addElement(Element root, String name, boolean value) {
        addElement(root, name, Boolean.toString(value));
    }
    
    public static String getPath(Element e) {
        String path = "";
        while(e != null) {
            String name = e.getName();
            Element parent = e.getParentElement();
            if(parent != null)
                name += "[" + (parent.indexOf(e)+1) + "]";
            
            if(path.length() > 0)
                path += "/";
            path += name;
            
            e = parent;
        }
        return null;
    }
    
    public static Element getExistingChild(Element parent, String name) throws IOException {
        Element child = parent.getChild(name);
        if(child == null) {
            String msg = "Element '%s' not found within '%s'!";
            throw new IOException(String.format(msg, name, getPath(parent)));
        }
        return child;
    }
    
    public static String getExistingString(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        String str = e.getTextTrim();
        if(str == null || str.length() == 0) {
            String msg = "Value not set at '%s'!";
            throw new IOException(String.format(msg, getPath(e)));
        }
        return str;
    }
    
    public static byte getExistingByte(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Byte.parseByte(str);
        } catch (Exception ex) {
            String msg = "Illegal Byte literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static short getExistingShort(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Short.parseShort(str);
        } catch (Exception ex) {
            String msg = "Illegal Short literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static int getExistingInt(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            String msg = "Illegal Integer literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static long getExistingLong(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            String msg = "Illegal Long literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static float getExistingFloat(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            String msg = "Illegal Float literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static double getExistingDouble(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            if("NaN".equalsIgnoreCase(str))
                return Double.NaN;
            return Double.parseDouble(str);
        } catch (Exception ex) {
            String msg = "Illegal Double literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    public static char getExistingCharacter(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        if(str.length() == 1)
            return str.charAt(0);
        
        String msg = "Illegal Long literal '%s' at '%s'!";
        Element e = getExistingChild(parent, name);
        throw new IOException(String.format(msg, str, getPath(e)));
    }
    
    public static boolean getExistingBoolean(Element parent, String name) throws IOException {
        String str = getExistingString(parent, name);
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception ex) {
            String msg = "Illegal Boolean literal '%s' at '%s'!";
            Element e = getExistingChild(parent, name);
            throw new IOException(String.format(msg, str, getPath(e)), ex);
        }
    }
    
    private JDomUtil() {}
}
