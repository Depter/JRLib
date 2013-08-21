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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.DataSourceUtil.ProviderCreate.Error=Unable to load data provider for ''{0}''!"
})
class DataSourceUtil {
    
    private final static Logger logger = Logger.getLogger(DataSourceUtil.class.getName());
    
    static void save(DataSourceImpl ds) throws IOException {
        FileObject file = ds.file;
        logger.log(Level.FINE, "Saving DataProvider: {0}", file.getPath());
        Properties props = createProperties(ds);
        save(file, props);
    }
    
    private static Properties createProperties(DataSourceImpl ds) throws IOException {
        Properties props = new Properties();
        DataProvider provider = ds.getDataProvider();
        
        props.put(DataProvider.PROP_DATA_TYPE, ds.getDataType().name());
        props.put(DataProvider.PROP_FACTORY_ID, provider.getFactory().getId());
        
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
               DataProvider.PROP_FACTORY_ID.equalsIgnoreCase(name);
    }
    
    static DataSourceImpl load(DataCategoryImpl parent, FileObject file) {
        logger.log(Level.FINE, "Loading DataSource: {0}", file.getPath());
        DataSourceImpl ds = new DataSourceImpl(file, parent);
        
        try {
            Properties props = loadProperties(file);
            ds.setDataType(getDataType(props));
            ds.setProvider(createProvider(props));
        } catch (Exception ex) {
            String msg = "Unable to load DataProvider from file '%s'!";
            logger.log(Level.SEVERE, String.format(msg, file.getPath()), ex);
            BubbleUtil.showException("", ex);
        }
        return ds;
    }
    
    private static Properties loadProperties(FileObject file) throws IOException {
        FileLock lock = null;
        
        try {
            lock = file.lock();
            InputStream is = file.getInputStream();
            Properties props = new Properties();
            props.loadFromXML(is);
            return props;
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    private static DataType getDataType(Properties props) throws IOException {
        String name = getProperty(props, DataProvider.PROP_DATA_TYPE);
        try {
            return DataType.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new  IOException("Unknown DataType name: "+name);
        }
    }
    
    private static DataProvider createProvider(Properties props) throws IOException {
        String id = props.getProperty(DataProvider.PROP_FACTORY_ID);
        if(id == null)
            throw new IOException("Factory ID not found!");
        DataProvider.Factory factory = DataProviderFactoryRegistry.getFactory(id);
        return factory.createProvider(toMapp(props));
    }
    
    private static Map<String, String> toMapp(Properties properties) {
        Map<String, String> result = new HashMap<String, String>(properties.size());
        for(String key : properties.stringPropertyNames())
            result.put(key, properties.getProperty(key));
        return result;
    }
    
    private static String getProperty(Properties props, String key) throws IOException {
        String prop = props.getProperty(key);
        if(prop == null)
            throw new IOException(String.format("Key '%s' not found in DataSource properties!", prop));
        return prop;
    }
}
