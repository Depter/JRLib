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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SubscriptionTest {
    
    private Listener listener;
    private Subscription subscription;
    
    public SubscriptionTest() {
    }
    
    @Before
    public void setUp() throws NoSuchMethodException {
        listener = new Listener();
        Method m = Listener.class.getMethod("listen", Number.class);
        subscription = new Subscription(listener, m);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(subscription.isEmpty());
    }

    @Test
    public void testReferences() {
        assertTrue(subscription.references(listener));
        
        Listener l = new Listener();
        assertFalse(subscription.references(l));
    }

    @Test
    public void testAddSelf() {
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        subscription.addSelf(subscriptions, Double.class);
        assertEquals(1, subscriptions.size());
        
        subscription.addSelf(subscriptions, Double.class);
        assertEquals(1, subscriptions.size());
        
        subscriptions = new ArrayList<Subscription>();
        subscription.addSelf(subscriptions, Integer.class);
        assertEquals(1, subscriptions.size());
        
        subscriptions = new ArrayList<Subscription>();
        subscription.addSelf(subscriptions, String.class);
        assertEquals(0, subscriptions.size());
    }

    @Test
    public void testPublish() {
        subscription.publish(Double.valueOf(0.5));
        assertEquals(1, listener.callCount);
        
        subscription.publish(Integer.valueOf(1));
        assertEquals(2, listener.callCount);
    }

    @Test
    public void testPublish_FromEdt() throws NoSuchMethodException {
        class EdtListener {
            @EventBusListener(forceEDT = true)
            public void listen(Double value) {
                boolean onEdt = SwingUtilities.isEventDispatchThread();
                System.out.println("On EDT: "+onEdt);
                assertTrue(onEdt);
            }
        }
        
        EdtListener l = new EdtListener();
        Method m = EdtListener.class.getMethod("listen", Double.class);
        Subscription s = new Subscription(l, m);
        s.publish(Double.valueOf(0.5));
    }
    
    private class Listener {
        
        private int callCount;
        
        @EventBusListener
        public void listen(Number event) {
            callCount++;
        }
    }
}