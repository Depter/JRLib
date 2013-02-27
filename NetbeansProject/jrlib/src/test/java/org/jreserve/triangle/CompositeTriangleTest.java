package org.jreserve.triangle;

import org.jreserve.TestData;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jreserve.JRLibTestSuite.EPSILON;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CompositeTriangleTest {

    private final static double[][] EXPECTED = {
        {167.85215941, 196.72892616, 199.85656749, 200.30790161, 201.69444243, 202.58291330, 203.91801139, 204.30782899},
        {158.88781088, 187.21044563, 189.18137187, 190.32727716, 192.91071366, 193.74198824, 193.76189151},
        {170.83376472, 202.42574596, 204.61773283, 207.92648290, 211.41388398, 211.77165623},
        {174.63228786, 202.91084914, 207.88476395, 208.37557477, 208.56693131},
        {170.81534013, 215.26446878, 219.47629491, 223.04865878},
        {175.06967420, 208.31446749, 209.91400036},
        {173.03697589, 207.31731734},
        {178.99654177}
    };
    
    private Triangle paid;
    private Triangle count;
    private CompositeTriangle triangle;
    
    public CompositeTriangleTest() {
    }

    @Before
    public void setUp() {
        paid = TriangleFactory.create(TestData.PAID).cummulate().build();
        count = TriangleFactory.create(TestData.NoC).cummulate().build();
        triangle = new CompositeTriangle(paid, count, TriangleOperation.DIVIDE);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(EXPECTED.length, triangle.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(EXPECTED[0].length, triangle.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        for(int a=0; a<paid.getAccidentCount(); a++)
            assertEquals(EXPECTED[a].length, triangle.getDevelopmentCount(a));
    }

    @Test
    public void testGetValue_Cell() {
        assertTrue(Double.isNaN(triangle.getValue(new Cell(-1, 0))));
        
        for(int a=0; a<EXPECTED.length; a++) {
            assertTrue(Double.isNaN(triangle.getValue(new Cell(a, - 1))));
        
            for(int d=0; d<EXPECTED[a].length; d++)
                assertEquals(EXPECTED[a][d], triangle.getValue(new Cell(a, d)), EPSILON);
            
            assertTrue(Double.isNaN(triangle.getValue(new Cell(a, EXPECTED[a].length))));
        }
        
        assertTrue(Double.isNaN(triangle.getValue(new Cell(EXPECTED.length, 0))));
    }

    @Test
    public void testGetValue_int_int() {
        assertTrue(Double.isNaN(triangle.getValue(-1, 0)));
        
        for(int a=0; a<EXPECTED.length; a++) {
            assertTrue(Double.isNaN(triangle.getValue(a, - 1)));
        
            for(int d=0; d<EXPECTED[a].length; d++)
                assertEquals(EXPECTED[a][d], triangle.getValue(a, d), EPSILON);
            
            assertTrue(Double.isNaN(triangle.getValue(a, EXPECTED[a].length)));
        }
        
        assertTrue(Double.isNaN(triangle.getValue(EXPECTED.length, 0)));
    }

    @Test(expected=NullPointerException.class)
    public void testGetValue_Cell_Null() {
        triangle.getValue(null);
    }

    @Test
    public void testToArray() {
        double[][] data = triangle.toArray();
        assertEquals(EXPECTED.length, data.length);
        
        for(int a=0; a<EXPECTED.length; a++)
            assertArrayEquals(EXPECTED[a], data[a], EPSILON);
    }
}