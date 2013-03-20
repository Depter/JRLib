package org.jreserve.estimate;

import org.jreserve.JRLibTestUtl;
import org.jreserve.triangle.Cell;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractEstimateTest {

    private final static double[][] EXPECTED = {
        {1 , 2 , 3 , 4 , 5 },
        {6 , 7 , 8 , 9 , 10},
        {11, 12, 13, 14, 15}
    };
    
    private final static double[] RESERVES = {
        0, 1, 2
    };
    
    private AbstractEstimateImpl estimate;
    
    public AbstractEstimateTest() {
    }

    @Before
    public void setUp() {
        estimate = new AbstractEstimateImpl();
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(EXPECTED.length, estimate.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(EXPECTED[0].length, estimate.getDevelopmentCount());
    }

    @Test
    public void testGetValue_Cell() {
        assertEquals(Double.NaN, estimate.getValue(new Cell(0, -1)), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell(-1, 0)), JRLibTestUtl.EPSILON);
        
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED[a][d], estimate.getValue(new Cell(a, d)), JRLibTestUtl.EPSILON);
        
        assertEquals(Double.NaN, estimate.getValue(new Cell(0, developments)), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell(accidents, 0)), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetValue_int_int() {
        assertEquals(Double.NaN, estimate.getValue(0, -1), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(-1, 0), JRLibTestUtl.EPSILON);
        
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED[a][d], estimate.getValue(a, d), JRLibTestUtl.EPSILON);
        
        assertEquals(Double.NaN, estimate.getValue(0, developments), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(accidents, 0), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testWithinBounds() {
        assertFalse(estimate.withinBounds(0, -1));
        assertFalse(estimate.withinBounds(-1, 0));
        
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertTrue(estimate.withinBounds(a, d));
        
        assertFalse(estimate.withinBounds(0, developments));
        assertFalse(estimate.withinBounds(accidents, 0));
    }

    @Test
    public void testToArray() {
        double[][] found = estimate.toArray();
        int accidents = EXPECTED.length;
        assertEquals(accidents, found.length);
        for(int a=0; a<accidents; a++)
            assertArrayEquals(EXPECTED[a], found[a], JRLibTestUtl.EPSILON);
    }

    @Test
    public void testDevelopmentEnd() {
        int devs = EXPECTED[0].length;
        assertEquals(devs--, estimate.getObservedDevelopmentCount(0));
        assertEquals(devs--, estimate.getObservedDevelopmentCount(1));
        assertEquals(devs--, estimate.getObservedDevelopmentCount(2));
    }
    
    @Test
    public void testGetReserve_int() {
        assertEquals(Double.NaN, estimate.getReserve(-1), JRLibTestUtl.EPSILON);
        int accidents = EXPECTED.length;
        for(int a=0; a<accidents; a++)
            assertEquals(RESERVES[a], estimate.getReserve(a), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getReserve(accidents), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetReserve_0args() {
        assertEquals(3d, estimate.getReserve(), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testToArrayReserve() {
        double[] found = estimate.toArrayReserve();
        assertArrayEquals(RESERVES, found, JRLibTestUtl.EPSILON);
    }

    private class AbstractEstimateImpl extends AbstractEstimate {

        private AbstractEstimateImpl() {
            recalculateLayer();
        }
        
        @Override
        public int getObservedDevelopmentCount(int accident) {
            return EXPECTED[0].length - accident;
        }

        @Override
        protected void recalculateLayer() {
            accidents = EXPECTED.length;
            developments = EXPECTED[0].length;
            values = EXPECTED;
        }

        public Estimate copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}