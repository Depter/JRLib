package org.jreserve.factor;

import org.jreserve.JRLibTestSuite;
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
public class WeightedAverageLRMethodTest {
        
    private final static double[] EXPECTED = {
        1.24694402,
        1.01584722,
        1.00946012,
        1.00954295,
        1.00347944,
        1.00335199,
        1.00191164
    };

    private DevelopmentFactors factors;

    public WeightedAverageLRMethodTest() {
    }

    @Before
    public void setUp() {
        Triangle source = new InputTriangle(JRLibTestSuite.PAID);
        source = new TriangleCummulation(source);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED, found, JRLibTestSuite.EPSILON);
    }
}