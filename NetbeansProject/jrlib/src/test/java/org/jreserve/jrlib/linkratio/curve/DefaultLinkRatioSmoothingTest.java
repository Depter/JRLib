package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.curve.InversePowerLRCurve;
import org.jreserve.jrlib.linkratio.curve.DefaultLRCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioCurve;
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve;
import org.jreserve.jrlib.linkratio.curve.WeibulLRCurve;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.curve.PowerLRCurve;
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSmoothingTest {

    private LinkRatio lr;
    private DefaultLinkRatioSmoothing dlrs;
    private ChangeCounter listener;
    
    public DefaultLinkRatioSmoothingTest() {
    }

    @Before
    public void setUp() {
        lr = TestData.getLinkRatio(TestData.PAID);
        dlrs = new DefaultLinkRatioSmoothing(lr);
        listener = new ChangeCounter();
        dlrs.addChangeListener(listener);
    }

    @Test
    public void testGetDefaultMethod() {
        assertTrue(dlrs.getDefaultMethod() instanceof DefaultLRCurve);
    }

    @Test(expected=NullPointerException.class)
    public void testSetDefaultFunction_Null() {
        dlrs.setDefaultMethod(null);
    }

    @Test
    public void testGetDevelopmentCount() {
        int expected = lr.getLength();
        int found = dlrs.getLength();
        assertEquals(expected, found);
        
        expected += 5;
        dlrs.setDevelopmentCount(expected);
        assertEquals(expected, dlrs.getLength());
        assertEquals(1, listener.getChangeCount());
    }

    @Test
    public void testGetValue() {
        double[] expected = {
            1.10309578, //-> Inverse power 
            1.03672801, //-> Exponential 
            1.01842143, //-> Power
            1.01302400, //-> Weibul
            1.00347944, 1.00335199, 1.00191164, 1.05, Double.NaN
        };
        
        Map<Integer, LinkRatioCurve> functions = new HashMap<Integer, LinkRatioCurve>(4);
        functions.put(0, new InversePowerLRCurve());
        functions.put(1, new ExponentialLRCurve());
        functions.put(2, new PowerLRCurve());
        functions.put(3, new WeibulLRCurve());
        functions.put(7, createTailAt7());
        dlrs.setMethods(functions);
        dlrs.setDevelopmentCount(expected.length);
        
        assertArrayEquals(expected, dlrs.toArray(), TestConfig.EPSILON);
    }
    
    private UserInputLRCurve createTailAt7() {
        UserInputLRCurve f = new UserInputLRCurve();
        f.setValue(7, 1.05);
        return f;
    }
}
