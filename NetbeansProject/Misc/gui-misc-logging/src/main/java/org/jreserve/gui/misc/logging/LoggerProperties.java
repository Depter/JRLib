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
package org.jreserve.gui.misc.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoggerProperties {
    public final static String SHOW_GUI = "gui.show";
    public final static String MAIN_LEVEL = ".level";
    public final static String SHOW_DIALOG = "nb.exceptionDialog";
    
    private final static Logger logger = Logger.getLogger(LoggerProperties.class.getName());
    private final static String PACKAGE = "org.jreserve.gui.misc.logging";
    private static Properties PROPERTIES = null;
    
    public static Properties getProperties() {
        if(PROPERTIES == null)
            readProperties();
        return PROPERTIES;
    }
    
    private static void readProperties() {
        InputStream is = null;
        try {
            PROPERTIES = new Properties();
            File propFile = getPropertiesFile();
            if(propFile != null) {
                logger.log(Level.INFO, "Loading logging properties from file: {0}", propFile.getAbsolutePath());
                is = new FileInputStream(propFile);
                PROPERTIES.load(is);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to read logging properties!", ex);
        } finally {
            closeStream(is);
        }
    }
    
    private static void closeStream(InputStream is) {
        if(is != null) {
            try{is.close();} catch (IOException ex) {
                logger.log(Level.SEVERE, "Unable to close InputStream for logging properties!", ex);
                Exceptions.printStackTrace(ex);
            }
        }
    }

    
    private static File getPropertiesFile() {
        InstalledFileLocator locator = InstalledFileLocator.getDefault();
        File child = locator.locate("logging/logging.properties", null, false);
        return (child!=null && child.exists())? child : null;
    }
    
    public static void save() {
        save(getProperties());
    }
    private static void save(Properties properties) {
        Writer writer = null;
        try {
            writer = new FileWriter(getPropertiesFile(), false);
            properties.store(writer, "Logging properties");
            writer.flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to save logging properties!", ex);
            Exceptions.printStackTrace(ex);
        } finally {
            if(writer != null) {
                try {writer.close();} catch (IOException ex) {
                    logger.log(Level.SEVERE, "Unable to close writer for logging properties!", ex);
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }    
}
