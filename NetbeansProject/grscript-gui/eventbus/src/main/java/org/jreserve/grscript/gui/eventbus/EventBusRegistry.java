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
package org.jreserve.grscript.gui.eventbus;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusRegistry {
    
    private static EventBus<Object> DEFAULT = null;
    private static Map<Object, EventBus> busMap = new HashMap<Object, EventBus>();
    
    public static synchronized EventBus<Object> getDefault() {
        if(DEFAULT == null)
            DEFAULT = new EventBusImpl<Object>();
        return DEFAULT;
    }
    
    public static synchronized EventBus getEventBus(Object key) {
        EventBus bus = busMap.get(key);
        if(bus == null) {
            bus = new EventBusImpl();
            busMap.put(key, bus);
        }
        return bus;
    }
    
    public static synchronized void remove(Object key) {
        busMap.remove(key);
    }
    
    public static synchronized void removeAll() {
        busMap.clear();
    }
    
    public static synchronized <T> EventBus<T> create(Class<T> clazz) {
        return new EventBusImpl<T>();
    }
    
    private EventBusRegistry() {}
}
