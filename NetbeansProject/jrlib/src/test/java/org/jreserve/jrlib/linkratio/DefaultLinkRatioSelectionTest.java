/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.linkratio;

import java.util.HashMap;
import java.util.Map;
import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSelectionTest {

    private ChangeCounter listener;
    private FactorTriangle factors;
    private DefaultLinkRatioSelection lr;
    
    public DefaultLinkRatioSelectionTest() {
    }

    @Before
    public void setUp() {
        factors = TestData.getDevelopmentFactors(TestData.PAID);
        lr = new DefaultLinkRatioSelection(factors);
        
        listener = new ChangeCounter();
        lr.addCalculationListener(listener);
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
        assertEquals(2, listener.getChangeCount());
        
        
        lr.setMethod(null, 1);
        assertTrue(lr.getMethod(1) instanceof WeightedAverageLRMethod);
        assertEquals(4, listener.getChangeCount());
    }

    @Test
    public void testSetMethods() {
        Map<Integer, LinkRatioMethod> methods = new HashMap<Integer, LinkRatioMethod>();
        methods.put(1, new MinLRMethod());
        methods.put(2, new MaxLRMethod());
        methods.put(3, new AverageLRMethod());
        lr.setMethods(methods);
        assertEquals(2, listener.getChangeCount());
        
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
        int found = lr.getLength();
        assertEquals(expected, found);
    }
    
    @Test
    public void testGetDevelopmentCount_Tail() {
        int expected = lr.getLength();
        UserInputLRMethod uiLR = new UserInputLRMethod();
        uiLR.setValue(expected, 1.05);
        lr.setMethod(uiLR, expected);
        assertEquals(expected, lr.getLength());
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
        assertArrayEquals(expected, lr.toArray(), TestConfig.EPSILON);
    }
}
