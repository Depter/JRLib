package org.jreserve.util;

import org.jreserve.CalculationData;
import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import static org.jreserve.TestData.EXPOSURE;
import static org.jreserve.TestData.getCachedVector;
import org.jreserve.vector.InputVector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
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
        assertEquals(SigmaFilter.DEFAULT_TRESHOLD, ((SigmaFilter)f).getTreshold(), JRLibTestSuite.EPSILON);
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