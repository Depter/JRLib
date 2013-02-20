package org.jreserve.factor.scale;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class LinkRatioScaleMinMaxEstimateTest {

    public LinkRatioScaleMinMaxEstimateTest() {
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
    public void testFit() {
        System.out.println("fit");
        LinkRatioScale scales = null;
        LinkRatioScaleMinMaxEstimate instance = new LinkRatioScaleMinMaxEstimate();
        instance.fit(scales);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        int development = 0;
        LinkRatioScaleMinMaxEstimate instance = new LinkRatioScaleMinMaxEstimate();
        double expResult = 0.0;
        double result = instance.getValue(development);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        LinkRatioScaleMinMaxEstimate instance = new LinkRatioScaleMinMaxEstimate();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        LinkRatioScaleMinMaxEstimate instance = new LinkRatioScaleMinMaxEstimate();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        LinkRatioScaleMinMaxEstimate instance = new LinkRatioScaleMinMaxEstimate();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}