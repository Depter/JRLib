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
