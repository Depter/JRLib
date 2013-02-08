package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.DefaultLinkRatioSelection;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.LinkRatio;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class InversePowerTest {

    private LinkRatio lr;
    
    public InversePowerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Triangle triangle = new InputTriangle(JRLibTestSuite.PAID);
        triangle = new TriangleCummulation(triangle);
        lr = new DefaultLinkRatioSelection(new DevelopmentFactors(triangle));
    }

    @Test
    public void testFit() {
        InversePower ip = new InversePower();
        ip.fit(lr);
        System.out.printf("a=%.14f; b=%.14f", ip.getA(), ip.getB());
        assertEquals( 0.244820700455068, ip.getA(), JRLibTestSuite.EPSILON);
        assertEquals(-3.58270962630236, ip.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetB() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValue() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testValue() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testGradient() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testEquals() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testHashCode() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        fail("The test case is a prototype.");
    }

}