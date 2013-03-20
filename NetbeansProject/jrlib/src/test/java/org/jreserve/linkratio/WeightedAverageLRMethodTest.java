package org.jreserve.linkratio;

import org.jreserve.linkratio.WeightedAverageLRMethod;
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
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED_INCURRED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_INCURRED[d], lrs.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        assertEquals(1d, lrs.getMackAlpha(), JRLibTestUtl.EPSILON);
    }
}