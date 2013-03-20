package org.jreserve.linkratio;

import org.jreserve.linkratio.MackRegressionLRMethod;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MackRegressionLRMethodTest {
        
    private final static double[] EXPECTED_PAID = {
        1.24827794,
        1.01574956,
        1.009982493,
        1.009424947,
        1.003339383,
        1.003357418,
        1.001911639
    };

    private final static double[] EXPECTED_INCURRED = {
        1.191052,
        0.995914252,
        0.994353693,
        1.009452314,
        1.002482127,
        1.000306664,
        0.987946735
    };
    
    private DevelopmentFactors factors;

    public MackRegressionLRMethodTest() {
    }

    @Test
    public void testGetLinkRatios_Paid() {
        initFactors(TestData.PAID);
        MackRegressionLRMethod lrs = new MackRegressionLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED_PAID.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_PAID[d], lrs.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    private void initFactors(String path) {
        factors = new DevelopmentFactors(TestData.getCummulatedTriangle(path));
    }

    @Test
    public void testGetLinkRatios_Incurred() {
        initFactors(TestData.INCURRED);
        MackRegressionLRMethod lrs = new MackRegressionLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED_INCURRED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_INCURRED[d], lrs.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MackRegressionLRMethod lrs = new MackRegressionLRMethod();
        assertEquals(2d, lrs.getMackAlpha(), JRLibTestUtl.EPSILON);
    }
}