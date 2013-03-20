package org.jreserve.linkratio;

import org.jreserve.linkratio.AverageLRMethod;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.claim.ClaimTriangle;
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
        ClaimTriangle source = TestData.getCummulatedTriangle(TestData.PAID);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        AverageLRMethod lrs = new AverageLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        AverageLRMethod lrs = new AverageLRMethod();
        assertEquals(0d, lrs.getMackAlpha(), JRLibTestUtl.EPSILON);
    }
}