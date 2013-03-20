package org.jreserve.test;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.test.UncorrelatedDevelopmentFactorsTest.RankHelper;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.triangle.factor.FixedDevelopmentFactors;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class UncorrelatedDevelopmentFactorsTestTest {

    final static double[][] MACK_FACTORS = {
        {1.6 , 1.32, 1.08, 1.15, 1.2 , 1.11, 1.033, 1.0 , 1.01},
        {40.4, 1.26, 1.98, 1.29, 1.13, 0.99, 1.043, 1.03},
        {2.6 , 1.54, 1.16, 1.16, 1.19, 1.03, 1.026},
        {2   , 1.36, 1.35, 1.1 , 1.11, 1.04},
        {8.8 , 1.66, 1.4 , 1.17, 1.01},
        {4.3 , 1.82, 1.11, 1.23},
        {7.2 , 2.72, 1.12},
        {5.1 , 1.89},
        {1.7}
    };
    
    private UncorrelatedDevelopmentFactorsTest test;
    private ChangeCounter counter;
    
    public UncorrelatedDevelopmentFactorsTestTest() {
    }

    @Before
    public void setUp() {
        FactorTriangle factors = new FixedDevelopmentFactors(MACK_FACTORS);
        test = new UncorrelatedDevelopmentFactorsTest(factors);
        counter = new ChangeCounter();
        test.addChangeListener(counter);
    }

    @Test
    public void testTest() {
        assertEquals( 0.50000000, test.getAlpha(), JRLibTestUtl.EPSILON);
        assertEquals( 0.06955782, test.getTestValue(), JRLibTestUtl.EPSILON);
        assertEquals(-0.12746658, test.getLowerBound(), JRLibTestUtl.EPSILON);
        assertEquals( 0.12746658, test.getUpperBound(), JRLibTestUtl.EPSILON);
        assertEquals( 0.71282447, test.getPValue(), JRLibTestUtl.EPSILON);
        assertTrue(test.isTestPassed());
        
        test.setAlpha(0.75);
        assertEquals( 0.75000000, test.getAlpha(), JRLibTestUtl.EPSILON);
        assertEquals(1, counter.getChangeCount());
        assertFalse(test.isTestPassed());
    }

    @Test
    public void testTest_Quarterly() {
        ClaimTriangle claims = TestData.getCummulatedTriangle(TestData.Q_PAID);
        FactorTriangle factors =  new DevelopmentFactors(claims);
        test = new UncorrelatedDevelopmentFactorsTest(factors);
        test.addChangeListener(counter);
        assertEquals( 0.50000000, test.getAlpha(), JRLibTestUtl.EPSILON);
        assertEquals( 0.28288600, test.getTestValue(), JRLibTestUtl.EPSILON);
        assertEquals(-0.04654421, test.getLowerBound(), JRLibTestUtl.EPSILON);
        assertEquals( 0.04654421, test.getUpperBound(), JRLibTestUtl.EPSILON);
        assertEquals( 0.00004142, test.getPValue(), JRLibTestUtl.EPSILON);
        assertFalse(test.isTestPassed());
        
        test.setAlpha(0.25);
        assertEquals( 0.25000000, test.getAlpha(), JRLibTestUtl.EPSILON);
        assertEquals(1, counter.getChangeCount());
        assertFalse(test.isTestPassed());
    }
    
    @Test
    public void testRankHelper() {
        int[] expectedN = {8, 7, 6, 
                           5, 4, 3, 
                           2, 1, 0};
        double[] expectedT = { 0.19047619, -0.32142857,  0.42857143, 
                              -0.20000000,  0.40000000, -0.50000000, 
                               1.00000000,  Double.NaN, Double.NaN
                            };
        boolean[] expectedUsed = {true, true , true, 
                                  true, true , true, 
                                  true, false, false};
        FactorTriangle factors = new FixedDevelopmentFactors(MACK_FACTORS);
        for(int d=0; d<expectedN.length; d++) {
            RankHelper helper = new RankHelper(d, factors);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedN[d], helper.getN());
            assertEquals(expectedT[d], helper.getT(), JRLibTestUtl.EPSILON);
        }
    }
    
    @Test
    public void testRankHelper_Quarterly() {
        int[] expectedN = {
            11, 11, 11, 10, 10, 10, 10, 9, 9, 9, 
            9 , 8 , 8 , 8 , 8 , 7 , 7 , 7, 7, 6, 
            6 , 6 , 6 , 5 , 5 , 5 , 5 , 4, 4, 4, 
            4 , 3 , 3 , 3 , 3 , 2 , 2 , 2, 2, 1, 
            1, 1, 1, 0
        };
        double[] expectedT = { 
            -0.34090909,  0.31818182, -0.17272727,  0.86666667,  0.50303030,  0.30909091, -0.04242424, 
             0.38333333,  0.75000000,  0.75000000,  0.75000000,  0.71428571, -0.30952381,  0.30952381, 
             0.45238095,  0.53571429,  0.57142857, -0.07142857,  0.71428571,  0.08571429, -0.25714286, 
             0.08571429,  0.88571429,  1.00000000,  0.90000000,  0.90000000, -0.60000000, -0.40000000, 
             0.40000000, -0.40000000, -0.40000000,  0.50000000, -0.50000000, -0.50000000, -1.25000000, 
             0.00000000, -1.00000000,  0.00000000,  1.00000000,  Double.NaN,  Double.NaN,  Double.NaN, 
             Double.NaN, Double.NaN
        };
        boolean[] expectedUsed = {
            true, true, true, true, true, true, true, true, true, true , 
            true, true, true, true, true, true, true, true, true, true , 
            true, true, true, true, true, true, true, true, true, true , 
            true, true, true, true, true, true, true, true, true, false, 
            false, false, false, false
        };
        ClaimTriangle claims = TestData.getCummulatedTriangle(TestData.Q_PAID);
        FactorTriangle factors = new DevelopmentFactors(claims);
        for(int d=0; d<expectedN.length; d++) {
            RankHelper helper = new RankHelper(d, factors);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedN[d], helper.getN());
            assertEquals("At d="+d, expectedT[d], helper.getT(), JRLibTestUtl.EPSILON);
        }
    }
}