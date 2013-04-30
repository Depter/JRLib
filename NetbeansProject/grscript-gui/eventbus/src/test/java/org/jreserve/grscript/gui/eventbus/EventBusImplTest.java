package org.jreserve.grscript.gui.eventbus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EventBusImplTest {
    
    private EventBusImpl bus;
    private EventBusListenerImpl listener;
    
    public EventBusImplTest() {
    }

    @Before
    public void setUp() {
        bus = new EventBusImpl();
        listener = new EventBusListenerImpl();
    }

    @Test(expected = NullPointerException.class)
    public void testPublish_Single_Null() {
        bus.publishEvents(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPublish_Collection_Null() {
        List items = new ArrayList();
        items.add(new Object());
        items.add(null);
        items.add(new Object());
        bus.publishEvents(items);
    }

    @Test
    public void testPublish_Collection_Null_NoPublish() {
        List items = new ArrayList();
        items.add(new Object());
        items.add(null);
        items.add(new Object());
        bus.subscribe(listener);
        try {
            bus.publishEvents(items);
        } catch (NullPointerException ex) {}
        assertEquals(0, listener.callCount);
    }

    @Test
    public void testPublish_Collection() {
        List items = new ArrayList();
        items.add(new Object());
        items.add(new Object());
        bus.subscribe(listener);
        bus.publishEvents(items);
        assertEquals(1, listener.callCount);
        assertEquals(2, listener.publishCount);
        
        items.set(1, items.get(0));
        bus.publishEvents(items);
        assertEquals(2, listener.callCount);
        assertEquals(3, listener.publishCount);
    }

    @Test
    public void testSubscribe_Class_EventBusListener() {
        EventBusListenerImpl l2 = new EventBusListenerImpl();
        bus.subscribe(Integer.class, l2);
        bus.subscribe(listener);
        
        List items = new ArrayList();
        items.add(new Object());
        items.add(2);
        
        bus.publishEvents(items);
        assertEquals(1, listener.callCount);
        assertEquals(2, listener.publishCount);
        
        assertEquals(1, l2.callCount);
        assertEquals(1, l2.publishCount);
    }

    @Test
    public void testUnsubscribe() {
        bus.subscribe(Integer.class, listener);
        bus.subscribe(listener);
        
        List items = new ArrayList();
        items.add(new Object());
        items.add(2);
        
        bus.publishEvents(items);
        assertEquals(1, listener.callCount);
        assertEquals(2, listener.publishCount);
        
        bus.unsubscribe(listener);
        bus.publishEvents(items);
        assertEquals(1, listener.callCount);
        assertEquals(2, listener.publishCount);
    }
    
    private static class EventBusListenerImpl implements EventBusListener {
        
        private int publishCount = 0;
        private int callCount = 0;
        
        @Override
        public void published(Collection instance) {
            callCount++;
            publishCount += instance.size();
        }
    
    }
}