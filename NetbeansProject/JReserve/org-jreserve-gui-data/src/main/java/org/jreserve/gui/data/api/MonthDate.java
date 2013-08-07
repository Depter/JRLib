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
package org.jreserve.gui.data.api;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public final class MonthDate implements Comparable<MonthDate> {
    
    public static class Factory {
        
        private final Calendar c;
        
        public Factory() {
            c = GregorianCalendar.getInstance();
        }
        
        public Factory(Locale locale) {
            c = GregorianCalendar.getInstance(locale);
        }
        
        public Factory(TimeZone timeZone) {
            c = GregorianCalendar.getInstance(timeZone);
        }
        
        public Factory(TimeZone timeZone, Locale locale) {
            c = GregorianCalendar.getInstance(timeZone, locale);
        }
        
        public MonthDate toMonthDate(Date date) {
            c.setTime(date);
            return new MonthDate(c);
        }
        
        public Date toDate(MonthDate date) {
            return date.toDate(c);
        }
    }
    
    private final int year;
    private final int month;
    
    public MonthDate(String str) {
        int index = str.indexOf('-');
        if(index < 0)
            throw new IllegalArgumentException("Illegal date format: "+str);
        
        year = Integer.parseInt(str.substring(0, index));
        month = Integer.parseInt(str.substring(index+1));
    }
    
    public MonthDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
    }
    
    public MonthDate(Calendar c) {
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
    }
    
    public MonthDate(int year, int month) {
        if(month > 11) {
            int my = month/12;
            year += my;
            month -= my*12;
        }
        
        this.year = year;
        this.month = month;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public MonthDate addYear(int year) {
        return new MonthDate(this.year+ year, month);
    }
    
    public MonthDate addMonth(int month) {
        int ym = this.year*12 + this.month + month;
        int y = ym/12;
        int m = ym - y*12;
        return new MonthDate(y, m);
    }
    
    public MonthDate add(int year, int month) {
        return addMonth(year * 12 + month);
    }

    public Date toDate() {
        return toDate(GregorianCalendar.getInstance());
    }
    
    private Date toDate(Calendar c) {
        c.clear();
        c.set(year, month, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    @Override
    public int compareTo(MonthDate o) {
        int dif = year - o.year;
        return dif!=0? dif : month - o.month;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MonthDate) &&
               compareTo((MonthDate)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return 31 *(17 + year) + month;
    }
    
    @Override
    public String toString() {
        if(month < 9)
            return ""+year+"-0"+(month+1);
        return ""+year+"-"+(month+1);
    }
}
