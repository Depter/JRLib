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

package org.jreserve.gui.data.spi;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public final class DataEntry implements Comparable<DataEntry>{
    
    private final MonthDate accident;
    private final MonthDate development;
    private final double value;
    
    public DataEntry(MonthDate accident, double value) {
        this(accident, accident, value);
    }
    
    public DataEntry(MonthDate accident, MonthDate development, double value) {
        if(accident == null)
            throw new NullPointerException("Accident is null!");
        this.accident = accident;
        
        if(development == null)
            throw new NullPointerException("Development is null!");
        this.development = development;
        
        this.value = value;
    }
    
    public MonthDate getAccidentDate() {
        return accident;
    }
    
    public MonthDate getDevelopmentDate() {
        return development;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(DataEntry o) {
        int dif = accident.compareTo(o.accident);
        return dif!=0? dif : development.compareTo(o.development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DataEntry) &&
               compareTo((DataEntry)o) == 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accident.hashCode();
        return 17 * hash + development.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DataEntry [%s / %s: %f]", accident, development, value);
    }
}
