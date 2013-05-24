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
package org.jreserve.jrlib.vector.smoothing;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractSmoothingTest {

    private final static SmoothingIndex[] CELLS = {
        new SmoothingIndex(0, true), 
        new SmoothingIndex(1, true), 
        new SmoothingIndex(2, false)
    };
    private final static double[] DATA = {1, 8, 13, 16};
    
    
    private final static double SMOOTH_VALUE = -1;
    
    private Vector data;
    private AbstractSmoothingImpl smoothing;

    @Before
    public void setUp() {
        data = new InputVector(DATA);
        smoothing = new AbstractSmoothingImpl(CELLS);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_CellsNull() {
        smoothing = new AbstractSmoothingImpl(null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_CellNull() {
        SmoothingIndex[] cells = {
            new SmoothingIndex(0, true), 
            null, 
            new SmoothingIndex(2, false)
        };
        smoothing = new AbstractSmoothingImpl(cells);
    }

    @Test
    public void testSmooth_TriangularCalculationData() {
        double[] original = data.toArray();
        double[]smoothed = smoothing.smooth(data);
        
        int length = original.length;
        assertEquals(length, smoothed.length);
            
        for(int i=0; i<length; i++) {
            double expected = isSmoothed(i) ? SMOOTH_VALUE : original[i];
            assertEquals(expected, smoothed[i], TestConfig.EPSILON);
        }
    }
    
    private boolean isSmoothed(int index) {
        for(int i=0; i<CELLS.length; i++)
            if(CELLS[i].getIndex() == index)
                return CELLS[i].isApplied();
        return false;
    }
    
    private class AbstractSmoothingImpl extends AbstractSmoothing {
        
        private AbstractSmoothingImpl(SmoothingIndex[] cells) {
            super(cells, new Smoother());
        }
    }
    
    private class Smoother implements VectorSmoothingMethod {

        public void smooth(double[] input) {
            for(int i=0; i<input.length; i++)
                input[i] = SMOOTH_VALUE;
        }
    }
}
