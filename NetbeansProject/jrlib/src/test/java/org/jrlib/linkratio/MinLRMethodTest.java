package org.jrlib.linkratio;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
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

    private FactorTriangle factors;
    
    public MinLRMethodTest() {
    }

    @Before
    public void setUp() {
        factors = TestData.getDevelopmentFactors(TestData.PAID);
    }

    @Test
    public void testGetLinkRatios() {
        MinLRMethod lrs = new MinLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MinLRMethod lrs = new MinLRMethod();
        assertEquals(AbstractLRMethod.DEFAULT_MACK_ALPHA, lrs.getMackAlpha(), TestConfig.EPSILON);
    }
}
