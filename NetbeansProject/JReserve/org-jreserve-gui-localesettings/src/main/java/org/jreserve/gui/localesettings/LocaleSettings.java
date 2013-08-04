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

package org.jreserve.gui.localesettings;

import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LocaleSettings {
    
    private final static Locale locale = Locale.getDefault();
    private final static DecimalFormatSymbols DFS = new DecimalFormatSymbols(locale);
    private final static String[] DEFAULT_DATE_FORMATS = {
        "yyyy-MM-dd", 
        "yyyy-MM", 
        "MM-dd-yyyy", 
        "dd-MM-yyyy",
        "MM-yyyy"
    };
    private final static int DEFAULT_DECIMAL_COUNT = 2;
    
    private final static String KEY_DECIMAL_SEPARATOR = "decimal.separator";
    private final static String KEY_THOUSAND_SEPARATOR = "thousand.separator";
    private final static String KEY_DATE_FORMAT = "date.format";
    private final static String KEY_DECIMAL_COUNT = "decimal.count";
    
    public static int getDecimalCount() {
        return getInt(KEY_DECIMAL_COUNT, DEFAULT_DECIMAL_COUNT);
    }
    
    private static int getInt(String key, int def) {
        return getPreferences().getInt(key, def);
    }
    
    private static Preferences getPreferences() {
        return NbPreferences.forModule(LocaleSettings.class);
    }
    
    public static void setDecimalCount(int count) {
        if(count < 0)
            count = 0;
        getPreferences().putInt(KEY_DECIMAL_COUNT, count);
    }
    
    public synchronized static char getDecimalSeparator() {
        return getCharacter(KEY_DECIMAL_SEPARATOR, DFS.getDecimalSeparator());
    }
    
    private static char getCharacter(String key, char def) {
        String str = getString(key, null);
        if(str==null || str.length()==0)
            return def;
        return str.charAt(0);
    }
    
    private static String getString(String key, String def) {
        return getPreferences().get(key, def);
    }
    
    public synchronized static void setDecimalSeparator(Character c) {
        setString(KEY_DECIMAL_SEPARATOR, c==null? null : ""+c);
    }
    
    private static void setString(String key, String value) {
        if(value == null || value.length() == 0)
            getPreferences().remove(key);
        else
            getPreferences().put(key, value);
    }
    
    public synchronized static char getThousandSeparator() {
        return getCharacter(KEY_THOUSAND_SEPARATOR, DFS.getGroupingSeparator());
    }
    
    public synchronized static void setThousandSeparator(Character c) {
        setString(KEY_THOUSAND_SEPARATOR, c==null? null : ""+c);
    }
    
    public synchronized static String[] getDateFormats() {
        return Arrays.copyOfRange(DEFAULT_DATE_FORMATS, 0, DEFAULT_DATE_FORMATS.length);
    }
    
    public synchronized static String getDateFormat() {
        String format = getString(KEY_DATE_FORMAT, null);
        if(format != null && format.length() > 0)
            return format;
        String pattern = new SimpleDateFormat().toPattern();
        if(pattern == null || pattern.length()==0 || pattern.charAt(0) == 'y')
            return DEFAULT_DATE_FORMATS[0];
        else if(pattern.charAt(0) == 'M')
            return DEFAULT_DATE_FORMATS[2];
        else 
            return DEFAULT_DATE_FORMATS[3];
    }
    
    public synchronized static void setDateFormat(SimpleDateFormat df) {
        if(df == null)
            setString(KEY_DATE_FORMAT, null);
        else
            setString(KEY_DATE_FORMAT, df.toPattern());
    }
    
    public synchronized static void clear() {
        setDecimalCount(DEFAULT_DECIMAL_COUNT);
        setDecimalSeparator(null);
        setThousandSeparator(null);
        setDateFormat(null);
    }
    
    private LocaleSettings() {}
}
