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

import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
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
    

    @Before
    public void setUp() {
        source1 = new AbstractMultiSourceCalculationDataImpl();
        source2 = new AbstractMultiSourceCalculationDataImpl();
        data = new AbstractMultiSourceCalculationDataImpl(source1, source2);
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
    public void testRecalculate_Source_Detached() {
        source1.detach();
        source1.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(0, source2.recalculateCount);
        assertEquals(0, data.recalculateCount);
        
        source2.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(1, source2.recalculateCount);
        assertEquals(1, data.recalculateCount);
        
        source2.detach();
        source2.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(2, source2.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Data() {
        data.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(1, source2.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Data_Detached() {
        data.detach();
        data.recalculate();
        assertEquals(1, source1.recalculateCount);
        assertEquals(1, source2.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testDetach_Source() {
        ChangeCounter sl1 = new ChangeCounter();
        source1.addChangeListener(sl1);
        ChangeCounter sl2 = new ChangeCounter();
        source2.addChangeListener(sl2);
        
        ChangeCounter dl = new ChangeCounter();
        data.addChangeListener(dl);
        
        source1.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(0, sl2.getChangeCount());
        assertEquals(1, dl.getChangeCount());
        
        source2.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(1, sl2.getChangeCount());
        assertEquals(2, dl.getChangeCount());
        
        source1.detach();
        data.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(2, sl2.getChangeCount());
        assertEquals(3, dl.getChangeCount());
        
        source2.detach();
        data.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(2, sl2.getChangeCount());
        assertEquals(4, dl.getChangeCount());
    }
 
    @Test
    public void testDetach_Data() {
        ChangeCounter sl1 = new ChangeCounter();
        source1.addChangeListener(sl1);
        ChangeCounter sl2 = new ChangeCounter();
        source2.addChangeListener(sl2);
        
        ChangeCounter dl = new ChangeCounter();
        data.addChangeListener(dl);
        
        data.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(1, sl2.getChangeCount());
        assertEquals(1, dl.getChangeCount());
        
        data.detach();
        data.recalculate();
        assertEquals(1, sl1.getChangeCount());
        assertEquals(1, sl2.getChangeCount());
        assertEquals(1, dl.getChangeCount());
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
