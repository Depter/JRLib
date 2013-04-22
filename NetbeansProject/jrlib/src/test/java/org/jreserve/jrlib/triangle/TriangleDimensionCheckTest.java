package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.triangle.InputTriangle;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleDimensionCheckTest {
    
    @Test
    public void testAcceptsNull() {
        new InputTriangle(null);
    }
    
    @Test
    public void testAcceptsEmpty() {
        new InputTriangle(new double[0][]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyAccident() {
        new InputTriangle(new double[1][0]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyAccident2() {
        double[][] data = {
            {1, 2},
            {}
        };
        new InputTriangle(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSameDevLength() {
        double[][] data = {
            {1, 2},
            {2, 3},
            {1}
        };
        new InputTriangle(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInconsistentDevLength() {
        double[][] data = {
            {1, 2, 4, 5},
            {2, 3},
            {1}
        };
        new InputTriangle(data);
    }
}