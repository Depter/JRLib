package org.jreserve.factor;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MinLRMethodTest {
        
    private final static double[] EXPECTED = {
            1.21380194,
            1.00906327,
            1.00240382,
            1.00094905,
            1.00172331,
            1.00010273,
            1.00191166
        };

    private DevelopmentFactors factors;
    
    public MinLRMethodTest() {
    }

    @Before
    public void setUp() {
        Triangle source = new InputTriangle(TestData.PAID);
        source = new TriangleCummulation(source);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        MinLRMethod lrs = new MinLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED, found, JRLibTestSuite.EPSILON);
    }
}