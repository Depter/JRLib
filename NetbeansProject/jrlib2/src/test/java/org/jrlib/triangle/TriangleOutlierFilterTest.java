package org.jrlib.triangle;

import org.jrlib.ChangeCounter;
import org.jrlib.TestData;
import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.SigmaFilter;
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
    
    private FactorTriangle source;
    private SigmaFilter filter;
    private TriangleOutlierFilter triangleFilter;
    private ChangeCounter counter;
    
    public TriangleOutlierFilterTest() {
    }

    @Before
    public void setUp() {
        source = TestData.getDevelopmentFactors(TestData.INCURRED);
        filter = new SigmaFilter(1.5);
        triangleFilter = new TriangleOutlierFilter(source, filter);
        counter = new ChangeCounter();
        triangleFilter.addChangeListener(counter);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(source.getAccidentCount(), triangleFilter.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(source.getDevelopmentCount(), triangleFilter.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = source.getAccidentCount();
        for(int a=-1; a<=accidents; a++)
            assertEquals(source.getDevelopmentCount(a), triangleFilter.getDevelopmentCount(a));
    }

    @Test
    public void testIsOutlier() {
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertEquals("At ["+a+"; "+d+"]", TRESHOLD_1_5[a][d], triangleFilter.isOutlier(a, d));
            assertFalse(triangleFilter.isOutlier(a, devs));
        }
        assertFalse(triangleFilter.isOutlier(accidents, 0));
        
        triangleFilter.setFilter(new SigmaFilter(1d));
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertEquals("At ["+a+"; "+d+"]", TRESHOLD_1[a][d], triangleFilter.isOutlier(a, d));
            assertFalse(triangleFilter.isOutlier(a, devs));
        }
        assertFalse(triangleFilter.isOutlier(accidents, 0));
    }
    
    @Test
    public void testToArray() {
        boolean[][] found = triangleFilter.toArray();
        assertEquals(TRESHOLD_1_5.length, found.length);
        for(int a=0; a<found.length; a++) {
            assertEquals(TRESHOLD_1_5[a].length, found[a].length);
            for(int d=0; d<found[a].length; d++)
                assertEquals(TRESHOLD_1_5[a][d], found[a][d]);
        }
        
        found[0][0] = !found[0][0];
        assertNotEquals(found[0][0], triangleFilter.isOutlier(0, 0));
    }

}
