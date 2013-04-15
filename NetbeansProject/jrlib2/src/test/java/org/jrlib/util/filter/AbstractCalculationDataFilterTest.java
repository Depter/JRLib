package org.jrlib.util.filter;

import org.jrlib.CalculationData;
import org.jrlib.ChangeCounter;
import org.jrlib.TestConfig;
import static org.jrlib.TestData.EXPOSURE;
import static org.jrlib.TestData.getCachedVector;
import org.jrlib.vector.InputVector;
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
