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
package org.jreserve.gui.plot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotSerie<K extends Comparable<K>, T extends Comparable<T>> implements Iterable<PlotEntry<T>> {
    
    private List<PlotEntry<T>> entries = new ArrayList<PlotEntry<T>>();
    private K key;
    
    public PlotSerie(K key) {
        if(key == null) 
            throw new NullPointerException("Key is null!");
        this.key = key;
    }
    
    public K getKey() {
        return key;
    }
    
    public void addEntry(T key, double value) {
        if(key == null) 
            throw new NullPointerException("Key is null!");
        PlotEntry<T> entry = new PlotEntry<T>(key, value);
        if(entries.contains(entry))
            throw new IllegalArgumentException("Key already added: "+key);
        entries.add(entry);
        Collections.sort(entries);
    }

    @Override
    public Iterator<PlotEntry<T>> iterator() {
        return entries.iterator();
    }
    
    public int getSize() {
        return entries.size();
    }
    
    public PlotEntry<T> getEntry(int index) {
        return entries.get(index);
    }
}
