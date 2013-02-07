package org.jreserve.smoothing;

import static org.jreserve.JLibTestSuite.EPSILON;
import org.jreserve.triangle.InputTriangleTest;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractVectorSmoothingTest {

    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, true), new SmoothingCell(1, 0, true), new SmoothingCell(2, 0, false)
    };
    
    private final static double SMOOTH_VALUE = -1;
    
    private Triangle data;
    private AbstractVectorSmoothingImpl smoothing;
    
    public AbstractVectorSmoothingTest() {
    }

    @Before
    public void setUp() {
        data = InputTriangleTest.createData();
        smoothing = new AbstractVectorSmoothingImpl(CELLS);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_CellsNull() {
        new AbstractVectorSmoothingImpl(null);
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
                    assertEquals(SMOOTH_VALUE, smoothed[a][d], EPSILON);
                else
                    assertEquals(original[a][d], smoothed[a][d], EPSILON);
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
            super(cells);
        }

        @Override
        public void smooth(double[] input) {
            for(int i=0; i<input.length; i++)
                input[i] = SMOOTH_VALUE;
        }
    }

}