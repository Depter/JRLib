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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusTest {
    
    private EventBus bus;
    
    public EventBusTest() {
    }
    
    @Before
    public void setUp() {
        bus = new EventBus();
    }

    @Test
    public void testGetName() {
        assertEquals("", bus.getName());
    }

    @Test
    public void testGetPath() {
        String n2 = "bus2";
        EventBus bus2 = bus.getChild(n2);
        assertEquals(n2, bus2.getName());
        assertEquals(n2, bus2.getPath());
    }

    @Test
    public void testGetChild() {
        EventBus child = bus.getChild("bus2.bus3");
        assertEquals("bus3", child.getName());
        assertEquals("bus2.bus3", child.getPath());
        
        child = bus.getChild("bus2.bus4");
        assertEquals("bus4", child.getName());
        assertEquals("bus2.bus4", child.getPath());
    }

    @Test
    public void testSubscribe() {
        Listener listener = new Listener();
        bus.subscribe(listener);
        
        List<Subscription> ss = new ArrayList<Subscription>();
        bus.fillSubscriptions(ss, Double.class);
        assertEquals(1, ss.size());
        assertTrue(ss.get(0).references(listener));
        
        bus.unsubscribe(listener);
        ss.clear();
        bus.fillSubscriptions(ss, Double.class);
        assertEquals(0, ss.size());
        
        EventBus bus3 = bus.getChild("bus2.bus3");
        Listener l3 = new Listener();
        bus3.subscribe(l3);
        
        EventBus bus2 = bus.getChild("bus2");
        Listener l2 = new Listener();
        bus2.subscribe(l2);
        
        bus.subscribe(listener);
        
        ss.clear();
        bus3.fillSubscriptions(ss, Double.class);
        assertEquals(3, ss.size());
        
        ss.clear();
        bus2.fillSubscriptions(ss, Double.class);
        assertEquals(2, ss.size());
        
        ss.clear();
        bus.fillSubscriptions(ss, Double.class);
        assertEquals(1, ss.size());
    }
    
    private class Listener {
        
        private int count;
        
        @EventBusListener
        public void event(Double value) {
            count++;
        }
    }
}