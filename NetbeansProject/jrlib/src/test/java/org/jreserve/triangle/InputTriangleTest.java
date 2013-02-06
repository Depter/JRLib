package org.jreserve.triangle;

import static org.jreserve.JLibTestSuite.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class InputTriangleTest {

    private final static double[][] DATA = {
        {1 , 2 , 3 , 4 , 5 , 6 , 7},
        {8 , 9 , 10, 11, 12},
        {13, 14, 15},
        {16}
    };
    
    public static TriangularCalculationData createData() {
        return new InputTriangle(DATA);
    }
    
    private TriangularCalculationData triangle;
    
    public InputTriangleTest() {
    }

    @Before
    public void setUp() {
        triangle = new InputTriangle(DATA);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(DATA.length, triangle.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(DATA[0].length, triangle.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        for(int a=0; a<DATA.length; a++)
            assertEquals(DATA[a].length, triangle.getDevelopmentCount(a));
    }

    @Test
    public void testGetValue() {
        assertTrue(Double.isNaN(triangle.getValue(-1, 0)));
        
        for(int a=0; a<DATA.length; a++) {
            assertTrue(Double.isNaN(triangle.getValue(a, -1)));
        
            for(int d=0; d<DATA[a].length; d++)
                assertEquals(DATA[a][d], triangle.getValue(a, d), EPSILON);
            
            assertTrue(Double.isNaN(triangle.getValue(a, DATA[a].length)));
        }
        
        assertTrue(Double.isNaN(triangle.getValue(DATA.length, 0)));
    }

    @Test
    public void testToArray() {
        double[][] data = triangle.toArray();
        assertEquals(DATA.length, data.length);
        
        for(int a=0; a<DATA.length; a++)
            assertArrayEquals(DATA[a], data[a], EPSILON);
    }

    @Test
    public void testCopeisArray() {
        double[][] data = triangle.toArray();
        data[0][0] = 100d;
        assertNotEquals(triangle.getValue(0, 0), data[0][0], EPSILON);
        
        triangle = new InputTriangle(data);
        assertEquals(data[0][0], triangle.getValue(0, 0), EPSILON);
        
        data[0][0] = DATA[0][0];
        assertNotEquals(triangle.getValue(0, 0), data[0][0], EPSILON);
    }
    
    @Test
    public void testEmptyTriangle() {
        triangle = new InputTriangle(null);
        assertEquals(0, triangle.getAccidentCount());
        assertEquals(0, triangle.getDevelopmentCount());
    }

}