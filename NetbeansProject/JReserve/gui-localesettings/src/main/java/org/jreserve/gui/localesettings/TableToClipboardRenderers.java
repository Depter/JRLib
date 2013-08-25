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
import java.util.Date;
import javax.swing.table.TableModel;
import org.jreserve.gui.misc.utils.widgets.TableToClipboard;
import org.jreserve.gui.misc.utils.widgets.TableToClipboard.DefaultRenderer;
import org.jreserve.gui.misc.utils.widgets.TableToClipboard.Renderer;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TableToClipboardRenderers {
    
    public static void writeToClipboard(TableModel model) {
        writeToClipboard(model, true);
    }
    
    public static void writeToClipboard(TableModel model, boolean withHeader) {
        createDefault().toClipboard(model, withHeader);
    }
    
    public static TableToClipboard createDefault() {
        TableToClipboard ttcb = new TableToClipboard();
        Renderer r = new MonthDateRenderer(true);
        ttcb.setRenderer(Date.class, r);
        ttcb.setRenderer(MonthDate.class, r);
        
        r = new NumberRenderer();
        ttcb.setRenderer(Byte.class, r);
        ttcb.setRenderer(Short.class, r);
        ttcb.setRenderer(Integer.class, r);
        ttcb.setRenderer(Long.class, r);
        ttcb.setRenderer(Float.class, r);
        ttcb.setRenderer(Double.class, r);
        
        return ttcb;
    }
    
    public static class NumberRenderer extends DefaultRenderer {
        
        private char dSep = LocaleSettings.getDecimalSeparator();
        private String nan = LocaleSettings.getNaN();
        private String infinity = LocaleSettings.getInfinity();
        
        @Override
        public String toString(Object value) {
            if(value instanceof Number)
                return toString((Number)value);
            return super.toString(value);
        }
        
        private String toString(Number number) {
            if(isDecimal(number)) {
                return toString(number.doubleValue());
            } else {
                return "" + number.intValue();
            }
        }
        
        private boolean isDecimal(Number number) {
            return (number instanceof Float) ||
                   (number instanceof Double);
        }
        
        private String toString(double value) {
            if(Double.isNaN(value))
                return nan;
            if(Double.isInfinite(value)) {
                return 0d > value? "-"+infinity : infinity;
            }
            
            String str = ""+value;
            return (dSep == '.')? str : str.replace('.', dSep);
        }
    }
    
    public static class DateRenderer extends DefaultRenderer {
        private DateFormat df = LocaleSettings.createDateFormat();
        
        @Override
        public String toString(Object value) {
            if(value instanceof Date)
                return df.format((Date)value);
            return super.toString(value);
        }
    
    }
    
    public static class MonthDateRenderer extends DateRenderer {
        private MonthDate.Factory mdf;
        private boolean toDate;
        
        public MonthDateRenderer(boolean convertToDate) {
            this.toDate = convertToDate;
            if(convertToDate)
                mdf = new MonthDate.Factory();
        }
        
        @Override
        public String toString(Object value) {
            if(value instanceof MonthDate) {
                MonthDate md = (MonthDate) value;
                if(toDate)
                    return super.toString(mdf.toDate(md));
                return md.toString();
            }
            return super.toString(value);
        }
    }
}
