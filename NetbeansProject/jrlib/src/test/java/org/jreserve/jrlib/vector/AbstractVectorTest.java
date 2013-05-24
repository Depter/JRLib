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

import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractVectorTest {

    private final static int SIZE = 10;
    private AbstractVector data;
    
    public AbstractVectorTest() {
    }

    @Before
    public void setUp() {
        data = new AbstractVectorImpl(SIZE);
    }
    
    @Test
    public void testWithinBonds() {
        assertFalse(data.withinBonds(-1));
        for(int i=0; i<SIZE; i++)
            assertTrue(data.withinBonds(i));
        assertFalse(data.withinBonds(SIZE));
    }

    @Test
    public void testToArray() {
        double[] arr = data.toArray();
        double[] expected = {0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d};
        assertArrayEquals(expected, arr, TestConfig.EPSILON);
        
        arr[1] = 25d;
        assertEquals(1d, data.getValue(1), TestConfig.EPSILON);
    }

    private class AbstractVectorImpl extends AbstractVector {
        
        private int size;
        
        private AbstractVectorImpl(int size) {
            this.size = size;
        }
        
        @Override
        protected void recalculateLayer() {
        }

        @Override
        public int getLength() {
            return size;
        }

        @Override
        public double getValue(int index) {
            return index;
        }
    }

}