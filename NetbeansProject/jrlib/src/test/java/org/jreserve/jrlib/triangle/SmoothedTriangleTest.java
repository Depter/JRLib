package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.triangle.smoothing.ArithmeticMovingAverage;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedTriangleTest {

    private final static double[][] DATA = {
        {1 , 2 , 3 , 4 , 5 , 6 , 7},
        {8 , 9 , 10, 11, 12},
        {13, 14, 15},
        {16}
    };
    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, false),
        new SmoothingCell(1, 0, false),
        new SmoothingCell(2, 0, true)
    };
    private final static int MA_LENGTH = 3;
    
    private Triangle source;
    private SmoothedTriangle smoothed;
    
    public SmoothedTriangleTest() {
    }
    
    @Before
    public void setUp() {
        source = new InputTriangle(DATA);
        TriangleSmoothing smoothing = new ArithmeticMovingAverage(CELLS, MA_LENGTH);
        smoothed = new SmoothedTriangle(source, smoothing);
    }

    @Test
    public void testRecalculateLayer() {
        int accidents = source.getAccidentCount();
        assertEquals(accidents, smoothed.getAccidentCount());
        
        assertEquals(Double.NaN, smoothed.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount();
            assertEquals(devs, smoothed.getDevelopmentCount());
            
            assertEquals(Double.NaN, smoothed.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++) {
                if(a==2 && d==0) {
                    double expected = (1d+8d+13d) / 3d;
                    assertEquals(expected, smoothed.getValue(a, d), TestConfig.EPSILON);
                } else {
                    assertEquals(source.getValue(a, d), smoothed.getValue(a, d), TestConfig.EPSILON);
                }
            }
            assertEquals(Double.NaN, smoothed.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, smoothed.getValue(accidents, 0), TestConfig.EPSILON);
    }
}