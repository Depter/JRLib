package org.jreserve;

import javax.swing.event.ChangeListener;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractDoubleInputCalculationDataTest {

    private Source primary;
    private Source secondary;
    private AbstractDoubleInputCalculationDataImpl data;
    
    public AbstractDoubleInputCalculationDataTest() {
    }

    @Before
    public void setUp() {
        primary = new Source();
        secondary = new Source();
        data = new AbstractDoubleInputCalculationDataImpl(primary, secondary);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_PrimaryNull() {
        data = new AbstractDoubleInputCalculationDataImpl(null, secondary);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_SecondaryNull() {
        data = new AbstractDoubleInputCalculationDataImpl(primary, null);
    }

    @Test
    public void testGetSource() {
        assertTrue(primary == data.getSource());
    }

    @Test
    public void testGetSecondarySource() {
        assertTrue(secondary == data.getSecondarySource());
    }

    @Test
    public void testRecalculate_Priamry() {
        primary.recalculate();
        assertEquals(1, primary.calculations);
        assertEquals(1, data.calculations);
    }

    @Test
    public void testRecalculate_Priamry_Detached() {
        primary.detach();
        primary.recalculate();
        assertEquals(1, primary.calculations);
        assertEquals(0, secondary.calculations);
        assertEquals(0, data.calculations);
    }

    @Test
    public void testRecalculate_Secondary() {
        secondary.recalculate();
        assertEquals(1, secondary.calculations);
        assertEquals(1, data.calculations);
    }

    @Test
    public void testRecalculate_Secondary_Detached() {
        secondary.detach();
        secondary.recalculate();
        assertEquals(0, primary.calculations);
        assertEquals(1, secondary.calculations);
        assertEquals(0, data.calculations);
    }

    @Test
    public void testRecalculate_Data() {
        data.recalculate();
        assertEquals(1, primary.calculations);
        assertEquals(1, secondary.calculations);
        assertEquals(1, data.calculations);
    }

    @Test
    public void testRecalculate_Data_Detached() {
        data.detach();
        data.recalculate();
        assertEquals(1, primary.calculations);
        assertEquals(1, secondary.calculations);
        assertEquals(1, data.calculations);
    }

    @Test
    public void testDetach_Primary() {
        ChangeCounter primaryListener = new ChangeCounter();
        primary.addChangeListener(primaryListener);
        
        ChangeCounter secondaryListener = new ChangeCounter();
        secondary.addChangeListener(secondaryListener);
        
        ChangeCounter dataListener = new ChangeCounter();
        data.addChangeListener(dataListener);
        
        primary.recalculate();
        assertEquals(1, primaryListener.getChangeCount());
        assertEquals(0, secondaryListener.getChangeCount());
        assertEquals(1, dataListener.getChangeCount());
        
        primary.detach();
        data.recalculate();
        assertEquals(1, primaryListener.getChangeCount());
        assertEquals(1, secondaryListener.getChangeCount());
        assertEquals(2, dataListener.getChangeCount());
    }

    @Test
    public void testDetach_Secondary() {
        ChangeCounter primaryListener = new ChangeCounter();
        primary.addChangeListener(primaryListener);
        
        ChangeCounter secondaryListener = new ChangeCounter();
        secondary.addChangeListener(secondaryListener);
        
        ChangeCounter dataListener = new ChangeCounter();
        data.addChangeListener(dataListener);
        
        secondary.recalculate();
        assertEquals(0, primaryListener.getChangeCount());
        assertEquals(1, secondaryListener.getChangeCount());
        assertEquals(1, dataListener.getChangeCount());
        
        secondary.detach();
        data.recalculate();
        assertEquals(1, primaryListener.getChangeCount());
        assertEquals(1, secondaryListener.getChangeCount());
        assertEquals(2, dataListener.getChangeCount());
    }

    @Test
    public void testDetach_Data() {
        ChangeCounter primaryListener = new ChangeCounter();
        primary.addChangeListener(primaryListener);
        
        ChangeCounter secondaryListener = new ChangeCounter();
        secondary.addChangeListener(secondaryListener);
        
        ChangeCounter dataListener = new ChangeCounter();
        data.addChangeListener(dataListener);
        
        data.recalculate();
        assertEquals(1, primaryListener.getChangeCount());
        assertEquals(1, secondaryListener.getChangeCount());
        assertEquals(1, dataListener.getChangeCount());
        
        data.detach();
        data.recalculate();
        assertEquals(1, primaryListener.getChangeCount());
        assertEquals(1, secondaryListener.getChangeCount());
        assertEquals(1, dataListener.getChangeCount());
    }

    private class AbstractDoubleInputCalculationDataImpl extends AbstractDoubleInputCalculationData<Source, Source> {
        private int calculations;
        
        private AbstractDoubleInputCalculationDataImpl(Source p, Source s) {
            super(p, s);
        }
        
        @Override
        public void recalculateLayer() {
            calculations++;
        }
    }
    
    private class Source extends AbstractCalculationData {
        private int calculations;
        
        @Override
        protected void recalculateLayer() {
            calculations++;
        }
    }

}