package org.jreserve.triangle.claim;

import org.jreserve.triangle.claim.ClaimTriangleCorrection;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.jreserve.JRLibTestUtl.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ClaimTriangleCorrectionTest {

    private final static double CORRECTION = 0.5;
    private final static int ACCIDENT = 0;
    private final static int DEVELOPMENT = 3;

    private ClaimTriangle source;
    private ClaimTriangleCorrection corrigated;
    private ClaimTriangleCorrection outsider;
    
    public ClaimTriangleCorrectionTest() {
    }

    @Before
    public void setUp() {
        source = InputTriangleTest.createData();
        corrigated = new ClaimTriangleCorrection(source, ACCIDENT, DEVELOPMENT, CORRECTION);
        int dev = source.getDevelopmentCount(ACCIDENT);
        outsider = new ClaimTriangleCorrection(source, ACCIDENT, dev, CORRECTION);
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