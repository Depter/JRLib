package org.jrlib.vector;

import org.jrlib.TestConfig;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractVectorTest {

    private final static int SIZE = 10;
    private AbstractVector data;
    
    public AbstractVectorTest() {
    }

    @Before
    public void setUp() {
        data = new AbstractVectorImpl(SIZE);
    }
    
    @Test
    public void testWithinBonds() {
        assertFalse(data.withinBonds(-1));
        for(int i=0; i<SIZE; i++)
            assertTrue(data.withinBonds(i));
        assertFalse(data.withinBonds(SIZE));
    }

    @Test
    public void testToArray() {
        double[] arr = data.toArray();
        double[] expected = {0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d};
        assertArrayEquals(expected, arr, TestConfig.EPSILON);
        
        arr[1] = 25d;
        assertEquals(1d, data.getValue(1), TestConfig.EPSILON);
    }

    private class AbstractVectorImpl extends AbstractVector {
        
        private int size;
        
        private AbstractVectorImpl(int size) {
            this.size = size;
        }
        
        @Override
        protected void recalculateLayer() {
        }

        @Override
        public int getLength() {
            return size;
        }

        @Override
        public double getValue(int index) {
            return index;
        }
    }

}