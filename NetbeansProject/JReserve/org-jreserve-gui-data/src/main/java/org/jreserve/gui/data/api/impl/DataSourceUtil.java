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
package org.jreserve.gui.data.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.DataProviderFactoryType;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourceUtil {
    
    private final static Logger logger = Logger.getLogger(DataSourceUtil.class.getName());
    private final static String DT_CLASS_NAME = DataType.class.getName();
    
    static void save(DataSourceImpl ds) throws IOException {
        FileObject file = ds.file;
        logger.log(Level.FINE, "Daving DataProvider: {0}", file.getPath());
        Properties props = createProperties(ds.getDataProvider());
        save(file, props);
    }
    
    private static Properties createProperties(DataProvider provider) throws IOException {
        Properties props = new Properties();
        props.put(DataProvider.PROP_DATA_TYPE, provider.getDataType().name());
        props.put(DataProvider.PROP_FACTORY_TYPE, provider.getFactoryType().name());
        props.put(DataProvider.PROP_INSTANCE_PATH, provider.getInstancePath());
        
        for(Map.Entry<String, String> entry : provider.getProperties().entrySet()) {
            if(isFixedProperty(entry.getKey()))
                throw new IOException(String.format("DataProvider '%s' trys to set the fixed property '%s'!", provider, entry.getKey()));
            props.put(entry.getKey(), entry.getValue());
        }
        return props;
    }
    
    private static void save(FileObject file, Properties props) {
        OutputStream os = null;
        FileLock lock = null;
        
        try {
            lock = file.lock();
            os = file.getOutputStream(lock);
            props.storeToXML(os, null);
        } catch (Exception ex) {
        } finally {
            close(file, os);
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    private static void close(FileObject file, OutputStream os) {
        if(os != null) {
            try {os.close();} catch (IOException ex) {
                String msg = "Unable to close OutputStream for file '%s'!";
                msg = String.format(msg, file.getPath());
                logger.log(Level.SEVERE, msg, ex);
            }
        }
    }
    
    private static boolean isFixedProperty(String name) {
        return DataProvider.PROP_DATA_TYPE.equalsIgnoreCase(name) ||
               DataProvider.PROP_FACTORY_TYPE.equalsIgnoreCase(name) ||
               DataProvider.PROP_INSTANCE_PATH.equalsIgnoreCase(name);
    }
    
    static DataProvider load(DataSource ds) {
        FileObject file = ds.getFile();
        logger.log(Level.FINE, "Loading DataProvider: {0}", file.getPath());
        try {
            Properties props = loadProperties(file);
            DataProvider provider = loadProvider(props);
            provider.setProperties(ds, toMapp(props));
            return provider;
        } catch (Exception ex) {
            String msg = "Unable to load DataProvider from file '%s'!";
            logger.log(Level.SEVERE, String.format(msg, file.getPath()), ex);
            //TODO show error bubble
            return EmptyDataProvider.getInstance();
        }
    }
    
    private static Map<String, String> toMapp(Properties properties) {
        Map<String, String> result = new HashMap<String, String>(properties.size());
        for(String key : properties.stringPropertyNames())
            result.put(key, properties.getProperty(key));
        return result;
    }
    
    private static Properties loadProperties(FileObject file) throws IOException {
        FileLock lock = null;
        
        try {
            lock = file.lock();
            InputStream is = file.getInputStream();
            Properties props = new Properties();
            props.loadFromXML(is);
            props.load(is);
            return props;
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    private static DataProvider loadProvider(Properties properties) throws IOException {
        DataType dataType = getDataType(properties);
        DataProviderFactoryType factoryType = getFactoryType(properties);
        String path = getInstancePath(properties);
        return instantiateDataProvider(dataType, factoryType, path);
    }
    
    private static DataType getDataType(Properties props) throws IOException {
        String name = getProperty(props, DataProvider.PROP_DATA_TYPE);
        try {
            return DataType.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new  IOException("Unknown DataType name: "+name);
        }
    }
    
    private static String getProperty(Properties props, String key) throws IOException {
        String prop = props.getProperty(key);
        if(prop == null)
            throw new IOException(String.format("Key '%s' not found in DataSource properties!", prop));
        return prop;
    }
    
    private static DataProviderFactoryType getFactoryType(Properties props) throws IOException {
        String name = getProperty(props, DataProvider.PROP_FACTORY_TYPE);
        try {
            return DataProviderFactoryType.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new  IOException("Unknown DataProviderFactoryType name: "+name);
        }
    }
    
    private static String getInstancePath(Properties props) throws IOException {
        return getProperty(props, DataProvider.PROP_INSTANCE_PATH);
    }
    
    private static DataProvider instantiateDataProvider(DataType dataType, DataProviderFactoryType factoryType, String instancePath) {
        switch(factoryType) {
            case INSTANCE: return createProviderInstance(instancePath, dataType);
            case METHOD: return createProviderMethod(instancePath, dataType);
            default:
                throw new IllegalStateException("Unknonw DataProviderFactoryType: "+factoryType);
        }
    }
    
    private static DataProvider createProviderInstance(String path, DataType dataType) {
        Class instanceClass = loadClass(path);
        if(!DataProvider.class.isAssignableFrom(instanceClass))
            throw new IllegalArgumentException(String.format("Class '%s' does not implement the '%s' interface!", path, DataProvider.class.getName()));
        
        Constructor c = getConstructor(instanceClass);
        try {
            if(c.getParameterTypes().length == 0) {
                return (DataProvider) c.newInstance();
            } else {
                return (DataProvider) c.newInstance(dataType);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Unable to create DataProvider instance by using constructor: %s", c), ex);
        }
    }
    
    private static Class loadClass(String className) {
        try {
            ClassLoader loader = Lookup.getDefault().lookup(ClassLoader.class);
            if(loader == null)
                loader = DataSourceUtil.class.getClassLoader();
            return loader.loadClass(className);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Unable to load class: %s", className), ex);
        }
    }
    
    private static Constructor getConstructor(Class instanceClass) {
        Constructor fallBack = null;
        for(Constructor c : instanceClass.getConstructors()) {
            Class[] params = c.getParameterTypes();
            if(params.length == 0)
                fallBack = c;
            else if(params.length==1 && params[0].getName().equals(DT_CLASS_NAME))
                return c;
        }
        if(fallBack != null)
            return fallBack;
        throw new IllegalArgumentException(String.format("Class '%s' does not have a public empty constructor or a public constructor with parameter type '%s'!", instanceClass, DataType.class.getName()));
    }
    
    private static DataProvider createProviderMethod(String path, DataType dataType) {
        int index = path.lastIndexOf('.');
        if(index < 0)
            throw new IllegalArgumentException(String.format("Illegal factory method path: %s", path));
        Class factoryClass = loadClass(path.substring(0, index));
        
        String name = path.substring(index+1);
        Method m = getFactoryMethod(factoryClass, name);
        try {
            if(m.getParameterTypes().length == 0) {
                return (DataProvider) m.invoke(null);
            } else {
                return (DataProvider) m.invoke(dataType);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Unable to create DataProvider instance by using constructor: %s", m), ex);
        }
    }
    
    private static Method getFactoryMethod(Class factoryClass, String name) {
        Method fallBack = null;
        for(Method m : factoryClass.getMethods()) {
            if(Modifier.isStatic(m.getModifiers()) && name.equals(m.getName())) {
                Class[] params = m.getParameterTypes();
                if(params .length == 0)
                    fallBack = m;
                else if(params.length==1 && params[0].getName().equals(DT_CLASS_NAME))
                    return m;
            }
        }
        if(fallBack != null)
            return fallBack;
        throw new IllegalArgumentException(String.format("Class '%s' does not have a public static factory method with no parameter or parameter type '%s'!", factoryClass, DataType.class.getName()));
    }
}
