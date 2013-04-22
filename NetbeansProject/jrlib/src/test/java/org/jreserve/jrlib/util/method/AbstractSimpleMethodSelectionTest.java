package org.jreserve.jrlib.util.method;

import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;
import org.jreserve.jrlib.util.method.SelectableMethod;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.DummyCalculationData;
import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractSimpleMethodSelectionTest {

    private AbstractSimpleMethodSelection data;
    private Method defaultMethod;
    private Method estimatorMethod;
    
    public AbstractSimpleMethodSelectionTest() {
    }

    @Before
    public void setUp() {
        defaultMethod = new Method(5, 1d);
        estimatorMethod = new Method(10, 2d);
        data = new AbstractSimpleMethodSelectionImpl(defaultMethod, estimatorMethod);
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, data.getValue(-1), TestConfig.EPSILON);
        double expected = defaultMethod.value;
        for(int i=0; i<defaultMethod.length; i++)
            assertEquals(expected, data.getValue(i), TestConfig.EPSILON);
        
        expected = estimatorMethod.value;
        for(int i=defaultMethod.length; i<estimatorMethod.length; i++)
            assertEquals(expected, data.getValue(i), TestConfig.EPSILON);
        
        assertEquals(Double.NaN, data.getValue(estimatorMethod.length), TestConfig.EPSILON);
    }

    private class AbstractSimpleMethodSelectionImpl extends AbstractSimpleMethodSelection<CalculationData, Method> {

        public AbstractSimpleMethodSelectionImpl(Method defaultMethod, Method estimatorMethod) {
            super(new DummyCalculationData(), defaultMethod, estimatorMethod);
        }

        @Override
        public void initCalculation() {
        }

        @Override
        public int getLength() {
            return estimatorMethod.length;
        }
    }
    
    private class Method implements SelectableMethod<CalculationData> {
        
        private int length;
        private double value;
        
        private Method(int length, double value) {
            this.length = length;
            this.value = value;
        }
        
        public void fit(CalculationData source) {
        }

        public double getValue(int index) {
            return (0<=index && index < length)? value : Double.NaN;
        }

        public SelectableMethod<CalculationData> copy() {
            return this;
        }
    }

}