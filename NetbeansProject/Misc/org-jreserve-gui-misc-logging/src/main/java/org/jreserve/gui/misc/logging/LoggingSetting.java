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

import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.gui.misc.logging.view.GuiHandler;
import org.jreserve.gui.misc.logging.view.LogviewTopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoggingSetting {
    
    private final static Logger logger = Logger.getLogger(LoggingSetting.class.getName());
    private static transient GuiHandler guiHandler;
    
    public static void initialize() {
        Properties props = LoggerProperties.getProperties();
        configureGui(props);
        configureHandlers();
        showNbDialog(props);
        applyLevels(props);
    }
    
    private static void configureGui(Properties props) {
        if(showGui(props))
            appendGuiAppender();
        else
            deleteGuiAppender();
    }
    
    static void appendGuiAppender() {
        if(guiHandler != null)
            return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiHandler = GuiHandler.getInstance();
                Logger.getLogger("").addHandler(guiHandler);
                LogviewTopComponent.openView();
            }
        });
    }
    
    static void deleteGuiAppender() {
        if(guiHandler != null) {
            Logger.getLogger("").removeHandler(guiHandler);
            guiHandler = null;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LogviewTopComponent.closeView();
            }
        });
    }
    
    private static boolean showGui(Properties props) {
        String strShow = props.getProperty(LoggerProperties.SHOW_GUI, "false");
        return "true".equalsIgnoreCase(strShow);
    }
    
    private static void showNbDialog(Properties props) {
        String show = props.getProperty(LoggerProperties.SHOW_DIALOG, "false");
        int value = show.equals("true")? Level.WARNING.intValue() : 99999;
        System.setProperty("netbeans.exception.report.min.level", ""+value);
    }
    
    private static void configureHandlers() {
        Logger root = Logger.getLogger("");
        for(Handler handler : root.getHandlers())
            handler.setLevel(Level.ALL);
    }
    
    private static void applyLevels(Properties properties) {
        for(String prop : properties.stringPropertyNames()) {
            if(isLevelProperty(prop))
                setLevel(prop, properties.getProperty(prop));
        }
    }
    
    private static boolean isLevelProperty(String property) {
        return property.toLowerCase().endsWith(".level");
    }
    
    private static void setLevel(String property, String value) {
        Logger l = getLogger(property);
        Level level = Level.parse(value);
        logger.config(String.format("Logger level: \"%s\" => %s", l.getName(), level.getName()));
        l.setLevel(level);
    }
    
    private static Logger getLogger(String property) {
        int index = property.lastIndexOf('.');
        String loggerName = property.substring(0, index);
        return Logger.getLogger(loggerName);
    }
    
}
