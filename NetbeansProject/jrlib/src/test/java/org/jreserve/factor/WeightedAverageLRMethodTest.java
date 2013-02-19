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
public class WeightedAverageLRMethodTest {
        
    private final static double[] EXPECTED_PAID = {
        1.24694402,
        1.01584722,
        1.00946012,
        1.00954295,
        1.00347944,
        1.00335199,
        1.00191164
    };

    private final static double[] EXPECTED_INCURRED = {
        1.19471971,
        0.99540619,
        0.99507566,
        1.01018160,
        1.00310913,
        1.00031727,
        0.98794674
    };
    
    private DevelopmentFactors factors;

    public WeightedAverageLRMethodTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetLinkRatios_Paid() {
        initFactors(TestData.PAID);
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED_PAID, found, JRLibTestSuite.EPSILON);
    }
    
    private void initFactors(double[][] data) {
        Triangle source = new InputTriangle(data);
        source = new TriangleCummulation(source);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios_Incurred() {
        initFactors(TestData.INCURRED);
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED_INCURRED, found, JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        assertEquals(1d, lrs.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}