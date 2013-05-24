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
package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleUtilTest {

    private final static double[][] INCREMENTIAL = {
        {26373, 1173, 14, 4 , 2, 11, 0, 0},
        {27623, 1078, 19, 11, 8, 0 , 0},
        {30908, 1299, 27, 17, 2, 1},
        {31182, 1392, 46, 2 , 1},
        {32855, 1754, 46, 15, },
        {37661, 1634, 54},
        {38160, 1696},
        {40194}
    };
    
    private final static double[][] CUMMULATED = {
        {26373, 27546, 27560, 27564, 27566, 27577, 27577, 27577},
        {27623, 28701, 28720, 28731, 28739, 28739, 28739},
        {30908, 32207, 32234, 32251, 32253, 32254},
        {31182, 32574, 32620, 32622, 32623},
        {32855, 34609, 34655, 34670},
        {37661, 39295, 39349},
        {38160, 39856},
        {40194},
    };
    
    private double[][] cummulated;
    private double[][] incremential;
    
    public TriangleUtilTest() {
    }

    @Before
    public void setUp() {
        cummulated = TriangleUtil.copy(CUMMULATED);
        incremential = TriangleUtil.copy(INCREMENTIAL);
    }

    @Test
    public void testCopy_1dim() {
        double[] values = null;
        assertTrue(TriangleUtil.copy(values) == null);
        
        double[] expected = CUMMULATED[0];
        double[] found = TriangleUtil.copy(expected);
        assertTrue(expected != found);
        assertArrayEquals(expected, found, TestConfig.EPSILON);
    }
    
    @Test
    public void testCopy_2dim() {
        double[][] values = null;
        assertTrue(TriangleUtil.copy(values) == null);
        
        double[][] found = TriangleUtil.copy(CUMMULATED);
        
        assertTrue(CUMMULATED != found);
        int size = CUMMULATED.length;
        assertEquals(size, found.length);
        for(int i=0; i<size; i++)
            assertArrayEquals(CUMMULATED[i], found[i], TestConfig.EPSILON);
        
        values = TriangleUtil.copy(CUMMULATED);
        values[0] = null;
        found = TriangleUtil.copy(values);
        
        assertTrue(values != found);
        size = values.length;
        assertEquals(size, found.length);
        for(int i=0; i<size; i++)
            assertArrayEquals(values[i], found[i], TestConfig.EPSILON);
    }
    
    @Test
    public void testCummulate() {
        TriangleUtil.cummulate(incremential);
        int size = CUMMULATED.length;
        assertEquals(size, incremential.length);
        for(int i=0; i<size; i++)
            assertArrayEquals(CUMMULATED[i], incremential[i], TestConfig.EPSILON);
    }

    @Test
    public void testDeCummulate() {
        TriangleUtil.deCummulate(cummulated);
        int size = INCREMENTIAL.length;
        assertEquals(size, cummulated.length);
        for(int i=0; i<size; i++)
            assertArrayEquals(INCREMENTIAL[i], cummulated[i], TestConfig.EPSILON);
    }
    
    @Test
    public void testGetAccidentCount() {
        Triangle triangle = new InputTriangle(cummulated);
        for(int d=0; d<8; d++)
            assertEquals(8-d, TriangleUtil.getAccidentCount(triangle, d));
        assertEquals(0, TriangleUtil.getAccidentCount(triangle, 8));
        assertEquals(0, TriangleUtil.getAccidentCount(triangle, 9));
        
        cummulated[7][0] = Double.NaN;
        triangle = new InputTriangle(cummulated);
        for(int d=0; d<8; d++)
            assertEquals(8-d, TriangleUtil.getAccidentCount(triangle, d));
        assertEquals(0, TriangleUtil.getAccidentCount(triangle, 8));
        assertEquals(0, TriangleUtil.getAccidentCount(triangle, 9));
    }

}
