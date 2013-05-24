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