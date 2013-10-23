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
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorTriangleTest {
    
    private final static double[] DATA = {1d, 2d, 3d, 4d, 5d};
    private final static int SIZE = DATA.length;
    
    private Triangle accidentT;
    private Triangle developmentT;
    
    public VectorTriangleTest() {
    }
    
    @Before
    public void setUp() {
        accidentT = new VectorTriangle(new InputVector(DATA, true));
        developmentT = new VectorTriangle(new InputVector(DATA, false));
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(SIZE, accidentT.getAccidentCount());
        assertEquals(1, developmentT.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(1, accidentT.getDevelopmentCount());
        assertEquals(SIZE, developmentT.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        assertEquals(0, accidentT.getDevelopmentCount(-1));
        assertEquals(0, developmentT.getDevelopmentCount(-1));

        for(int i=0; i<SIZE; i++) {
            assertEquals(1, accidentT.getDevelopmentCount(i));
            assertEquals(i==0? SIZE : 0, developmentT.getDevelopmentCount(i));
        }
        
        assertEquals(0, accidentT.getDevelopmentCount(SIZE));
        assertEquals(0, developmentT.getDevelopmentCount(SIZE));
    }

    @Test
    public void testGetValue_Cell_Accident() {
        Cell cell = new Cell(-1, 0);
        assertTrue(Double.isNaN(accidentT.getValue(cell)));
        
        cell = new Cell(0, -1);
        assertTrue(Double.isNaN(accidentT.getValue(cell)));
        
        for(int a=0; a<SIZE; a++) {
            for(int d=0; d<SIZE; d++) {
                cell = new Cell(a, d);
                double expected = d==0? DATA[a] : Double.NaN;
                assertEquals(expected, accidentT.getValue(cell), TestConfig.EPSILON);
            }
        }
        
        cell = new Cell(SIZE, 0);
        assertTrue(Double.isNaN(accidentT.getValue(cell)));
    }

    @Test
    public void testGetValue_Cell_Development() {
        Cell cell = new Cell(-1, 0);
        assertTrue(Double.isNaN(developmentT.getValue(cell)));
        
        cell = new Cell(0, -1);
        assertTrue(Double.isNaN(developmentT.getValue(cell)));
        
        for(int a=0; a<SIZE; a++) {
            for(int d=0; d<SIZE; d++) {
                cell = new Cell(a, d);
                double expected = a==0? DATA[d] : Double.NaN;
                assertEquals(expected, developmentT.getValue(cell), TestConfig.EPSILON);
            }
        }
        
        cell = new Cell(0, SIZE);
        assertTrue(Double.isNaN(developmentT.getValue(cell)));
    }

    @Test
    public void testGetValue_int_int_Accident() {
        assertTrue(Double.isNaN(accidentT.getValue(-1, 0)));
        
        assertTrue(Double.isNaN(accidentT.getValue(0, -1)));
        
        for(int a=0; a<SIZE; a++) {
            for(int d=0; d<SIZE; d++) {
                double expected = d==0? DATA[a] : Double.NaN;
                assertEquals(expected, accidentT.getValue(a, d), TestConfig.EPSILON);
            }
        }
        
        assertTrue(Double.isNaN(accidentT.getValue(SIZE, 0)));
    }

    @Test
    public void testGetValue_int_int_Development() {
        assertTrue(Double.isNaN(developmentT.getValue(-1, 0)));
        
        assertTrue(Double.isNaN(developmentT.getValue(0, -1)));
        
        for(int a=0; a<SIZE; a++) {
            for(int d=0; d<SIZE; d++) {
                double expected = a==0? DATA[d] : Double.NaN;
                assertEquals(expected, developmentT.getValue(a, d), TestConfig.EPSILON);
            }
        }
        
        assertTrue(Double.isNaN(developmentT.getValue(0, SIZE)));
    }

    @Test
    public void testToArray() {
        double[][] found = accidentT.toArray();
        assertEquals(SIZE, found.length);
        for(int i=0; i<SIZE; i++) {
            double[] f = found[i];
            assertEquals(1, f.length);
            assertEquals(DATA[i], f[0], TestConfig.EPSILON);
        }
        
        found = developmentT.toArray();
        assertEquals(1, found.length);
        
        double[] f = found[0];
        assertEquals(SIZE, f.length);
        assertArrayEquals(DATA, found[0], TestConfig.EPSILON);
    }
}