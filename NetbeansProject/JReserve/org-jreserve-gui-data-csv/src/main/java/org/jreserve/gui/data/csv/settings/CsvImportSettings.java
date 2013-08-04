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

package org.jreserve.gui.data.csv.settings;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvImportSettings {
    
    private final static Logger logger = Logger.getLogger(CsvImportSettings.class.getName());
    
    private final static boolean DEFAULT_HAS_COLUMN_HEADERS = false;
    private final static boolean DEFAULT_HAS_ROW_HEADERS = false;
    private final static boolean DEFAULT_CELL_QUOTES = false;
    private final static String DEFAULT_CELL_SEPARATOR = ";";
    private final static String[] DEFAULT_CELL_SEPARATORS = {
        ";", ",", "Tab"
    };
    
    private final static String KEY_COLUMN_HEADERS = "column.headers";
    private final static String KEY_ROW_HEADERS = "row.headers";
    private final static String KEY_CELL_QUOTES = "cells.quoted";
    private final static String KEY_CELL_SEPARATOR = "cell.separator";
    
    public static boolean hasColumnHeaders() {
        return getBoolean(KEY_COLUMN_HEADERS, DEFAULT_HAS_COLUMN_HEADERS);
    }
    
    private static boolean getBoolean(String key, boolean def) {
        return getPreferences().getBoolean(key, def);
    } 
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(CsvImportSettings.class);
    }
    
    public static void setHasColumnHeaders(boolean hasColumnHeaders) {
        logger.log(Level.INFO, "CSV Setting 'hasColumnHeaders' is set to: {0}", hasColumnHeaders);
        getPreferences().putBoolean(KEY_COLUMN_HEADERS, hasColumnHeaders);
    }
    
    public static boolean hasRowHeaders() {
        return getBoolean(KEY_ROW_HEADERS, DEFAULT_HAS_ROW_HEADERS);
    }
    
    public static void setHasRowHeaders(boolean hasRowHeaders) {
        logger.log(Level.INFO, "CSV Setting 'hasRowHeaders' is set to: {0}", hasRowHeaders);
        getPreferences().putBoolean(KEY_ROW_HEADERS, hasRowHeaders);
    }
    
    public static boolean cellsQuoted() {
        return getBoolean(KEY_CELL_QUOTES, DEFAULT_CELL_QUOTES);
    }
    
    public static void setCellsQuoted(boolean cellsQuoted) {
        logger.log(Level.INFO, "CSV Setting 'cellsQuoted' is set to: {0}", cellsQuoted);
        getPreferences().putBoolean(KEY_CELL_QUOTES, cellsQuoted);
    }
    
    public static String[] getCellSeparators() {
        return Arrays.copyOf(DEFAULT_CELL_SEPARATORS, DEFAULT_CELL_SEPARATORS.length);
    }
    
    public static String getCellSeparator() {
        return getPreferences().get(KEY_CELL_SEPARATOR, DEFAULT_CELL_SEPARATOR);
    }
    
    public static void setCellSeparator(String separator) {
        if(separator==null || separator.length()==0) {
            logger.log(Level.INFO, "CSV Setting 'cellSeparator' is set to: {0}", DEFAULT_CELL_SEPARATOR);
            getPreferences().remove(KEY_CELL_SEPARATOR);
        } else {
            if("Tab".equalsIgnoreCase(separator))
                separator = "\t";
            logger.log(Level.INFO, "CSV Setting 'cellSeparator' is set to: {0}", separator);
            getPreferences().put(KEY_CELL_SEPARATOR, separator);
        }
    }
    
    public static void clear() {
        setHasRowHeaders(DEFAULT_HAS_ROW_HEADERS);
        setHasColumnHeaders(DEFAULT_HAS_COLUMN_HEADERS);
        setCellsQuoted(DEFAULT_CELL_QUOTES);
        setCellSeparator(DEFAULT_CELL_SEPARATOR);
    }
    
    private CsvImportSettings() {}
}
