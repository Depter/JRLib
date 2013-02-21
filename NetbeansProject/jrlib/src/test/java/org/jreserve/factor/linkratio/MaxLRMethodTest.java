package org.jreserve.factor.linkratio;

import org.jreserve.factor.linkratio.MaxLRMethod;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
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
public class MaxLRMethodTest {
        
    private final static double[] EXPECTED = {
        1.32749558,
        1.02595957,
        1.01671671,
        1.01683530,
        1.00480590,
        1.00659039,
        1.00191166
    };

    private DevelopmentFactors factors;

    public MaxLRMethodTest() {
    }

    @Before
    public void setUp() {
        Triangle source = new InputTriangle(TestData.PAID);
        source = new TriangleCummulation(source);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        MaxLRMethod lrs = new MaxLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED, found, JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MaxLRMethod lrs = new MaxLRMethod();
        assertEquals(1d, lrs.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}