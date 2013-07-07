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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusTest {
    
    public EventBusTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getName method, of class EventBus.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        EventBus instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPath method, of class EventBus.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        EventBus instance = null;
        String expResult = "";
        String result = instance.getPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChild method, of class EventBus.
     */
    @Test
    public void testGetChild() {
        System.out.println("getChild");
        String path = "";
        EventBus instance = null;
        EventBus expResult = null;
        EventBus result = instance.getChild(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of subscribe method, of class EventBus.
     */
    @Test
    public void testSubscribe() {
        System.out.println("subscribe");
        Object listener = null;
        EventBus instance = null;
        instance.subscribe(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unsubscribe method, of class EventBus.
     */
    @Test
    public void testUnsubscribe() {
        System.out.println("unsubscribe");
        Object listener = null;
        EventBus instance = null;
        instance.unsubscribe(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fillSubscriptions method, of class EventBus.
     */
    @Test
    public void testFillSubscriptions() {
        System.out.println("fillSubscriptions");
        List<Subscription> subscriptions = null;
        Class eventClass = null;
        EventBus instance = null;
        instance.fillSubscriptions(subscriptions, eventClass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class EventBus.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        EventBus instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class EventBus.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        EventBus instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class EventBus.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        EventBus instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}