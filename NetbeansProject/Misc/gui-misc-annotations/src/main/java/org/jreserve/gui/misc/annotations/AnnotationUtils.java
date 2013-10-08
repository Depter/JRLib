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
package org.jreserve.gui.misc.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AnnotationUtils {
    
    public static boolean booleanAttribute(String name, FileObject file) {
        return (Boolean) file.getAttribute(name);
    }
    
    public static boolean booleanAttribute(String name, FileObject file, boolean defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Boolean)
            return (Boolean) value;
        return defaultValue;
    }
    
    public static byte byteAttribute(String name, FileObject file) {
        return (Byte) file.getAttribute(name);
    }
    
    public static byte byteAttribute(String name, FileObject file, byte defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).byteValue();
        return defaultValue;
    }
    
    public static short shortAttribute(String name, FileObject file) {
        return (Short) file.getAttribute(name);
    }
    
    public static short shortAttribute(String name, FileObject file, short defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).shortValue();
        return defaultValue;
    }
    
    public static int intAttribute(String name, FileObject file) {
        return ((Number) file.getAttribute(name)).intValue();
    }
    
    public static int intAttribute(String name, FileObject file, int defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).intValue();
        return defaultValue;
    }
    
    public static long longAttribute(String name, FileObject file) {
        return (Short) file.getAttribute(name);
    }
    
    public static long longAttribute(String name, FileObject file, long defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).longValue();
        return defaultValue;
    }
    
    public static float floatAttribute(String name, FileObject file) {
        return (Float) file.getAttribute(name);
    }
    
    public static float floatAttribute(String name, FileObject file, float defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).floatValue();
        return defaultValue;
    }
    
    public static double doubleAttribute(String name, FileObject file) {
        return (Double) file.getAttribute(name);
    }
    
    public static double doubleAttribute(String name, FileObject file, double defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Number)
            return ((Number) value).doubleValue();
        return defaultValue;
    }
    
    public static char characterAttribute(String name, FileObject file) {
        return (Character) file.getAttribute(name);
    }
    
    public static char characterAttribute(String name, FileObject file, char defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof Character)
            return (Character) value;
        return defaultValue;
    }
    
    public static String stringAttribute(String name, FileObject file) {
        return (String) file.getAttribute(name);
    }
    
    public static String stringAttribute(String name, FileObject file, String defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof String)
            return (String) value;
        return defaultValue;
    }
    
    public static URL urlAttribute(String name, FileObject file) {
        return (URL) file.getAttribute(name);
    }
    
    public static URL urlAttribute(String name, FileObject file, URL defaultValue) {
        Object value = file.getAttribute(name);
        if(value instanceof URL)
            return (URL) value;
        return defaultValue;
    }
    
    public static Object loadInstance(FileObject file) throws Exception {
        DataObject obj = DataObject.find(file);
        InstanceCookie ic = obj.getLookup().lookup(InstanceCookie.class);
        return ic.instanceCreate();
    }
    
    public static Object instantiate(FileObject file, Object... parameters) {
        return instantiate(file, parameters, getTypes(parameters));
    }
    
    private static Class[] getTypes(Object... params) {
        int size = params.length;
        Class[] result = new Class[size];
        for(int i=0; i<size; i++)
            result[i] = (params[i]==null)? Object.class : params[i].getClass();
        return result;
    }
    
    public static Object instantiate(FileObject file, Object[] parameters, Class[] paramTypes) {
        String className = stringAttribute("class", file, null);
        if(className == null)
            className = stringAttribute("instanceCreate", file, null);
        if(className == null)
            className = stringAttribute("instanceClass", file, null);
        
        String method = stringAttribute("method", file, null);
        Exception initial = null;
        
        try {
            ClassLoader cl = getClassLoader();
            Class<?> clazz = Class.forName(className, true, cl);
            if(method == null) {
                try {
                    Constructor<?> constructor = getConstructor(clazz, paramTypes);
                    return constructor.newInstance(parameters);
                } catch (Exception ex) {
                    initial = ex;
                    Constructor<?> constructor = clazz.getConstructor();
                    return constructor.newInstance();
                }
            } else {
                try {
                    Method m = getMethod(clazz, method, paramTypes);
                    return m.invoke(null, parameters);
                } catch (Exception ex) {
                    initial = ex;
                    Method m = clazz.getMethod(method);
                    return m.invoke(null);
                }
            }
        } catch (Exception ex) {
            throw instantiateException(ex, initial, className);
        }
    }
    
    private static Constructor getConstructor(Class clazz, Class[] paramTypes) throws NoSuchMethodException {
        for(Constructor c : clazz.getConstructors()) {
            if(isAssignable(c.getParameterTypes(), paramTypes))
                return c;
        }
        return clazz.getConstructor(paramTypes);
    }
    
    private static boolean isAssignable(Class[] constructor, Class[] parameters) {
        int size = parameters.length;
        if(size != constructor.length)
            return false;
        for(int i=0; i<size; i++)
            if(!constructor[i].isAssignableFrom(parameters[i]))
                return false;
        return true;
    }
    
    private static Method getMethod(Class clazz, String name, Class[] parameters) throws NoSuchMethodException {
        for(Method method : clazz.getMethods()) {
            if(name.equals(method.getName()) && isAssignable(method.getParameterTypes(), parameters))
                return method;
        }
        return clazz.getMethod(name, parameters);
    }

    private static ClassLoader getClassLoader() {
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        if(cl == null)
            cl = Thread.currentThread().getContextClassLoader();
        if(cl == null)
            cl = AnnotationUtils.class.getClassLoader();
        return cl;
    }
    
    private static IllegalStateException instantiateException(Exception ex, Exception initial, String className) {
        IllegalStateException ise = new IllegalStateException("Unable to instantiate class: "+className, ex);
        getCause(ise).initCause(initial);
        return ise;
    }
    
    private static Throwable getCause(Throwable t) {
        while(t.getCause() != null)
            t = t.getCause();
        return t;
    }

}
