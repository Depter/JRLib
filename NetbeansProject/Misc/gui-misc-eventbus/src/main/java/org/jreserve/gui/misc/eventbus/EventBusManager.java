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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusManager {
    
    private final static String DISPATCHER_NAME = "EventBus_Dispatcher";
    private final static Logger logger = Logger.getLogger(EventBusManager.class.getName());
    
    private static EventBusManager INSTANCE;
    
    public synchronized static EventBusManager getDefault() {
        if(INSTANCE == null) {
            logger.info("Starting EventBusManager...");
            INSTANCE = new EventBusManager();
            INSTANCE.dispatcherThread.start();
        }
        return INSTANCE;
    }
    
    private final EventBus root = new EventBus();
    private final List<Event> events = new LinkedList<Event>();
//    private final BlockingQueue<Event> eventQue = new LinkedBlockingQueue<Event>();
    private final EventDispatcher dispatcher = new EventDispatcher();
    private final Thread dispatcherThread;
    
    private EventBusManager() {
        dispatcherThread = new Thread(dispatcher, DISPATCHER_NAME);
        dispatcherThread.setPriority(Thread.NORM_PRIORITY);
        dispatcherThread.setDaemon(true);
    }
    
    public void subscribe(Object listener) {
        subscribe("", listener);
    }
    
    public void subscribe(String busName, Object listener) {
        if(listener == null)
            throw new NullPointerException("Listener can not be null!");
        if(busName == null)
            busName = "";
        synchronized(root) {
            root.getChild(busName).subscribe(listener);
        }
    }
    
    public void unsubscribe(Object listener) {
        unsubscribe("", listener);
    }
    
    public void unsubscribe(String busName, Object listener) {
        if(listener == null)
            return;
        if(busName == null)
            busName = "";
        
        synchronized(root) {
            root.getChild(busName).unsubscribe(listener);
        }
    }
    
    public void publish(Object event) {
        publish("", event);
    }
    
    public void publish(String busName, Object value) {
        if(value == null)
            throw new NullPointerException("Can not publish null events!");
        if(busName == null)
            busName = "";
        
        synchronized(events) {
            events.add(new Event(busName, value));
            events.notifyAll();
        }
    }
    
    
    private class EventDispatcher implements Runnable {
        
        @Override
        public void run() {
            try {
                doEventLoop();
            } catch (InterruptedException ex) {
                String msg = "EventBus dispatcher thread interrupted!";
                logger.log(Level.WARNING, msg, ex);
            }
        }
        
        private void doEventLoop() throws InterruptedException {
            while(true) {
                synchronized(events) {
                    while(!events.isEmpty())
                        publishEvent(events.remove(0));
                    events.wait();
                }
            }
        }
        
        private void publishEvent(Event event) {
            Object value = event.value;
            for(Subscription s : getSubscriptons(event.bus, value.getClass()))
                s.publish(value);
        }
        
        private List<Subscription> getSubscriptons(String busName, Class clazz) {
            synchronized(root) {
                EventBus bus = root.getChild(busName);
                List<Subscription> subscriptions = new ArrayList<Subscription>();
                bus.fillSubscriptions(subscriptions, clazz);
                return subscriptions;
            }
        }
    }
    
    private static class Event {
        private final String bus;
        private final Object value;

        private Event(String bus, Object value) {
            this.bus = bus;
            this.value = value;
        }        
    }
}