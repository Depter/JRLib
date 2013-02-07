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
public class TriangleSmoothingCalculationDataTest {

    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, false), 
        new SmoothingCell(1, 0, false), 
        new SmoothingCell(2, 0, true)
    };
    
    private Triangle source;
    private Triangle smoothed;
    
    public TriangleSmoothingCalculationDataTest() {
    }

    @Before
    public void setUp() {
        source = InputTriangleTest.createData();
        TriangleSmoothing smoothing = new ExponentialSmoothing(CELLS, 0d);
        smoothed = new TriangleSmoothingCalculationData(source, smoothing);
    }

    @Test
    public void testGetValue() {
        int accidents = smoothed.getAccidentCount();
        
        for(int a=0; a<accidents; a++) {
            int devs = smoothed.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double found = smoothed.getValue(a, d);
                double expected = isSmoothed(a, d)?
                        source.getValue(0, 0) :
                        source.getValue(a, d);
                assertEquals(expected, found, EPSILON);
            }
        }
    }
    
    private boolean isSmoothed(int accident, int development) {
        for(SmoothingCell cell : CELLS)
            if(cell.equals(accident, development))
                return cell.isApplied();
        return false;
    }
}