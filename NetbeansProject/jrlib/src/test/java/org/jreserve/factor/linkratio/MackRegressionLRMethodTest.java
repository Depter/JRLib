package org.jreserve.factor.linkratio;

import org.jreserve.factor.linkratio.MackRegressionLRMethod;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
import static org.junit.Assert.assertArrayEquals;
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
        MackRegressionLRMethod lrs = new MackRegressionLRMethod();
        double[] found = lrs.getLinkRatios(factors);
        assertArrayEquals(EXPECTED_INCURRED, found, JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MackRegressionLRMethod lrs = new MackRegressionLRMethod();
        assertEquals(2d, lrs.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}