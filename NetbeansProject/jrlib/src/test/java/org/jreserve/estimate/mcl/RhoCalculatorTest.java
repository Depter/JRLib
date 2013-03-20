package org.jreserve.estimate.mcl;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class RhoCalculatorTest {

    private final static double[] RATIO_IP = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997
    };
    
    private final static double[] RHO_IP = {
        216.92324278, 147.27868035, 148.51503147, 90.84835978, 
         93.12294180,  98.75714113, 149.90267924, Double.NaN
    };
    
    private final static double[] RATIO_PI = {
        0.80765895, 0.85381734, 0.88205743, 0.90907340, 
        0.91421262, 0.91845318, 0.91987046, 0.97277214
    };
    
    private final static double[] RHO_PI = {
        158.78064398, 118.64087385, 125.68259517, 80.44088323, 
         82.09314392,  86.79189307, 132.33680485, Double.NaN
    };
    
    private static ClaimTriangle paid;
    private static ClaimTriangle incurred;
    private MclRhoCalculator calculator;
    
    public RhoCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        paid = TestData.getCummulatedTriangle(TestData.PAID);
        incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        paid = null;
        incurred = null;
    }

    @Test
    public void testIperP() {
        calculator = new MclRhoCalculator(incurred, paid);
        int devs = RATIO_IP.length;
        assertEquals(devs, calculator.getDevelopmentCount());
        
        assertEquals(Double.NaN, calculator.getRatio(-1), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, calculator.getRho(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<devs; d++) {
            assertEquals(RATIO_IP[d], calculator.getRatio(d), JRLibTestUtl.EPSILON);
            assertEquals(RHO_IP[d], calculator.getRho(d), JRLibTestUtl.EPSILON);
        }
        assertEquals(Double.NaN, calculator.getRatio(devs), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, calculator.getRho(devs), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testPperI() {
        calculator = new MclRhoCalculator(paid, incurred);
        int devs = RATIO_PI.length;
        assertEquals(devs, calculator.getDevelopmentCount());
        
        assertEquals(Double.NaN, calculator.getRatio(-1), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, calculator.getRho(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<devs; d++) {
            assertEquals(RATIO_PI[d], calculator.getRatio(d), JRLibTestUtl.EPSILON);
            assertEquals(RHO_PI[d], calculator.getRho(d), JRLibTestUtl.EPSILON);
        }
        assertEquals(Double.NaN, calculator.getRatio(devs), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, calculator.getRho(devs), JRLibTestUtl.EPSILON);
    }

}