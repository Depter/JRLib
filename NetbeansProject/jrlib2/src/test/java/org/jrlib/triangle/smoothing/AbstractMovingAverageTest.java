package org.jrlib.triangle.smoothing;

import org.jrlib.TestConfig;
import org.jrlib.triangle.TriangleUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractMovingAverageTest {

    private final static SmoothingCell[] CELLS = {
        new SmoothingCell(0, 0, true), 
        new SmoothingCell(1, 0, true), 
        new SmoothingCell(2, 0, false)
    };

    public AbstractMovingAverageTest() {
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooShortMa() {
        new AbstractMovingAverageImpl(CELLS, 0);
    }

    @Test
    public void testSmooth() {
        
        AbstractMovingAverageImpl smoothing = new AbstractMovingAverageImpl(CELLS, 2);
        double[] original = {1d, 2d, 3d};
        double[] data = TriangleUtil.copy(original);
        smoothing.smooth(data);
        
        for(int i=0; i<(smoothing.maLength-1); i++)
            assertEquals(original[i], data[i], TestConfig.EPSILON);
        
        for(int i=smoothing.maLength; i<data.length; i++) {
            double expected = original[i] / 2d;
            assertEquals(expected, data[i], TestConfig.EPSILON);
        }
    }

    public class AbstractMovingAverageImpl extends AbstractMovingAverage {

        public AbstractMovingAverageImpl(SmoothingCell[] cells, int maLength) {
            super(cells, maLength);
        }
        
        @Override
        public double mean(double[] input, int index) {
            return input[index]/2d;
        }

        @Override
        public TriangleSmoothing copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}