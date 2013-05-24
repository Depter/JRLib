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

import java.util.Collection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface EventBus<T> {
    
    /**
     * Publishes the event to the listeners.
     * 
     * @throws NullPointerException if `event` is null.
     */
    public void publishEvent(Object event);
    
    /**
     * Publishes the events to the listeners. Eache
     * unique element will be published only once.
     * 
     * @throws NullPointerException if any elements of `events` is null.
     */
    public void publishEvents(Collection events);
    
    /**
     * Subscribes the listener for all kind of instances.
     */
    public void subscribe(EventBusListener<T> listener);
    
    /**
     * Subscribes the listener for a given
     * class ands it's sublcasses. If the listener for already
     * subscribed for a superclass, nothing will happen. If a listener
     * was already subscribed for a sub-class, then it's subscription
     * is widened to this class.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public <L extends T> void subscribe(Class<L> clazz, EventBusListener<L> listener);
    
    /**
     * Unsubscribes the listener from all instances.
     */
    public void unsubscribe(EventBusListener listener);
}
