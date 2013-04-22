package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.InputTriangle;
import static org.jreserve.jrlib.TestConfig.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrectionTest {

    private final static double[][] DATA = {
        {1 , 2 , 3 , 4 , 5 , 6 , 7},
        {8 , 9 , 10, 11, 12},
        {13, 14, 15},
        {16}
    };

    private final static double CORRECTION = 0.5;
    private final static int ACCIDENT = 0;
    private final static int DEVELOPMENT = 3;

    private Triangle source;
    private TriangleCorrection corrigated;
    private TriangleCorrection outsider;
    
    public TriangleCorrectionTest() {
    }

    @Before
    public void setUp() {
        source = new InputTriangle(DATA);
        corrigated = new TriangleCorrection(source, ACCIDENT, DEVELOPMENT, CORRECTION);
        int dev = source.getDevelopmentCount(ACCIDENT);
        outsider = new TriangleCorrection(source, ACCIDENT, dev, CORRECTION);
    }

    @Test
    public void testGetAccident() {
        assertEquals(ACCIDENT, corrigated.getCorrigatedAccident());
        assertEquals(ACCIDENT, outsider.getCorrigatedAccident());
    }

    @Test
    public void testGetDevelopment() {
        assertEquals(DEVELOPMENT, corrigated.getCorrigatedDevelopment());
        int dev = source.getDevelopmentCount(ACCIDENT);
        assertEquals(dev, outsider.getCorrigatedDevelopment());
    }

    @Test
    public void testGetCorrection() {
        assertEquals(CORRECTION, corrigated.getCorrigatedValue(), EPSILON);
        assertEquals(CORRECTION, outsider.getCorrigatedValue(), EPSILON);
    }

    @Test
    public void testGetValue() {
        int accidents = source.getAccidentCount();
        
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double expected = source.getValue(a, d);
                assertEquals(expected, outsider.getValue(a, d), EPSILON);
                
                if(a == ACCIDENT && d == DEVELOPMENT)
                    expected = CORRECTION;
                assertEquals(expected, corrigated.getValue(a, d), EPSILON);
            }
        }
        
        int dev = source.getDevelopmentCount(ACCIDENT);
        assertTrue(Double.isNaN(outsider.getValue(ACCIDENT, dev)));
    }

}
