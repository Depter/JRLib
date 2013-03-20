package org.jreserve.linkratio;

import org.jreserve.linkratio.AbstractLRMethod;
import org.jreserve.linkratio.MaxLRMethod;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
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
        factors = new DevelopmentFactors(TestData.getCummulatedTriangle(TestData.PAID));
    }

    @Test
    public void testGetLinkRatios() {
        MaxLRMethod lrs = new MaxLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MaxLRMethod lrs = new MaxLRMethod();
        assertEquals(AbstractLRMethod.DEFAULT_MACK_ALPHA, lrs.getMackAlpha(), JRLibTestUtl.EPSILON);
    }
}