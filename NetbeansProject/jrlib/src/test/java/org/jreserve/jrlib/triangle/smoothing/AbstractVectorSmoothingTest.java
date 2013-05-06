package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.triangle.InputTriangle;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.vector.smoothing.VectorSmoothingMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractVectorSmoothingTest {

    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, true), 
        new SmoothingCell(1, 0, true), 
        new SmoothingCell(2, 0, false)
    };
    private final static double[][] DATA = {
        {1 , 2 , 3 , 4 , 5 , 6 , 7},
        {8 , 9 , 10, 11, 12},
        {13, 14, 15},
        {16}
    };
    
    private final static double SMOOTH_VALUE = -1;
    
    private Triangle data;
    private AbstractVectorSmoothingImpl smoothing;
    
    public AbstractVectorSmoothingTest() {
    }

    @Before
    public void setUp() {
        data = new InputTriangle(DATA);
        smoothing = new AbstractVectorSmoothingImpl(CELLS);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_CellsNull() {
        smoothing = new AbstractVectorSmoothingImpl(null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_CellNull() {
        SmoothingCell[] cells = {
            new SmoothingCell(0, 0, true), 
            null, 
            new SmoothingCell(2, 0, false)
        };
        smoothing = new AbstractVectorSmoothingImpl(cells);
    }

    @Test
    public void testSmooth_TriangularCalculationData() {
        double[][] original = data.toArray();
        double[][] smoothed = smoothing.smooth(data);
        
        assertEquals(original.length, smoothed.length);
        for(int a=0; a<original.length; a++) {
            int devs = original[a].length;
            assertEquals(devs, smoothed[a].length);
            
            for(int d=0; d<devs; d++) {
                if(isSmoothed(a, d))
                    assertEquals(SMOOTH_VALUE, smoothed[a][d], TestConfig.EPSILON);
                else
                    assertEquals(original[a][d], smoothed[a][d], TestConfig.EPSILON);
            }
        }
    }
    
    private boolean isSmoothed(int accident, int development) {
        for(int i=0; i<CELLS.length; i++)
            if(CELLS[i].equals(accident, development))
                return CELLS[i].isApplied();
        return false;
    }

    private class AbstractVectorSmoothingImpl extends AbstractVectorSmoothing {

        private AbstractVectorSmoothingImpl(SmoothingCell[] cells) {
            super(cells, new Smoother());
        }

        public TriangleSmoothing copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    private class Smoother implements VectorSmoothingMethod {

        public void smooth(double[] input) {
            for(int i=0; i<input.length; i++)
                input[i] = SMOOTH_VALUE;
        }
    }

}
