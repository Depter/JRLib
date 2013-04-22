package org.jreserve.jrlib.linkratio;

import org.jreserve.jrlib.linkratio.UserInputLRMethod;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRMethodTest {

    @Test
    public void testGetLinkRatios() {
        UserInputLRMethod ui = new UserInputLRMethod();
        int userValueIndex = 2;
        double userValue = 5d;
        ui.setValue(userValueIndex, userValue);
        
        FactorTriangle factors = TestData.getDevelopmentFactors(TestData.PAID);
        ui.fit(factors);
        
        for(int d=0; d<(userValueIndex+2); d++) {
            double expected = (d==userValueIndex)? userValue : Double.NaN;
            assertEquals(expected, ui.getValue(d), TestConfig.EPSILON);
        }
    }

    @Test
    public void testGetMackAlpha() {
        UserInputLRMethod ui = new UserInputLRMethod();
        assertEquals(UserInputLRMethod.MACK_ALPHA, ui.getMackAlpha(), TestConfig.EPSILON);
    }

}
