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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 */
public class JDomUtil {
    
    private final static Logger logger = Logger.getLogger(JDomUtil.class.getName());
    
    public static Element getRootElement(final FileObject file) throws IOException {
        synchronized(file) {
            SAXBuilder builder = new SAXBuilder();
            InputStream is = null;
            
            try {
                is = file.getInputStream();
                Document doc = builder.build(is);
                return doc.getRootElement();
            } catch (Exception ex) {
                String msg = String.format("Unable to read xml from '%s'!", file.getPath());
                logger.log(Level.SEVERE, msg, ex);
                throw new IOException(msg, ex);
            } finally {
                if(is != null) {
                    try{is.close();} catch(IOException ex) {
                        String msg = String.format("Unable to close inputstream for file '%s'!", file.getPath());
                        logger.log(Level.SEVERE, msg, ex);
                        throw ex;
                    }
                }
            }
        }
    }
    
    public static void save(final FileObject file, Element element) throws IOException {
        save(file, new Document(element));
    }
    
    public static void save(final FileObject file, Document document) throws IOException {
        synchronized(file) {
            XMLOutputter output = new XMLOutputter();
            OutputStream os = null;
            
            try {
                os = file.getOutputStream();
                output.output(document, os);
                os.flush();
            } catch (IOException ex) {
                String msg = "Unable to save document to file '%s'!";
                msg = String.format(msg, file.getPath());
                logger.log(Level.SEVERE, msg, ex);
                throw new IOException(msg, ex);
            } finally {
                if(os != null) {
                    try{os.close();} catch (IOException ex) {
                        String msg = String.format("Unable to close outputstream for file '%s'!", file.getPath());
                        logger.log(Level.SEVERE, msg, ex);
                        throw ex;
                    }
                }
            }
        }
    }
    
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
        return getNonEmptyString(e);
    }
    
    private static String getNonEmptyString(Element element) throws IOException {
        String str = element.getTextTrim();
        if(str == null || str.length() == 0) {
            String msg = "Value not set at '%s'!";
            throw new IOException(String.format(msg, getPath(element)));
        }
        return str;
    }
    
    public static byte getExistingByte(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getByte(e);
    }
    
    public static byte getByte(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Byte.parseByte(str);
        } catch (Exception ex) {
            String msg = "Illegal Byte literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static short getExistingShort(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getShort(e);
    }
    
    public static short getShort(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Short.parseShort(str);
        } catch (Exception ex) {
            String msg = "Illegal Short literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static int getExistingInt(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getInt(e);
    }
    
    public static int getInt(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            String msg = "Illegal Integer literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static long getExistingLong(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getLong(e);
    }
    
    public static long getLong(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Long.parseLong(str);
        } catch (Exception ex) {
            String msg = "Illegal Long literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static float getExistingFloat(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getFloat(e);
    }
    
    public static float getFloat(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            String msg = "Illegal Float literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static double getExistingDouble(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getDouble(e);
    }
    
    public static double getDouble(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
            String msg = "Illegal Double literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    public static char getExistingCharacter(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getCharacter(e);
    }
    
    public static char getCharacter(Element element) throws IOException {
        String str = getNonEmptyString(element);
        if(str.length() == 1)
            return str.charAt(0);
        
        String msg = "Illegal Long literal '%s' at '%s'!";
        throw new IOException(String.format(msg, str, getPath(element)));
    }
    
    public static boolean getExistingBoolean(Element parent, String name) throws IOException {
        Element e = getExistingChild(parent, name);
        return getBoolean(e);
    }
    
    public static boolean getBoolean(Element element) throws IOException {
        String str = getNonEmptyString(element);
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception ex) {
            String msg = "Illegal Boolean literal '%s' at '%s'!";
            throw new IOException(String.format(msg, str, getPath(element)), ex);
        }
    }
    
    private JDomUtil() {}
}
