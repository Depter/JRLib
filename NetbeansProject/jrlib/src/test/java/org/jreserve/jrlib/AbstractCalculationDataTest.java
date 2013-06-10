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
public class AbstractCalculationDataTest {

    private AbstractCalculationDataImpl source;
    private AbstractCalculationDataImpl data;
    private ChangeCounter dataListener;
    
    public AbstractCalculationDataTest() {
    }

    @Before
    public void setUp() {
        source = new AbstractCalculationDataImpl();
        data = new AbstractCalculationDataImpl(source);
        dataListener = new ChangeCounter();
        data.addCalculationListener(dataListener);
    }

    @Test
    public void testRecalculate_Source() {
        source.recalculate();
        assertEquals(1, source.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Data() {
        data.recalculate();
        assertEquals(0, source.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }
    
    @Test
    public void testGetSourceState() {
        assertEquals(CalculationState.VALID, data.getSourceState());
        
        source.setState(CalculationState.INVALID);
        assertEquals(CalculationState.INVALID, data.getSourceState());
        
        source.setState(CalculationState.VALID);
        assertEquals(CalculationState.VALID, data.getSourceState());
    }
    
    @Test
    public void testSourceRecalculate() {
        source.recalculate();
        assertEquals(2, dataListener.getChangeCount());
        assertEquals(1, data.recalculateCount);
    }

    public class AbstractCalculationDataImpl extends AbstractCalculationData {
        
        private int recalculateCount;

        public AbstractCalculationDataImpl() {
        }

        public AbstractCalculationDataImpl(CalculationData source) {
            super(source);
        }
        
        @Override
        protected void recalculateLayer() {
            recalculateCount++;
        }
    }
}