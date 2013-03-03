package org.jreserve.test;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.test.CalendarEffectTest.DiagonalHelper;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class CalendarEffectTestTest {
    private final static int S = 0;
    private final static int L = 1;
    private final static int STAR = 2;

    public CalendarEffectTestTest() {
    }

    @Test
    public void testTest() {
        Triangle triangle = new InputTriangle(UncorrelatedDevelopmentFactorsTestTest.MACK_FACTORS);
        CalendarEffectTest test = new CalendarEffectTest(triangle);
        ChangeCounter counter = new ChangeCounter();
        test.addChangeListener(counter);
        
        assertEquals( 0.05000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals(14.0000000, test.getTestValue(), JRLibTestSuite.EPSILON);
        assertEquals( 8.96561335, test.getLowerBound(), JRLibTestSuite.EPSILON);
        assertEquals(16.78438665, test.getUpperBound(), JRLibTestSuite.EPSILON);
        assertEquals( 0.57274274, test.getPValue(), JRLibTestSuite.EPSILON);
        assertTrue(test.isTestPassed());
        
        test.setAlpha(0.75);
        assertEquals( 0.75000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals(1, counter.getChangeCount());
        assertFalse(test.isTestPassed());
    }

    @Test
    public void testTest_Quarterly() {
        Triangle triangle = new DevelopmentFactors(TestData.getCummulatedTriangle(TestData.Q_PAID));
        CalendarEffectTest test = new CalendarEffectTest(triangle);
        ChangeCounter counter = new ChangeCounter();
        test.addChangeListener(counter);
        
        assertEquals( 0.05000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals(75.00000000, test.getTestValue(), JRLibTestSuite.EPSILON);
        assertEquals(69.24185858, test.getLowerBound(), JRLibTestSuite.EPSILON);
        assertEquals(87.32845392, test.getUpperBound(), JRLibTestSuite.EPSILON);
        assertEquals(0.47646752, test.getPValue(), JRLibTestSuite.EPSILON);
        assertTrue(test.isTestPassed());
        
        test.setAlpha(0.25);
        assertEquals( 0.25000000, test.getAlpha(), JRLibTestSuite.EPSILON);
        assertEquals(1, counter.getChangeCount());
        assertTrue(test.isTestPassed());
    }
    
    @Test
    public void testDiagonalHelper() {
        int[][] sl = {
            {S, S, S, S, L, L, STAR, S, STAR},
            {L, S, L, L, STAR, S, L, L},
            {S, S, STAR, S, L, S, S},
            {S, S, L, S, S, L},
            {L, L, L, L, S},
            {STAR, L, S, L},
            {L, L, S},
            {L, L},
            {S}
        };
        double[] expectedEZ = { 
            Double.NaN, 0.5, 0.75, 1.25, 1.25, 
            1.25, 2.0625, 2.90625, 2.90625
        };
        double[] expectedVarZ = {
            Double.NaN, 0.25, 0.1875, 0.4375, 0.4375, 
            0.4375, 0.62109375, 0.803710938, 0.803710938
        };
        boolean[] expectedUsed = {
            false, true, true, true, true , 
            true,  true, true, true};
        Triangle triangle = new InputTriangle(UncorrelatedDevelopmentFactorsTestTest.MACK_FACTORS);
        for(int d=0; d<expectedEZ.length; d++) {
            DiagonalHelper helper = new DiagonalHelper(sl, d, triangle);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedEZ[d], helper.getEZ(), JRLibTestSuite.EPSILON);
            assertEquals(expectedVarZ[d], helper.getVarZ(), JRLibTestSuite.EPSILON);
        }
    }
    
    @Test
    public void testDiagonalHelper_Quarterly() {
        int[][] sl = {
            {S, S, S, S, S, S, S, L, S, S, S, S, L, S, S, S, S, S, STAR, S, S, L, S, S, S, S, S, STAR, L, S, S, S, L, S, STAR, STAR, L, S, STAR, STAR, STAR, STAR, STAR, STAR},
            {L, S, S, S, S, L, L, S, S, S, S, S, S, L, L, L, S, S, S, S, S, S, S, S, S, S, S, L, S, S, L, S, S, STAR, L, STAR, S, L, STAR, STAR},
            {L, S, L, S, S, S, S, S, S, S, S, S, S, S, S, S, S, STAR, S, S, L, S, S, S, STAR, L, L, S, S, L, L, L, STAR, L, S, L},
            {L, L, S, L, L, L, L, S, L, STAR, STAR, STAR, S, L, S, L, L, L, L, L, L, S, L, L, L, L, L, S, L, L, S, L},
            {L, S, S, L, L, L, S, L, L, L, L, L, S, L, L, L, STAR, S, L, L, S, L, L, L, L, STAR, STAR, L},
            {S, L, L, STAR, S, L, S, L, L, L, L, L, L, L, S, S, L, L, L, L, L, L, L, L},
            {S, L, L, L, L, S, S, S, L, L, L, L, L, S, L, S, L, L, S, STAR},
            {L, L, S, L, L, L, L, L, STAR, L, L, L, L, S, L, L},
            {STAR, STAR, L, L, L, S, L, S, S, S, S, S},
            {S, S, L, S, S, S, L, L},
            {S, L, STAR, S}
        };
        double[] expectedEZ = {
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, 
            0.5, 0.5, 0.5, 0.5, 0.75, 0.75, 0.75, 0.75, 1.25, 1.25, 
            1.25, 1.25, 1.5625, 1.5625, 1.25, 1.5625, 2.0625, 1.5625, 
            1.5625, 1.25, 2.40625, 2.0625, 2.40625, 2.0625, 2.90625, 
            2.90625, 2.90625, 2.90625, 2.0625, 2.90625, 2.90625, 2.90625, 
            3.26953125, 3.26953125, 3.26953125, 2.90625, 3.26953125, 
            3.26953125, 2.40625, 2.90625
        };
        double[] expectedVarZ = {
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, 
            0.25, 0.25, 0.25, 0.25, 0.1875, 0.1875, 0.1875, 
            0.1875, 0.4375, 0.4375, 0.4375, 0.4375, 0.37109375, 
            0.37109375, 0.4375, 0.37109375, 0.62109375, 0.37109375, 
            0.37109375, 0.4375, 0.553710938, 0.62109375, 0.553710938, 
            0.62109375, 0.803710938, 0.803710938, 0.803710938, 
            0.803710938, 0.62109375, 0.803710938, 0.803710938, 
            0.803710938, 0.735946655, 0.735946655, 0.735946655, 
            0.803710938, 0.735946655, 0.735946655, 0.553710938, 
            0.803710938
        };
        boolean[] expectedUsed = {
            false, false, false, false, true, true, true, true, 
            true, true, true, true, true, true, true, true, true, 
            true, true, true, true, true, true, true, true, true, 
            true, true, true, true, true, true, true, true, true, 
            true, true, true, true, true, true, true, true, true
        };
        Triangle triangle = new DevelopmentFactors(TestData.getCummulatedTriangle(TestData.Q_PAID));
        for(int d=0; d<expectedEZ.length; d++) {
            DiagonalHelper helper = new DiagonalHelper(sl, d, triangle);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedEZ[d], helper.getEZ(), JRLibTestSuite.EPSILON);
            assertEquals(expectedVarZ[d], helper.getVarZ(), JRLibTestSuite.EPSILON);
        }
    }
}