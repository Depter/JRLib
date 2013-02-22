package org.jreserve.factor.linkratio;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class UserInputLRMethodTest {

    public UserInputLRMethodTest() {
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
    public void testGetLinkRatios() {
        UserInputLRMethod ui = new UserInputLRMethod();
        int userValueIndex = 2;
        double userValue = 5d;
        ui.setValue(userValueIndex, userValue);
        
        Triangle cik = TriangleFactory.create(TestData.PAID).cummulate().build();
        DevelopmentFactors factors = new DevelopmentFactors(cik);
        double[] found = ui.getLinkRatios(factors);
        assertEquals(found.length, factors.getDevelopmentCount());
        
        for(int d=0; d<found.length; d++) {
            double expected = (d==userValueIndex)? userValue : Double.NaN;
            assertEquals(expected, found[d], JRLibTestSuite.EPSILON);
        }
    }

    @Test
    public void testGetMackAlpha() {
        UserInputLRMethod ui = new UserInputLRMethod();
        assertEquals(UserInputLRMethod.MACK_ALPHA, ui.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}