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
 * @version 1.0
 */
public class AbstractMultiSourceCalculationDataTest {   
    
    private AbstractMultiSourceCalculationDataImpl source1;
    private AbstractMultiSourceCalculationDataImpl source2;
    private AbstractMultiSourceCalculationDataImpl data;
    private ChangeCounter dataListener;

    @Before
    public void setUp() {
        source1 = new AbstractMultiSourceCalculationDataImpl();
        source2 = new AbstractMultiSourceCalculationDataImpl();
        data = new AbstractMultiSourceCalculationDataImpl(source1, source2);
        dataListener = new ChangeCounter();
        data.addCalculationListener(dataListener);
    }

    @Test
    public void testRecalculate_Source() {
        source1.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(0, source2.recalculateCount);
        assertEquals(1, data.recalculateCount);
        
        source2.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(1, source2.recalculateCount);
        assertEquals(2, data.recalculateCount);
    }
    
    @Test
    public void testGetSourceState() {
        assertEquals(CalculationState.VALID, data.getSourceState());
        
        source1.setState(CalculationState.INVALID);
        assertEquals(CalculationState.INVALID, data.getSourceState());
        
        source2.setState(CalculationState.INVALID);
        assertEquals(CalculationState.INVALID, data.getSourceState());
        
        source1.setState(CalculationState.VALID);
        assertEquals(CalculationState.INVALID, data.getSourceState());
        
        source2.setState(CalculationState.VALID);
        assertEquals(CalculationState.VALID, data.getSourceState());        
    }
    
    @Test
    public void testRecalculateInvalidSource() {
        source1.setState(CalculationState.INVALID);
        assertEquals(CalculationState.INVALID, data.getState());
        assertEquals(0, data.recalculateCount);
        
        source2.setState(CalculationState.INVALID);
        assertEquals(CalculationState.INVALID, data.getState());
        assertEquals(0, data.recalculateCount);
        
        source1.setState(CalculationState.VALID);
        assertEquals(CalculationState.INVALID, data.getState());
        assertEquals(0, data.recalculateCount);
        
        source2.setState(CalculationState.VALID);
        assertEquals(CalculationState.VALID, data.getState());
        assertEquals(1, data.recalculateCount);
    }
   
    private class AbstractMultiSourceCalculationDataImpl extends 
            AbstractMultiSourceCalculationData<AbstractMultiSourceCalculationDataImpl> {
        
        private int recalculateCount;
        
        private AbstractMultiSourceCalculationDataImpl() {
        }
        
        private AbstractMultiSourceCalculationDataImpl(AbstractMultiSourceCalculationDataImpl... sources) {
            super(sources);
        }
        
        @Override
        protected void recalculateLayer() {
            recalculateCount++;
        }
    }
}
