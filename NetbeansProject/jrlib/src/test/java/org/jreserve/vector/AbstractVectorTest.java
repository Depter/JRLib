package org.jreserve.vector;

import org.jreserve.JRLibTestSuite;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractVectorTest {
    
    private final static double[] DUMMY_DATA = {1d, 2d, 3d, 4d};
    
    private AbstractVectorImpl vector;
    
    public AbstractVectorTest() {
    }

    @Before
    public void setUp() {
        vector = new AbstractVectorImpl(DUMMY_DATA);
    }

    @Test
    public void testWithinBound() {
        assertFalse(vector.withinBound(-1));
        for(int i=0; i<DUMMY_DATA.length; i++)
            assertTrue(vector.withinBound(i));
        assertFalse(vector.withinBound(DUMMY_DATA.length));
    }

    @Test
    public void testToArray() {
        double[] found = vector.toArray();
        assertArrayEquals(DUMMY_DATA, found, JRLibTestSuite.EPSILON);
        
        found[1] = 3.5d;
        assertNotEquals(found[1], vector.data[1], JRLibTestSuite.EPSILON);
    }

    @Test
    public void testToArray_Null() {
        vector = new AbstractVectorImpl(null);
        assertEquals(0, vector.toArray().length);
    }

    private class AbstractVectorImpl extends AbstractVector {
        
        private double[] data;
        
        private AbstractVectorImpl(double[] data) {
            this.data = data;
        }
        
        @Override
        protected void recalculateLayer() {
        }

        public int getLength() {
            return data==null? 0 : data.length;
        }

        public double getValue(int index) {
            return withinBound(index) ?
                    data[index] :
                    Double.NaN;
        }
    }
}