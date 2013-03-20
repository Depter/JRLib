package org.jreserve.util;

import org.jreserve.JRLibTestUtl;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractLinearRegressionTest {

    private final static double[] Y = {1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
    private final static double[] X = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    
    private AbstractLinearRegressionImpl lr;
    
    public AbstractLinearRegressionTest() {
    }

    @Before
    public void setUp() {
        lr = new AbstractLinearRegressionImpl();
    }

    @Test
    public void testSetExcluded() {
        assertArrayEquals(Y, lr.getYs(null), JRLibTestUtl.EPSILON);
        
        int index = 2;
        lr.setExcluded(index, true);
        assertTrue(lr.isExcluded(index));
        
        double[] y = lr.getYs(null);
        lr.excludeValues(y);
        for(int i=0; i<Y.length; i++) {
            if(i == index)
                assertTrue(Double.isNaN(y[i]));
            else
                assertEquals(Y[i], y[i], JRLibTestUtl.EPSILON);
        }
        
        lr.setExcluded(index, false);
        assertFalse(lr.isExcluded(index));
        y = lr.getYs(null);
        lr.excludeValues(y);
        assertArrayEquals(Y, y, JRLibTestUtl.EPSILON);
    }

    @Test
    public void testFit() {
        lr.fit(null);
        assertEquals(1.0, lr.intercept, JRLibTestUtl.EPSILON);
        assertEquals(0.5, lr.slope, JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetXZeroBased() {
        int length = 10;
        double[] x = lr.getXZeroBased(length);
        assertEquals(length, x.length);
        for(int i=0; i<length; i++)
            assertEquals((double)i, x[i], JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetXOneBased() {
        int length = 10;
        double[] x = lr.getXOneBased(length);
        assertEquals(length, x.length);
        for(int i=0; i<length; i++)
            assertEquals((double)(i+1), x[i], JRLibTestUtl.EPSILON);
    }

    public class AbstractLinearRegressionImpl extends AbstractLinearRegression<Object> {

        //y = 1 + 0.5 * x
        @Override
        public double[] getYs(Object source) {
            double[] y = new double[Y.length];
            System.arraycopy(Y, 0, y, 0, Y.length);
            return y;
        }

        @Override
        public double[] getXs(Object source) {
            double[] x = new double[X.length];
            System.arraycopy(X, 0, x, 0, X.length);
            return x;
        }

        @Override
        public double getValue(int index) {
            double x = (double) (index+1);
            return intercept + slope * x;
        }

        public SelectableMethod<Object> copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}