package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.smoothing.ExponentialLRFunction;
import org.jreserve.linkratio.smoothing.WeibulLRFunction;
import org.jreserve.linkratio.smoothing.LinkRatioFunction;
import org.jreserve.linkratio.smoothing.PowerLRFunction;
import org.jreserve.linkratio.smoothing.UserInputLRFunction;
import org.jreserve.linkratio.smoothing.DefaultLinkRatioSmoothing;
import org.jreserve.linkratio.smoothing.DefaultLRFunction;
import org.jreserve.linkratio.smoothing.InversePowerLRFunction;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.JRLibTestUtl;
import org.jreserve.linkratio.FixedLinkRatio;
import org.jreserve.linkratio.LinkRatio;
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
    private Listener listener;
    
    public DefaultLinkRatioSmoothingTest() {
    }

    @Before
    public void setUp() {
        lr = FixedLinkRatio.getPaid();
        dlrs = new DefaultLinkRatioSmoothing(lr);
        listener = new Listener();
        dlrs.addChangeListener(listener);
    }

    @Test
    public void testGetDefaultMethod() {
        assertTrue(dlrs.getDefaultMethod() instanceof DefaultLRFunction);
    }

    @Test
    public void testSetDefaultFunction() {
        dlrs.setDefaultMethod(null);
        assertTrue(dlrs.getDefaultMethod() instanceof DefaultLRFunction);
    }

    @Test
    public void testGetDevelopmentCount() {
        int expected = lr.getDevelopmentCount();
        int found = dlrs.getDevelopmentCount();
        assertEquals(expected, found);
        
        expected += 5;
        dlrs.setDevelopmentCount(expected);
        assertEquals(expected, dlrs.getDevelopmentCount());
        assertEquals(1, listener.changeCount);
    }

    @Test
    public void testGetValue() {
        double[] expected = {
            1.10309578, //-> Inverse power 
            1.03672801, //-> Exponential 
            1.01842143, //-> Power
            1.01302400, //-> Weibul
            1.00347944, 1.00335199, 1.00191164, 1.05, 1d
        };
        
        Map<Integer, LinkRatioFunction> functions = new HashMap<Integer, LinkRatioFunction>(4);
        functions.put(0, new InversePowerLRFunction());
        functions.put(1, new ExponentialLRFunction());
        functions.put(2, new PowerLRFunction());
        functions.put(3, new WeibulLRFunction());
        functions.put(7, createTailAt7());
        dlrs.setMethods(functions);
        dlrs.setDevelopmentCount(expected.length);
        
        assertArrayEquals(expected, dlrs.toArray(), JRLibTestUtl.EPSILON);
    }
    
    private UserInputLRFunction createTailAt7() {
        UserInputLRFunction f = new UserInputLRFunction();
        f.setValue(7, 1.05);
        return f;
    }
    
    private class Listener implements ChangeListener {
        private int changeCount;
        
        public void stateChanged(ChangeEvent e) {
            changeCount++;
        }
    }

}