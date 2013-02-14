package org.jreserve.factor;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class WeightTriangleTest {

    private WeightTriangle weights;
    private ChangeCounter counter;
    
    public WeightTriangleTest() {
    }

    @Before
    public void setUp() {
        double[][] data = new double[0][];
        weights = new WeightTriangle(data);
        counter = new ChangeCounter();
        weights.addChangeListener(counter);
    }

    @Test
    public void testSetWeights() {
        double[][] data = new double[][] {
            {2d, 3d},
            {4d}
        };
        weights.setWeights(data);
        assertEquals(1, counter.getChangeCount());
        
        int accidents = data.length;
        assertEquals(accidents, weights.getAccidentCount());
        for(int a=0; a<accidents; a++) {
            int devs = data[a].length;
            assertEquals(devs, weights.getDevelopmentCount(a));
            for(int d=0; d<devs; d++)
                assertEquals(data[a][d], weights.getValue(a, d), JRLibTestSuite.EPSILON);
            assertEquals(1d, weights.getValue(a, devs), JRLibTestSuite.EPSILON);
        }
        int devs = weights.getDevelopmentCount();
        assertEquals(1d, weights.getValue(accidents, devs), JRLibTestSuite.EPSILON);
    }
}