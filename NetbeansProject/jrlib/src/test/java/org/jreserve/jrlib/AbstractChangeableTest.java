package org.jreserve.jrlib;

import org.jreserve.jrlib.AbstractChangeable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractChangeableTest {

    private AbstractChangeable changeable;
    private Listener listener;
    
    public AbstractChangeableTest() {
    }

    @Before
    public void setUp() {
        changeable = new AbstractChangeable();
        listener = new Listener();
    }

    @Test
    public void testAddChangeListener() {
        changeable.addChangeListener(listener);
        changeable.fireChange();
        assertEquals(1, listener.changeCount);
        
        changeable.removeChangeListener(listener);
        changeable.fireChange();
        assertEquals(1, listener.changeCount);
    }

    @Test
    public void testAddChangeListener_Twice() {
        changeable.addChangeListener(listener);
        changeable.addChangeListener(listener);
        changeable.fireChange();
        assertEquals(1, listener.changeCount);
    }

    private class Listener implements ChangeListener {
        
        private int changeCount;
        
        public void stateChanged(ChangeEvent e) {
            changeCount++;
        }
    }
}