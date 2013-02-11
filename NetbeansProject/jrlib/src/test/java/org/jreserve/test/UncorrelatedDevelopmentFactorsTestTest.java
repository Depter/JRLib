package org.jreserve.test;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.test.UncorrelatedDevelopmentFactorsTest.RankHelper;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class UncorrelatedDevelopmentFactorsTestTest {

    private final static double[][] MACK_FACTORS = {
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
        Triangle triangle = new InputTriangle(MACK_FACTORS);
        test = new UncorrelatedDevelopmentFactorsTest(triangle);
        counter = new ChangeCounter();
        test.addChangeListener(counter);
    }

    @Test
    public void testTest() {
        assertEquals( 0.50000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals( 0.06955782, test.getTestValue(), JRLibTestSuite.EPSILON);
        assertEquals(-0.12746658, test.getLowerBound(), JRLibTestSuite.EPSILON);
        assertEquals( 0.12746658, test.getUpperBound(), JRLibTestSuite.EPSILON);
        assertEquals( 0.28717553, test.getPValue(), JRLibTestSuite.EPSILON);
        assertTrue(test.isTestPassed());
        
        test.setAlpha(0.25);
        assertEquals( 0.25000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals(1, counter.getChangeCount());
        assertFalse(test.isTestPassed());
    }
//
//    @Test
//    public void testGetTestValue() {
//        System.out.println("getTestValue");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        double expResult = 0.0;
//        double result = instance.getTestValue();
//        assertEquals(expResult, result, 0.0);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetLowerBound() {
//        System.out.println("getLowerBound");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        double expResult = 0.0;
//        double result = instance.getLowerBound();
//        assertEquals(expResult, result, 0.0);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetUpperBound() {
//        System.out.println("getUpperBound");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        double expResult = 0.0;
//        double result = instance.getUpperBound();
//        assertEquals(expResult, result, 0.0);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetAlpha() {
//        System.out.println("getAlpha");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        double expResult = 0.0;
//        double result = instance.getAlpha();
//        assertEquals(expResult, result, 0.0);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPValue() {
//        System.out.println("getPValue");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        double expResult = 0.0;
//        double result = instance.getPValue();
//        assertEquals(expResult, result, 0.0);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testRecalculateLayer() {
//        System.out.println("recalculateLayer");
//        UncorrelatedDevelopmentFactorsTest instance = null;
//        instance.recalculateLayer();
//        fail("The test case is a prototype.");
//    }
    
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
        Triangle triangle = new InputTriangle(MACK_FACTORS);
        for(int d=0; d<expectedN.length; d++) {
            RankHelper helper = new RankHelper(d, triangle);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedN[d], helper.getN());
            assertEquals(expectedT[d], helper.getT(), JRLibTestSuite.EPSILON);
        }
    }
}