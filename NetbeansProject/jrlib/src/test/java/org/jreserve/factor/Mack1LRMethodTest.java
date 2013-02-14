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
public class Mack1LRMethodTest {
        
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
        1.19105200, 
        0.99591425, 
        0.99435369, 
        1.00945231, 
        1.00248213, 
        1.00030666, 
        0.98794674
    };
    
    private DevelopmentFactors factors;

    public Mack1LRMethodTest() {
    }

    @Test
    public void testGetLinkRatios_Paid() {
        initFactors(TestData.PAID);
        Mack1LRMethod lrs = new Mack1LRMethod();
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
        Mack1LRMethod lrs = new Mack1LRMethod();
        double[] found = lrs.getLinkRatios(factors, WeightTriangle.getDefault());
        assertArrayEquals(EXPECTED_INCURRED, found, JRLibTestSuite.EPSILON);
    }
}