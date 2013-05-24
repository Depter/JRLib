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

import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractVectorUserInputTest {
    
    public AbstractVectorUserInputTest() {
    }
    
    @Test
    public void testConstructor_array() {
        double[] expected = new double[]{1d, 2d, 3d};
        AbstractVectorUserInput ui = new AbstractVectorUserInputImpl(expected);
        
        for(int i=0; i<expected.length; i++)
            assertEquals(expected[i], ui.getValue(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, ui.getValue(expected.length), TestConfig.EPSILON);
        
        double e = expected[1];
        expected[1] += 10d;
        assertEquals(e, ui.getValue(1), TestConfig.EPSILON);
    }

    @Test
    public void testSetValue() {
        AbstractVectorUserInput ui = new AbstractVectorUserInputImpl();
        assertEquals(Double.NaN, ui.getValue(-1), TestConfig.EPSILON);
        assertEquals(Double.NaN, ui.getValue(5), TestConfig.EPSILON);
        
        ui.setValue(5, 1d);
        for(int i=0; i<5; i++)
            assertEquals(Double.NaN, ui.getValue(i), TestConfig.EPSILON);
        assertEquals(1d, ui.getValue(5), TestConfig.EPSILON);
    }
    
    private class AbstractVectorUserInputImpl extends AbstractVectorUserInput {

        private AbstractVectorUserInputImpl(double[] values) {
            super(values);
        }

        private AbstractVectorUserInputImpl() {
        }
        
        public SelectableMethod copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
