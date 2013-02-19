package org.jreserve.factor;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AverageLRMethodTest {
        
    private final static double[] EXPECTED = {
        1.24532988,
        1.01587187,
        1.00893837,
        1.00965880,
        1.00361278,
        1.00334656,
        1.00191166
    };

    private DevelopmentFactors factors;

    public AverageLRMethodTest() {
    }

    @Before
    public void setUp() {
        Triangle source = new InputTriangle(TestData.PAID);
        source = new TriangleCummulation(source);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        AverageLRMethod lrs = new AverageLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED, found, JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        AverageLRMethod lrs = new AverageLRMethod();
        assertEquals(0d, lrs.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}