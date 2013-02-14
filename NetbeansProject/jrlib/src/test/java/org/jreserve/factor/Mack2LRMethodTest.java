package org.jreserve.factor;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class Mack2LRMethodTest {
        
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

    public Mack2LRMethodTest() {
    }

    @Test
    public void testGetLinkRatios_Paid() {
        initFactors(TestData.PAID);
        Mack2LRMethod lrs = new Mack2LRMethod();
        double[] found = lrs.getLinkRatios(factors, WeightTriangle.getDefault());
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
        Mack2LRMethod lrs = new Mack2LRMethod();
        double[] found = lrs.getLinkRatios(factors, WeightTriangle.getDefault());
        assertArrayEquals(EXPECTED_INCURRED, found, JRLibTestSuite.EPSILON);
    }
}