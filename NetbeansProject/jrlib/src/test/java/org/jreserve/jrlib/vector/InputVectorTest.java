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
package org.jreserve.jrlib.vector;

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import static org.jreserve.jrlib.TestData.EXPOSURE;
import static org.jreserve.jrlib.TestData.getCachedVector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputVectorTest {

    private InputVector vector;
    private ChangeCounter changeCounter;
    
    private final static double[] INPUT = getCachedVector(EXPOSURE);
    
    public InputVectorTest() {
    }

    @Before
    public void setUp() {
        vector = new InputVector(INPUT);
        changeCounter = new ChangeCounter();
        vector.addChangeListener(changeCounter);
    }
    
    @Test
    public void testConstructor_Null() {
        vector = new InputVector(null);
        assertEquals(0, vector.getLength());
    }

    @Test
    public void testSetData() {
        double data[] = null;
        vector.setData(data);
        assertEquals(0, vector.getLength());
        assertEquals(1, changeCounter.getChangeCount());
        
        data = new double[]{1d, 2d, 3d, 4d};
        vector.setData(data);
        assertEquals(data.length, vector.getLength());
        assertEquals(2, changeCounter.getChangeCount());
        
        assertEquals(Double.NaN, vector.getValue(-1), TestConfig.EPSILON);
        for(int i=0; i<data.length; i++)
            assertEquals(data[i], vector.getValue(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, vector.getValue(data.length), TestConfig.EPSILON);
    }

    @Test
    public void testGetLength() {
        assertEquals(INPUT.length, vector.getLength());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, vector.getValue(-1), TestConfig.EPSILON);
        for(int i=0; i<INPUT.length; i++)
            assertEquals(INPUT[i], vector.getValue(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, vector.getValue(INPUT.length), TestConfig.EPSILON);
    }

}
