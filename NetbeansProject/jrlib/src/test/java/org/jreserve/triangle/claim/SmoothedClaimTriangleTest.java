package org.jreserve.triangle.claim;

import org.jreserve.triangle.claim.SmoothedClaimTriangle;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.jreserve.JRLibTestUtl.EPSILON;
import org.jreserve.smoothing.ExponentialSmoothing;
import org.jreserve.smoothing.SmoothingCell;
import org.jreserve.smoothing.TriangleSmoothing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class SmoothedClaimTriangleTest {

    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, false), 
        new SmoothingCell(1, 0, false), 
        new SmoothingCell(2, 0, true)
    };
    
    private ClaimTriangle source;
    private ClaimTriangle smoothed;
    
    public SmoothedClaimTriangleTest() {
    }

    @Before
    public void setUp() {
        source = InputTriangleTest.createData();
        TriangleSmoothing smoothing = new ExponentialSmoothing(CELLS, 0d);
        smoothed = new SmoothedClaimTriangle(source, smoothing);
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