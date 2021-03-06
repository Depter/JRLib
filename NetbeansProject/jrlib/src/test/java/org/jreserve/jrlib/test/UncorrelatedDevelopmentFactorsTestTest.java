/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.test;

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.test.UncorrelatedDevelopmentFactorsTest.RankHelper;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UncorrelatedDevelopmentFactorsTestTest {
    
    private UncorrelatedDevelopmentFactorsTest test;
    private ChangeCounter counter;
    
    public UncorrelatedDevelopmentFactorsTestTest() {
    }

    @Before
    public void setUp() {
        FactorTriangle factors = TestData.getDevelopmentFactors(TestData.MACK_DATA3);
        test = new UncorrelatedDevelopmentFactorsTest(factors);
        counter = new ChangeCounter();
        test.addCalculationListener(counter);
    }

    @Test
    public void testTest() {
        assertEquals( 0.50000000, test.getAlpha(), TestConfig.EPSILON);
        assertEquals( 0.06955782, test.getTestValue(), TestConfig.EPSILON);
        assertEquals(-0.12746658, test.getLowerBound(), TestConfig.EPSILON);
        assertEquals( 0.12746658, test.getUpperBound(), TestConfig.EPSILON);
        assertEquals( 0.71282447, test.getPValue(), TestConfig.EPSILON);
        assertTrue(test.isTestPassed());
        
        test.setAlpha(0.75);
        assertEquals( 0.75000000, test.getAlpha(), TestConfig.EPSILON);
        assertEquals(2, counter.getChangeCount());
        assertFalse(test.isTestPassed());
    }

    @Test
    public void testTest_Quarterly() {
        ClaimTriangle claims = TestData.getCummulatedTriangle(TestData.Q_PAID);
        FactorTriangle factors =  new DevelopmentFactors(claims);
        test = new UncorrelatedDevelopmentFactorsTest(factors);
        test.addCalculationListener(counter);
        assertEquals( 0.50000000, test.getAlpha(), TestConfig.EPSILON);
        assertEquals( 0.28288600, test.getTestValue(), TestConfig.EPSILON);
        assertEquals(-0.04654421, test.getLowerBound(), TestConfig.EPSILON);
        assertEquals( 0.04654421, test.getUpperBound(), TestConfig.EPSILON);
        assertEquals( 0.00004142, test.getPValue(), TestConfig.EPSILON);
        assertFalse(test.isTestPassed());
        
        test.setAlpha(0.25);
        assertEquals( 0.25000000, test.getAlpha(), TestConfig.EPSILON);
        assertEquals(2, counter.getChangeCount());
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
        FactorTriangle factors = TestData.getDevelopmentFactors(TestData.MACK_DATA3);
        for(int d=0; d<expectedN.length; d++) {
            RankHelper helper = new RankHelper(d, factors);
            assertEquals(expectedUsed[d], helper.shouldUse());
            assertEquals(expectedN[d], helper.getN());
            assertEquals(expectedT[d], helper.getT(), TestConfig.EPSILON);
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
            assertEquals("At d="+d, expectedT[d], helper.getT(), TestConfig.EPSILON);
        }
    }
}
