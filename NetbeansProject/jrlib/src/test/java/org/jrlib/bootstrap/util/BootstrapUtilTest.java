package org.jrlib.bootstrap.util;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class BootstrapUtilTest {

    public BootstrapUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetTotalReserves() {
        System.out.println("getTotalReserves");
        double[][] reserves = null;
        double[] expResult = null;
        double[] result = BootstrapUtil.getTotalReserves(reserves);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetReserves() {
        System.out.println("getReserves");
        double[][] reserves = null;
        int accident = 0;
        double[] expResult = null;
        double[] result = BootstrapUtil.getReserves(reserves, accident);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMeanTotalReserve() {
        System.out.println("getMeanTotalReserve");
        double[][] reserves = null;
        double expResult = 0.0;
        double result = BootstrapUtil.getMeanTotalReserve(reserves);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMeans() {
        System.out.println("getMeans");
        double[][] reserves = null;
        double[] expResult = null;
        double[] result = BootstrapUtil.getMeans(reserves);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMeanReserve() {
        System.out.println("getMeanReserve");
        double[][] reserves = null;
        int accident = 0;
        double expResult = 0.0;
        double result = BootstrapUtil.getMeanReserve(reserves, accident);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }

    @Test
    public void testShiftAdjustment_doubleArr_double() {
        System.out.println("shiftAdjustment");
        double[] reserves = null;
        double mean = 0.0;
        BootstrapUtil.shiftAdjustment(reserves, mean);
        fail("The test case is a prototype.");
    }

    @Test
    public void testShiftAdjustment_doubleArrArr_doubleArr() {
        System.out.println("shiftAdjustment");
        double[][] reserves = null;
        double[] means = null;
        BootstrapUtil.shiftAdjustment(reserves, means);
        fail("The test case is a prototype.");
    }

    @Test
    public void testScaleAdjustment_doubleArr_double() {
        System.out.println("scaleAdjustment");
        double[] reserves = null;
        double mean = 0.0;
        BootstrapUtil.scaleAdjustment(reserves, mean);
        fail("The test case is a prototype.");
    }

    @Test
    public void testScaleAdjustment_doubleArrArr_doubleArr() {
        System.out.println("scaleAdjustment");
        double[][] reserves = null;
        double[] means = null;
        BootstrapUtil.scaleAdjustment(reserves, means);
        fail("The test case is a prototype.");
    }

    @Test
    public void testPercentile() {
        System.out.println("percentile");
        double[] reserves = null;
        double percentile = 0.0;
        BootstrapUtil instance = null;
        double expResult = 0.0;
        double result = instance.percentile(reserves, percentile);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }

}