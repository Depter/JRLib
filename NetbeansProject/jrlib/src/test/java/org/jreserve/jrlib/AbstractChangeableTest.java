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

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractChangeableTest {

    private AbstractChangeableImpl source;
    private ChangeCounter listener;
    
    public AbstractChangeableTest() {
    }

    @Before
    public void setUp() {
        source = new AbstractChangeableImpl();
        listener = new ChangeCounter();
        source.addCalculationListener(listener);
    }

    @Test
    public void testAddChangeListener() {
        source.addCalculationListener(listener);
        source.fireStateChange();
        assertEquals(1, listener.getChangeCount());
        
        source.removeCalculationListener(listener);
        source.fireStateChange();
        assertEquals(1, listener.getChangeCount());
    }

    @Test
    public void testAddChangeListener_Twice() {
        source.addCalculationListener(listener);
        source.addCalculationListener(listener);
        source.fireStateChange();
        assertEquals(1, listener.getChangeCount());
    }

    @Test
    public void testRecalculate() {
        source.recalculate();
        assertEquals(2, listener.getChangeCount());
        assertEquals(CalculationState.INVALID, listener.getStateAt(0));
        assertEquals(CalculationState.VALID, listener.getStateAt(1));
        assertEquals(1, source.calculationCount);
    }
    
    private static class AbstractChangeableImpl extends AbstractChangeable {

        private int calculationCount = 0;
        
        public AbstractChangeableImpl() {
        }
        
        @Override
        protected void recalculateLayer() {
            calculationCount++;
        }

        @Override
        protected CalculationState getSourceState() {
            return CalculationState.VALID;
        }

        public void detach() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void detach(CalculationData source) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}