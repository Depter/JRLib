package org.jreserve.factor.curve;

import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.LinkRatio;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

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
    public void testGetDefaultFunction() {
        assertTrue(dlrs.getDefaultFunction() instanceof DefaultLRFunction);
    }

    @Test
    public void testSetDefaultFunction() {
        LinkRatioFunction f = new WeibulLRFunction();
        dlrs.setDefaultFunction(f);
        assertEquals(f, dlrs.getDefaultFunction());
        
        dlrs.setDefaultFunction(null);
        assertTrue(dlrs.getDefaultFunction() instanceof DefaultLRFunction);
        
        assertEquals(0, listener.changeCount);
    }

    @Test
    public void testSetFunction() {
        LinkRatioFunction function = new WeibulLRFunction();
        dlrs.setFunction(function, 1);
        assertEquals(function, dlrs.getFunction(1));
        assertEquals(1, listener.changeCount);
        
        
        dlrs.setFunction(null, 1);
        assertTrue(dlrs.getFunction(1) instanceof DefaultLRFunction);
        assertEquals(2, listener.changeCount);
    }

    @Test
    public void testSetFunctions() {
        Map<Integer, LinkRatioFunction> functions = new HashMap<Integer, LinkRatioFunction>();
        functions.put(0, new WeibulLRFunction());
        functions.put(1, new ExponentialLRFunction());
        functions.put(2, new PowerLRFunction());
        dlrs.setFunctions(functions);
        assertEquals(1, listener.changeCount);
        
        assertTrue(dlrs.getFunction(0) instanceof WeibulLRFunction);
        assertTrue(dlrs.getFunction(1) instanceof ExponentialLRFunction);
        assertTrue(dlrs.getFunction(2) instanceof PowerLRFunction);
    }

    @Test
    public void testGetFunction() {
        LinkRatioFunction defaultFunction = dlrs.getDefaultFunction();
        
        int devs = lr.getDevelopmentCount();
        for(int d=0; d<devs; d++)
            assertEquals(defaultFunction, dlrs.getFunction(d));
        assertEquals(defaultFunction, dlrs.getFunction(devs));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetFunction_MinDev() {
        dlrs.getFunction(-1);
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
            1.15379669, //-> Inverse power 
            1.03672801, //-> Exponential 
            1.01842143, //-> Power
            1.01302400, //-> Weibul
            1.00347944, 1.00335199, 1.00191164, 1d, 1d
        };
        
        Map<Integer, LinkRatioFunction> functions = new HashMap<Integer, LinkRatioFunction>(4);
        functions.put(0, new InversePowerLRFunction());
        functions.put(1, new ExponentialLRFunction());
        functions.put(2, new PowerLRFunction());
        functions.put(3, new WeibulLRFunction());
        dlrs.setFunctions(functions);
        dlrs.setDevelopmentCount(expected.length);
        
        assertArrayEquals(expected, dlrs.toArray(), JRLibTestSuite.EPSILON);
    }
    
    private class Listener implements ChangeListener {
        private int changeCount;
        
        public void stateChanged(ChangeEvent e) {
            changeCount++;
        }
    }

}