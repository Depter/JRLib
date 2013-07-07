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

package org.jreserve.gui.misc.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusManager {
    
    private final static EventBusManager INSTANCE = new EventBusManager();
    
    public static EventBusManager getDefault() {
        return INSTANCE;
    }
    
    private final EventBus root = new EventBus(null, "");
    
    private EventBusManager() {
    }
    
    public synchronized void subscribe(Object listener) {
        subscribe("", listener);
    }
    
    public synchronized void subscribe(String busName, Object listener) {
        if(listener == null)
            throw new NullPointerException("Listener can not be null!");
        if(busName == null)
            busName = "";
        root.getChild(busName).subscribe(listener);
    }
    
    public synchronized void unsubscribe(Object listener) {
        unsubscribe("", listener);
    }
    
    public synchronized void unsubscribe(String busName, Object listener) {
        if(listener == null)
            return;
        if(busName == null)
            busName = "";
        root.getChild(busName).unsubscribe(listener);
    }
    
    public synchronized void publish(Object event) {
        publish("", event);
    }
    
    public synchronized void publish(String busName, Object event) {
        if(event == null)
            throw new NullPointerException("Can not publish null events!");
        if(busName == null)
            busName = "";
        EventBus bus = root.getChild(busName);
        
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        bus.fillSubscriptions(subscriptions, event.getClass());
        
        for(Subscription s : subscriptions)
            s.publish(event);
    }
}