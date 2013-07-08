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

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusManagerTest {
    
    private static EventBusManager ebm;
    
    public EventBusManagerTest() {
    }
    
    @BeforeClass
    public static void setUp() {
        ebm = EventBusManager.getDefault();
    }

    @Test
    public void testSubscribe_Object() throws InterruptedException {
        Listener listener = new Listener();
        ebm.subscribe(listener);
        ebm.publish(Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(1, listener.eventCount);
    }

    @Test
    public void testSubscribe_String_Object() throws InterruptedException {
        Listener listener = new Listener();
        ebm.subscribe("bus", listener);
        ebm.publish(Double.valueOf(0.5));
        ebm.publish("bus", Double.valueOf(0.5));
        ebm.publish("bus.bus2", Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(2, listener.eventCount);
    }

    @Test
    public void testUnsubscribe_Object() throws InterruptedException {
        Listener listener = new Listener();
        ebm.subscribe(listener);
        ebm.publish(Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(1, listener.eventCount);
        
        ebm.unsubscribe(listener);
        ebm.publish(Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(1, listener.eventCount);
    }

    @Test
    public void testUnsubscribe_String_Object() throws InterruptedException {
        Listener listener = new Listener();
        ebm.subscribe("bus", listener);
        ebm.publish("bus", Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(1, listener.eventCount);
        
        ebm.unsubscribe("bus", listener);
        ebm.publish("bus", Double.valueOf(0.5));
        Thread.sleep(100);
        assertEquals(1, listener.eventCount);
    }
    
    private class Listener {
        private int eventCount;
        
        @EventBusListener
        public void event(Double value) {
            eventCount++;
        }
    }
}