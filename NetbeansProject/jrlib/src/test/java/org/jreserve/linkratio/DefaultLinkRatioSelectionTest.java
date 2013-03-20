package org.jreserve.linkratio;

import org.jreserve.linkratio.UserInputLRMethod;
import org.jreserve.linkratio.AverageLRMethod;
import org.jreserve.linkratio.MinLRMethod;
import org.jreserve.linkratio.MaxLRMethod;
import org.jreserve.linkratio.LinkRatioMethod;
import org.jreserve.linkratio.DefaultLinkRatioSelection;
import org.jreserve.linkratio.WeightedAverageLRMethod;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultLinkRatioSelectionTest {

    private Listener listener;
    private DevelopmentFactors factors;
    private DefaultLinkRatioSelection lr;
    
    public DefaultLinkRatioSelectionTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle triangle = TestData.getCummulatedTriangle(TestData.PAID);
        factors = new DevelopmentFactors(triangle);
        lr = new DefaultLinkRatioSelection(factors);
        
        listener = new Listener();
        lr.addChangeListener(listener);
    }

    @Test
    public void testGetDefaultMethod() {
        assertTrue(lr.getDefaultMethod() instanceof WeightedAverageLRMethod);
    }

    @Test
    public void testSetDefaultMethod() {
        LinkRatioMethod method = new MinLRMethod();
        lr.setDefaultMethod(method);
        assertTrue(lr.getDefaultMethod() instanceof MinLRMethod);
        
        lr.setMethod(null, 0);
        assertTrue(lr.getMethod(0) instanceof MinLRMethod);
    }

    @Test
    public void testSetMethod() {
        LinkRatioMethod method = new MinLRMethod();
        lr.setMethod(method, 1);
        assertEquals(method, lr.getMethod(1));
        assertEquals(1, listener.changeCount);
        
        
        lr.setMethod(null, 1);
        assertTrue(lr.getMethod(1) instanceof WeightedAverageLRMethod);
        assertEquals(2, listener.changeCount);
    }

    @Test
    public void testSetMethods() {
        Map<Integer, LinkRatioMethod> methods = new HashMap<Integer, LinkRatioMethod>();
        methods.put(1, new MinLRMethod());
        methods.put(2, new MaxLRMethod());
        methods.put(3, new AverageLRMethod());
        lr.setMethods(methods);
        assertEquals(1, listener.changeCount);
        
        assertTrue(lr.getMethod(1) instanceof MinLRMethod);
        assertTrue(lr.getMethod(2) instanceof MaxLRMethod);
        assertTrue(lr.getMethod(3) instanceof AverageLRMethod);
    }

    @Test
    public void testGetMethod() {
        LinkRatioMethod defaultMethod = lr.getDefaultMethod();
        
        int devs = factors.getDevelopmentCount();
        for(int d=0; d<devs; d++)
            assertEquals(defaultMethod, lr.getMethod(d));
        assertEquals(defaultMethod, lr.getMethod(devs));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetMethod_MinDev() {
        lr.getMethod(-1);
    }

    @Test
    public void testGetDevelopmentCount() {
        int expected = factors.getDevelopmentCount();
        int found = lr.getDevelopmentCount();
        assertEquals(expected, found);
    }
    
    @Test
    public void testGetDevelopmentCount_Tail() {
        int expected = lr.getDevelopmentCount();
        UserInputLRMethod uiLR = new UserInputLRMethod();
        uiLR.setValue(expected, 1.05);
        lr.setMethod(uiLR, expected);
        assertEquals(expected, lr.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        Map<Integer, LinkRatioMethod> methods = new HashMap<Integer, LinkRatioMethod>();
        methods.put(1, new MinLRMethod());
        methods.put(2, new MaxLRMethod());
        methods.put(3, new AverageLRMethod());
        lr.setMethods(methods);
        double[] expected = {
            1.24694402, 1.00906323, 1.01671665, 1.00965883, 
            1.00347944, 1.00335199, 1.00191164
        };
        assertArrayEquals(expected, lr.toArray(), JRLibTestUtl.EPSILON);
    }
    
    private class Listener implements ChangeListener {
        private int changeCount;
        
        public void stateChanged(ChangeEvent e) {
            changeCount++;
        }
    }
}