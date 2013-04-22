package org.jreserve.jrlib;

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.AbstractCalculationData;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    
    public AbstractCalculationDataTest() {
    }

    @Before
    public void setUp() {
        source = new AbstractCalculationDataImpl();
        data = new AbstractCalculationDataImpl(source);
    }

    @Test
    public void testRecalculate_Source() {
        source.recalculate();
        assertEquals(1, source.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Source_Detached() {
        source.detach();
        source.recalculate();
        assertEquals(1, source.recalculateCount);
        assertEquals(0, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Data() {
        data.recalculate();
        assertEquals(1, source.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testRecalculate_Data_Detached() {
        data.detach();
        data.recalculate();
        assertEquals(1, source.recalculateCount);
        assertEquals(1, data.recalculateCount);
    }

    @Test
    public void testDetach_Source() {
        Listener sourceListener = new Listener();
        source.addChangeListener(sourceListener);
        
        Listener dataListener = new Listener();
        data.addChangeListener(dataListener);
        
        source.recalculate();
        assertEquals(1, sourceListener.count);
        assertEquals(1, dataListener.count);
        
        source.detach();
        data.recalculate();
        assertEquals(1, sourceListener.count);
        assertEquals(2, dataListener.count);
    }

    @Test
    public void testDetach_Data() {
        Listener sourceListener = new Listener();
        source.addChangeListener(sourceListener);
        
        Listener dataListener = new Listener();
        data.addChangeListener(dataListener);
        
        data.recalculate();
        assertEquals(1, sourceListener.count);
        assertEquals(1, dataListener.count);
        
        data.detach();
        data.recalculate();
        assertEquals(1, sourceListener.count);
        assertEquals(1, dataListener.count);
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

    private class Listener implements ChangeListener {
        
        private int count;
        
        @Override
        public void stateChanged(ChangeEvent e) {
            count++;
        }
    
    }
}