package org.jreserve.vector;

import org.jreserve.util.SigmaFilter;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class VectorOutlierFilterTest {
    
    private final static double[] INPUT_DATA = {
        1.17504025292399, 1.18852084713466, 1.26574981107913,
        1.18528951312087, 1.23751861209000, 1.19236082906881,
        1.13771881327519,
    };
    
    private final static boolean[] TRESHOLD_1 = {
        false, false, true, false, false, false, true
    };
    
    private final static boolean[] TRESHOLD_1_5 = {
        false, false, true, false, false, false, false
    };
    
    private VectorOutlierFilter filter;
    
    public VectorOutlierFilterTest() {
    }

    @Before
    public void setUp() {
        filter = new VectorOutlierFilter(new InputVector(INPUT_DATA));
    }

    @Test
    public void testGetLength() {
        assertEquals(INPUT_DATA.length, filter.getLength());
    }

    @Test
    public void testIsOutlier() {
        int length = INPUT_DATA.length;
        assertFalse(filter.isOutlier(-1));
        for(int i=0; i<length; i++)
            assertEquals(TRESHOLD_1_5[i], filter.isOutlier(i));
        assertFalse(filter.isOutlier(length));
        
        filter.setFilter(new SigmaFilter(1d));
        assertFalse(filter.isOutlier(-1));
        for(int i=0; i<length; i++)
            assertEquals(TRESHOLD_1[i], filter.isOutlier(i));
        assertFalse(filter.isOutlier(length));
    }

    @Test
    public void testToArray() {
        boolean[] found = filter.toArray();
        assertEquals(TRESHOLD_1_5.length, found.length);
        for(int i=0; i<found.length; i++)
            assertEquals(TRESHOLD_1_5[i], found[i]);
    }
}