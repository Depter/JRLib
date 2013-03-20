package org.jreserve.util;

import java.util.HashMap;
import java.util.Map;
import org.jreserve.CalculationData;
import org.jreserve.ChangeCounter;
import org.jreserve.Copyable;
import org.jreserve.JRLibTestUtl;
import static org.jreserve.TestData.EXPOSURE;
import static org.jreserve.TestData.getCachedVector;
import org.jreserve.vector.InputVector;
import org.jreserve.vector.Vector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractMethodSelectionTest {

    private final static Method DEFAULT = new Method(Double.NaN);
    private AbstractOutputSelectionImpl selection;
    private ChangeCounter counter;
    
    public AbstractMethodSelectionTest() {
    }

    @Before
    public void setUp() {
        InputVector source = new InputVector(getCachedVector(EXPOSURE));
        selection = new AbstractOutputSelectionImpl(source, DEFAULT);
        counter = new ChangeCounter();
        selection.addChangeListener(counter);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_NullMethod() {
        InputVector source = new InputVector(getCachedVector(EXPOSURE));
        selection = new AbstractOutputSelectionImpl(source, null);
    }
    
    @Test
    public void testGetDefaultMethod() {
        assertEquals(DEFAULT, selection.getDefaultMethod());
    }

    @Test
    public void testSetDefaultMethod() {
        Method method = new Method(1d);
        selection.setDefaultMethod(method);
        assertEquals(method, selection.getDefaultMethod());
    }

    @Test(expected=NullPointerException.class)
    public void testSetDefaultMethod_Null() {
        selection.setDefaultMethod(null);
    }

    @Test
    public void testGetMethod() {
        for(int d=0; d<5; d++)
            assertEquals(DEFAULT, selection.getMethod(d));
    }

    @Test
    public void testSetMethod() {
        Method method = new Method(1d);
        selection.setMethod(method, 1);
        assertEquals(method, selection.getMethod(1));
        assertEquals(1, counter.getChangeCount());
        
        
        selection.setMethod(null, 1);
        assertEquals(DEFAULT, selection.getMethod(1));
        assertEquals(2, counter.getChangeCount());
    }

    @Test
    public void testSetMethods() {
        Map<Integer, Method> methods = new HashMap<Integer, Method>();
        methods.put(1, new Method(1d));
        methods.put(2, new Method(2d));
        methods.put(3, new Method(3d));
        selection.setMethods(methods);
        assertEquals(1, counter.getChangeCount());
        
        for(int i=1; i<4; i++) {
            Method method = selection.getMethod(i);
            assertEquals((double)i, method.getValue(i), JRLibTestUtl.EPSILON);
        }
    }

    public class AbstractOutputSelectionImpl extends AbstractMethodSelection<Vector, Method> implements Copyable<AbstractOutputSelectionImpl> {

        public AbstractOutputSelectionImpl(Vector source, Method defaultMethod) {
            super(source, defaultMethod);
        }

        @Override
        protected void recalculateLayer() {
        }

        public AbstractOutputSelectionImpl copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    private static class Method implements SelectableMethod<Vector> {

        private double value;
        
        private Method(double value) {
            this.value = value;
        }

        public double getValue(int index) {
            return value;
        }

        public SelectableMethod<Vector> copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void fit(Vector source) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
    }
}