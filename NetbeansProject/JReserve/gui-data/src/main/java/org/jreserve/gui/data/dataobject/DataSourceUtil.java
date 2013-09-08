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

package org.jreserve.gui.data.dataobject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.util.DataProviderFactoryRegistry;
import org.jreserve.jrlib.gui.data.DataType;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataSourceUtil {
    
    private final static Logger logger = Logger.getLogger(DataSourceUtil.class.getName());
    
    static Properties loadProperties(FileObject file) throws IOException {
        FileLock lock = null;
        
        try {
            lock = file.lock();
            InputStream is = file.getInputStream();
            Properties props = new Properties();
            props.loadFromXML(is);
            return props;
        } catch (IOException ex) {
            String msg = "Unable to load properties from file '%s'!";
            msg = String.format(msg, file.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw ex;
        } finally {
            if(lock != null)
                lock.releaseLock();
        }
    }
    
    static long getLong(Properties props, String name, long def) {
        String p = props.getProperty(name);
        if(p==null || p.length()==0)
            return def;
        try {
            return Long.parseLong(p);
        } catch (Exception ex) {
            return def;
        }
    }
    
    static DataProvider createProvider(FileObject primaryFile, Properties props) throws IOException {
        String id = props.getProperty(DataProvider.PROP_FACTORY_ID);
        if(id == null)
            throw new IOException("Factory ID not found!");
        DataProvider.Factory factory = DataProviderFactoryRegistry.getFactory(id);
        return factory.createProvider(primaryFile, toMapp(props));
    }
    
    private static Map<String, String> toMapp(Properties properties) {
        Map<String, String> result = new HashMap<String, String>(properties.size());
        for(String key : properties.stringPropertyNames())
            result.put(key, properties.getProperty(key));
        return result;
    }
    
    static DataType getDataType(FileObject primaryFile, Properties props) throws IOException {
        String name = props.getProperty(DataSourceDataObject.PROP_DATA_TYPE);
        try {
            return DataType.valueOf(name);
        } catch (Exception ex) {
            throw new IOException("DataType not set for file: "+primaryFile.getPath());
        }
    }
    
    public static void saveProperties(FileObject file, Properties props) throws IOException {
        OutputStream os = null;
        FileLock lock = null;
        try {
            lock = file.lock();
            os = file.getOutputStream(lock);
            props.storeToXML(os, null);
        } catch (IOException ex) {
            String msg = "Unabel to save properties to file: "+file.getPath();
            logger.log(Level.SEVERE, msg, ex);
            throw ex;
        } finally {
            if(os != null) {
                try {os.close();} catch(IOException ex) {}
            }

            if(lock != null) {
                lock.releaseLock();
            }
        }
    }
    
    private DataSourceUtil() {}
}
