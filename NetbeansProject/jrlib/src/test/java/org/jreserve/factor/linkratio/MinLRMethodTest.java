package org.jreserve.factor.linkratio;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.assertEquals;
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
        Triangle triangle = TestData.getCummulatedTriangle(TestData.PAID);
        factors = new DevelopmentFactors(triangle);
    }

    @Test
    public void testGetLinkRatios() {
        MinLRMethod lrs = new MinLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MinLRMethod lrs = new MinLRMethod();
        assertEquals(1d, lrs.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}