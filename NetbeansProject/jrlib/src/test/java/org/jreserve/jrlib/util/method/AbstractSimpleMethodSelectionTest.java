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
package org.jreserve.jrlib.util.method;

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