package org.jreserve.triangle;

import static org.jreserve.JLibTestSuite.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class TriangleCorrectionTest {

    private final static double CORRECTION = 0.5;
    private final static int ACCIDENT = 0;
    private final static int DEVELOPMENT = 3;

    private TriangularCalculationData source;
    private TriangleCorrection corrigated;
    private TriangleCorrection outsider;
    
    public TriangleCorrectionTest() {
    }

    @Before
    public void setUp() {
        source = InputTriangleTest.createData();
        corrigated = new TriangleCorrection(source, ACCIDENT, DEVELOPMENT, CORRECTION);
        int dev = source.getDevelopmentCount(ACCIDENT);
        outsider = new TriangleCorrection(source, ACCIDENT, dev, CORRECTION);
    }

    @Test
    public void testGetAccident() {
        assertEquals(ACCIDENT, corrigated.getAccident());
        assertEquals(ACCIDENT, outsider.getAccident());
    }

    @Test
    public void testGetDevelopment() {
        assertEquals(DEVELOPMENT, corrigated.getDevelopment());
        int dev = source.getDevelopmentCount(ACCIDENT);
        assertEquals(dev, outsider.getDevelopment());
    }

    @Test
    public void testGetCorrection() {
        assertEquals(CORRECTION, corrigated.getCorrection(), EPSILON);
        assertEquals(CORRECTION, outsider.getCorrection(), EPSILON);
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