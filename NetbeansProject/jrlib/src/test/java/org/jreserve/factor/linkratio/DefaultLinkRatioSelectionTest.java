package org.jreserve.factor.linkratio;

import org.jreserve.factor.linkratio.WeightedAverageLRMethod;
import org.jreserve.factor.linkratio.LinkRatioMethod;
import org.jreserve.factor.linkratio.DefaultLinkRatioSelection;
import org.jreserve.factor.linkratio.MinLRMethod;
import org.jreserve.factor.linkratio.AverageLRMethod;
import org.jreserve.factor.linkratio.MaxLRMethod;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;
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
        Triangle triangle = new InputTriangle(TestData.PAID);
        triangle = new TriangleCummulation(triangle);
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
        assertArrayEquals(expected, lr.toArray(), JRLibTestSuite.EPSILON);
    }
    
    private class Listener implements ChangeListener {
        private int changeCount;
        
        public void stateChanged(ChangeEvent e) {
            changeCount++;
        }
    }
}