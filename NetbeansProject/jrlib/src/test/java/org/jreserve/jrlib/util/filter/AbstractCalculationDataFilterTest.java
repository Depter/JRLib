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
package org.jreserve.jrlib.util.filter;

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import static org.jreserve.jrlib.TestData.EXPOSURE;
import static org.jreserve.jrlib.TestData.getCachedVector;
import org.jreserve.jrlib.vector.InputVector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCalculationDataFilterTest {

    private CalculationData source;
    private AbstractCalculationDataFilterImpl filter;
    private ChangeCounter counter;
    
    public AbstractCalculationDataFilterTest() {
    }

    @Before
    public void setUp() {
        source = new InputVector(getCachedVector(EXPOSURE));
        filter = new AbstractCalculationDataFilterImpl(source);
        counter = new ChangeCounter();
        filter.addChangeListener(counter);
    }

    @Test(expected=NullPointerException.class)
    public void testContructor_NullFilter() {
        filter = new AbstractCalculationDataFilterImpl(source, null);
    }

    @Test
    public void testGetFilter() {
        Filter f = filter.getFilter();
        assertTrue(f instanceof SigmaFilter);
        
        double expected = SigmaFilter.DEFAULT_TRESHOLD;
        double found = ((SigmaFilter)f).getTreshold();
        assertEquals(expected, found, TestConfig.EPSILON);
    }

    @Test
    public void testSetFilter() {
        Filter expected = new SigmaFilter(2d);
        filter.setFilter(expected);
        assertEquals(expected, filter.getFilter());
        assertEquals(1, counter.getChangeCount());
    }
    
    @Test(expected=NullPointerException.class)
    public void testSetFilter_NullValue() {
        filter.setFilter(null);
    }

    private class AbstractCalculationDataFilterImpl extends AbstractCalculationDataFilter {

        public AbstractCalculationDataFilterImpl(CalculationData source, Filter filter) {
            super(source, filter);
        }

        public AbstractCalculationDataFilterImpl(CalculationData source) {
            super(source);
        }

        @Override
        protected void recalculateLayer() {
        }
    }

}
