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
