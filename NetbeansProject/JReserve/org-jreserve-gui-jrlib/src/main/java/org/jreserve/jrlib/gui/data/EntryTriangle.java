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
package org.jreserve.jrlib.gui.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jreserve.jrlib.triangle.AbstractTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntryTriangle extends AbstractTriangle {
    
    private final Map<MonthDate, Map<MonthDate, DataEntry>> valueMap;
    private final List<MonthDate> accidents;
    private int devCount;
    
    public EntryTriangle(List<DataEntry> entries) {
        valueMap = new TreeMap<MonthDate, Map<MonthDate, DataEntry>>();
        accidents = new ArrayList<MonthDate>();
        initValueMap(entries);
        initDevelopmentCount();
    }
    
    private void initValueMap(List<DataEntry> entries) {
        for(DataEntry entry : entries) {
            MonthDate accident = entry.getAccidentDate();
            MonthDate development = entry.getDevelopmentDate();
            getRow(accident, true).put(development, entry);
            
            if(!accidents.contains(accident))
                accidents.add(accident);
        }
        Collections.sort(accidents);
    }
    
    private Map<MonthDate, DataEntry> getRow(MonthDate accident, boolean create) {
        Map<MonthDate, DataEntry> row = valueMap.get(accident);
        if(row == null && create) {
            row = new TreeMap<MonthDate, DataEntry>();
            valueMap.put(accident, row);
        }
        return row;
    }
    
    private void initDevelopmentCount() {
        devCount = 0;
        for(Map<MonthDate, DataEntry> row : valueMap.values()) {
            int size = row.size();
            if(size > devCount)
                devCount = size;
        }
    }
    
    @Override
    protected boolean withinBounds(int accident) {
        return 0 <= accident && accident < accidents.size();
    }

    @Override
    protected void recalculateLayer() {
    }

    @Override
    public int getAccidentCount() {
        return accidents.size();
    }

    @Override
    public int getDevelopmentCount() {
        return devCount;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(!withinBounds(accident))
            return 0;
        MonthDate ad = accidents.get(accident);
        return getRow(ad, false).size();
    }

    public Set<MonthDate> getAccidentDates() {
        return new TreeSet<MonthDate>(accidents);
    }
    
    public DataEntry getEntry(int accident, int development) {
        if(!withinBounds(accident))
            return null;
        Map<MonthDate, DataEntry> row = getRow(accidents.get(accident), false);
        int index = 0;
        for(DataEntry entry : row.values())
            if(index++ == development)
                return entry;
        return null;
    }
    
    @Override
    public double getValue(int accident, int development) {
        DataEntry entry = getEntry(accident, development);
        return entry==null? Double.NaN : entry.getValue();
    }
}
