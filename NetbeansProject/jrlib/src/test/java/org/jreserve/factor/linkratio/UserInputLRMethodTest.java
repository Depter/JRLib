package org.jreserve.factor.linkratio;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.triangle.Triangle;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        
        Triangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        DevelopmentFactors factors = new DevelopmentFactors(cik);
        ui.fit(factors);
        
        for(int d=0; d<(userValueIndex+2); d++) {
            double expected = (d==userValueIndex)? userValue : Double.NaN;
            assertEquals(expected, ui.getValue(d), JRLibTestSuite.EPSILON);
        }
    }

    @Test
    public void testGetMackAlpha() {
        UserInputLRMethod ui = new UserInputLRMethod();
        assertEquals(UserInputLRMethod.MACK_ALPHA, ui.getMackAlpha(), JRLibTestSuite.EPSILON);
    }
}