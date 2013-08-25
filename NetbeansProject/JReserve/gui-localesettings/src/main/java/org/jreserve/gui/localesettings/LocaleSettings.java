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

import java.text.DateFormat;
import java.text.DecimalFormat;
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
        "yyyy.MM.dd", 
        "yyyy-MM", 
        "yyyy.MM", 
        "MM-dd-yyyy", 
        "MM.dd.yyyy", 
        "dd-MM-yyyy",
        "dd.MM.yyyy",
        "MM-yyyy",
        "MM.yyyy"
    };
    private final static int DEFAULT_DECIMAL_COUNT = 0;
    private final static String DEFAULT_NAN = "NaN";
    
    private final static String KEY_DECIMAL_SEPARATOR = "decimal.separator";
    private final static String KEY_THOUSAND_SEPARATOR = "thousand.separator";
    private final static String KEY_DECIMAL_COUNT = "decimal.count";
    private final static String KEY_NAN = "nan";
    private final static String KEY_EXPONENT_SEPARATOR = "exponent.separator";
    private final static String KEY_INFINITY = "infinity";
    private final static String KEY_DATE_FORMAT = "date.format";
    
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
    
    public synchronized static String getNaN() {
        return getString(KEY_NAN, DEFAULT_NAN);
    }
    
    public synchronized static void setNaN(String nan) {
        setString(KEY_NAN, nan);
    }
    
    public synchronized static String getExponentSeparator() {
        return getString(KEY_EXPONENT_SEPARATOR, DFS.getExponentSeparator());
    }
    
    public synchronized static void setExponentSeparator(String exponentSeparator) {
        setString(KEY_EXPONENT_SEPARATOR, exponentSeparator);
    }
    
    public synchronized static String getInfinity() {
        return getString(KEY_INFINITY, DFS.getInfinity());
    }
    
    public synchronized static void setInfinity(String infinity) {
        setString(KEY_INFINITY, infinity);
    }
    
    public synchronized static String[] getDateFormatPatterns() {
        return Arrays.copyOfRange(DEFAULT_DATE_FORMATS, 0, DEFAULT_DATE_FORMATS.length);
    }
    
    public synchronized static String getDateFormatPattern() {
        String format = getString(KEY_DATE_FORMAT, null);
        if(format != null && format.length() > 0)
            return format;
        String pattern = new SimpleDateFormat().toPattern();
        if(pattern == null)
            return DEFAULT_DATE_FORMATS[0];
        if(pattern.length()==0 || pattern.charAt(0) == 'y') {
            return (pattern.indexOf('-')>0)? DEFAULT_DATE_FORMATS[0] : DEFAULT_DATE_FORMATS[1];
        } else if(pattern.charAt(0) == 'M') {
            return (pattern.indexOf('-')>0)? DEFAULT_DATE_FORMATS[4] : DEFAULT_DATE_FORMATS[5];
        } else {
            return (pattern.indexOf('-')>0)? DEFAULT_DATE_FORMATS[6] : DEFAULT_DATE_FORMATS[7];
        }
    }
    
    public synchronized static void setDateFormatPattern(SimpleDateFormat df) {
        String pattern = df==null? null : df.toPattern();
        System.setProperty("default.date.format", pattern);
        setString(KEY_DATE_FORMAT, pattern);
    }
    
    public synchronized static void clear() {
        setDecimalCount(DEFAULT_DECIMAL_COUNT);
        setDecimalSeparator(null);
        setThousandSeparator(null);
        setNaN(null);
        setExponentSeparator(null);
        setInfinity(null);
        setDateFormatPattern(null);
    }
    
    public synchronized static DecimalFormat createDecimalFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(createDecimalSymbols());
        return df;
    }
    
    public synchronized static DecimalFormatter createDecimalFormatter() {
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(createDecimalSymbols());
        
        DecimalFormatter formatter = new DecimalFormatter(df);
        formatter.setDecimalCount(getDecimalCount());
        df.setMaximumFractionDigits(formatter.decimals);
        df.setMinimumFractionDigits(formatter.decimals);
        
        return formatter;
    }
    
    private static DecimalFormatSymbols createDecimalSymbols() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(getDecimalSeparator());
        dfs.setExponentSeparator(getExponentSeparator());
        dfs.setGroupingSeparator(getThousandSeparator());
        dfs.setInfinity(getInfinity());
        dfs.setNaN(getNaN());
        return dfs;
    }
    
    public static DateFormat createDateFormat() {
        String pattern = getDateFormatPattern();
        try {
            return new SimpleDateFormat(pattern);
        } catch (Exception ex) {
            return new SimpleDateFormat();
        }
    }
    
    private LocaleSettings() {}
    
    public final static class DecimalFormatter {
        
        private int decimals;
        private double exp;
        private final DecimalFormat format;
        
        private DecimalFormatter(DecimalFormat format) {
            this.format = format;
        }
        
        public String format(double value) {
            if(decimals < 0 && !(Double.isNaN(value) || Double.isInfinite(value)))
                value /= exp;
            return format.format(value);
        }
        
        public void setDecimalCount(int decimals) {
            if(this.decimals != decimals) {
                this.decimals = decimals;
                this.exp = Math.pow(10d, -decimals);
                
                int fd = decimals<0? 0 : decimals;
                format.setMaximumFractionDigits(fd);
                format.setMinimumFractionDigits(fd);
            }
        }
        
        public int getDecimalCount() {
            return decimals;
        }
        
        public char getDecimalSepartor() {
            return format.getDecimalFormatSymbols().getDecimalSeparator();
        }
        
        public void setDecimalSepartor(char decimalSepartor) {
            format.getDecimalFormatSymbols().setDecimalSeparator(decimalSepartor);
        }
        
        public char getThousandSeparator() {
            return format.getDecimalFormatSymbols().getGroupingSeparator();
        }
        
        public void setThousandSepartor(char thousandSepartor) {
            format.getDecimalFormatSymbols().setGroupingSeparator(thousandSepartor);
        }
        
        public String getNaN() {
            return format.getDecimalFormatSymbols().getNaN();
        }
        
        public void setNaN(String nan) {
            format.getDecimalFormatSymbols().setNaN(nan);
        }
        
        public String getExponentSeparator() {
            return format.getDecimalFormatSymbols().getExponentSeparator();
        }
        
        public void setExponentSeparator(String exponentSeparator) {
            format.getDecimalFormatSymbols().setExponentSeparator(exponentSeparator);
        }
        
        public String getInfinity() {
            return format.getDecimalFormatSymbols().getInfinity();
        }
        
        public void setInfinity(String infinity) {
            format.getDecimalFormatSymbols().setInfinity(infinity);
        }
        
        
    }
}
