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
public class SubscriptionTest {
    
    private Listener listener;
    private Subscription subscription;
    
    public SubscriptionTest() {
    }
    
    @Before
    public void setUp() {
        listener = new Listener();
        subscription = new Subscription(listener);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContructor_NoAnnotation() {
        subscription = new Subscription(new Object() {
            public void listen(Double event) {
            }
        });
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContructor_NoParam() {
        subscription = new Subscription(new Object() {
            @EventBusListener
            public void listen() {
            }
        });
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContructor_PrimitiveParam() {
        subscription = new Subscription(new Object() {
            @EventBusListener
            public void listen(double event) {
            }
        });
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContructor_NotAccessible() {
        subscription = new Subscription(new Object() {
            @EventBusListener
            void listen(Double event) {
            }
        });
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
    
    private class Listener {
        
        private int callCount;
        
        @EventBusListener
        public void listen(Number event) {
            callCount++;
        }
    }
}