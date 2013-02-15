package org.jreserve.triangle;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleOutlierFilterTest {
    
    private final static boolean[][] TRESHOLD_1_5 = {
        {false, false, false, false, false, false, false},
        {false, false, false, false, false, false},
        {true , false, true , false, false},
        {false, true , false, false},
        {false, false, false},
        {false, false},
        {false},
    };
    
    private final static boolean[][] TRESHOLD_1 = {
        {false, false, true , false, true , false, false},
        {false, false, false, true , false, false},
        {true , false, true , false, false},
        {false, true , false, true},
        {false, false, false},
        {false, false},
        {true},
    };
    
    private Triangle source;
    private TriangleOutlierFilter filter;
    private ChangeCounter counter;
    
    public TriangleOutlierFilterTest() {
    }

    @Before
    public void setUp() {
        createSource();
        filter = new TriangleOutlierFilter(source);
        counter = new ChangeCounter();
        filter.addChangeListener(counter);
    }
    
    private void createSource() {
        source = new InputTriangle(TestData.INCURRED);
        source = new TriangleCummulation(source);
        source = new DevelopmentFactors(source);
    }

    @Test
    public void testGetTreshold() {
        //Default treshold is 1.5
        assertEquals(1.5, filter.getTreshold(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testSetTreshold() {
        double treshold = 2d;
        filter.setTreshold(treshold);
        assertEquals(treshold, filter.getTreshold(), JRLibTestSuite.EPSILON);
        assertEquals(1, counter.getChangeCount());
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(source.getAccidentCount(), filter.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(source.getDevelopmentCount(), filter.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = source.getAccidentCount();
        for(int a=-1; a<=accidents; a++)
            assertEquals(source.getDevelopmentCount(a), filter.getDevelopmentCount(a));
    }

    @Test
    public void testIsOutlier() {
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertEquals("At ["+a+"; "+d+"]", TRESHOLD_1_5[a][d], filter.isOutlier(a, d));
            assertFalse(filter.isOutlier(a, devs));
        }
        assertFalse(filter.isOutlier(accidents, 0));
        
        filter.setTreshold(1d);
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertEquals("At ["+a+"; "+d+"]", TRESHOLD_1[a][d], filter.isOutlier(a, d));
            assertFalse(filter.isOutlier(a, devs));
        }
        assertFalse(filter.isOutlier(accidents, 0));
    }
    
    @Test
    public void testToArray() {
        boolean[][] found = filter.toArray();
        assertEquals(TRESHOLD_1_5.length, found.length);
        for(int a=0; a<found.length; a++) {
            assertEquals(TRESHOLD_1_5[a].length, found[a].length);
            for(int d=0; d<found[a].length; d++)
                assertEquals(TRESHOLD_1_5[a][d], found[a][d]);
        }
        
        found[0][0] = !found[0][0];
        assertNotEquals(found[0][0], filter.isOutlier(0, 0));
    }
}