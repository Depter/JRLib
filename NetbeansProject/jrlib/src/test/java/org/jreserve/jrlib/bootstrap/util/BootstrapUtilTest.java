package org.jreserve.jrlib.bootstrap.util;

import org.jreserve.jrlib.bootstrap.util.BootstrapUtil;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class BootstrapUtilTest {

    private final static double[][] RESERVES = {
        { 1,  2,  3},
        { 4,  5,  6},
        { 7,  8,  9},
        {10, 11, 12}
    };
    
    private double[][] reserves;
    
    @Before
    public void setUp() {
        reserves = TriangleUtil.copy(RESERVES);
    }

    @Test
    public void testGetTotalReserves() {
        double[] expected = {6, 15, 24, 33};
        assertArrayEquals(expected, BootstrapUtil.getTotalReserves(reserves), TestConfig.EPSILON);
    }

    @Test
    public void testGetReserves() {
        double[] expected = {1, 4, 7, 10};
        assertArrayEquals(expected, BootstrapUtil.getReserves(reserves, 0), TestConfig.EPSILON);
        
        expected = new double[]{2, 5, 8, 11};
        assertArrayEquals(expected, BootstrapUtil.getReserves(reserves, 1), TestConfig.EPSILON);
        
        expected = new double[]{3, 6, 9, 12};
        assertArrayEquals(expected, BootstrapUtil.getReserves(reserves, 2), TestConfig.EPSILON);
    }

    @Test
    public void testGetMeanTotalReserve() {
        assertEquals(19.5, BootstrapUtil.getMeanTotalReserve(reserves), TestConfig.EPSILON);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {5.5, 6.5, 7.5};
        assertArrayEquals(expected, BootstrapUtil.getMeans(reserves), TestConfig.EPSILON);
    }

    @Test
    public void testGetMeanReserve() {
        assertEquals(5.5, BootstrapUtil.getMeanReserve(reserves, 0), TestConfig.EPSILON);
        assertEquals(6.5, BootstrapUtil.getMeanReserve(reserves, 1), TestConfig.EPSILON);
        assertEquals(7.5, BootstrapUtil.getMeanReserve(reserves, 2), TestConfig.EPSILON);
    }

    @Test
    public void testShiftAdjustment_doubleArr_double() {
        double[] r = BootstrapUtil.getReserves(RESERVES, 0);
        double[] expected = {1.5, 4.5, 7.5, 10.5};
        BootstrapUtil.shiftAdjustment(r, 6);
        assertArrayEquals(expected, r, TestConfig.EPSILON);
    }

    @Test
    public void testShiftAdjustment_doubleArrArr_doubleArr() {
        double[] means = {6, 7, 8};
        BootstrapUtil.shiftAdjustment(reserves, means);
        
        double[] expected = {1.5, 2.5, 3.5};
        assertArrayEquals(expected, reserves[0], TestConfig.EPSILON);
        
        expected = new double[]{4.5, 5.5, 6.5};
        assertArrayEquals(expected, reserves[1], TestConfig.EPSILON);

        expected = new double[]{7.5, 8.5, 9.5};
        assertArrayEquals(expected, reserves[2], TestConfig.EPSILON);

        expected = new double[]{10.5, 11.5, 12.5};
        assertArrayEquals(expected, reserves[3], TestConfig.EPSILON);
    }

    @Test
    public void testScaleAdjustment_doubleArr_double() {
        double[] r = BootstrapUtil.getReserves(RESERVES, 0);
        double[] expected = {1.09090909, 4.36363636, 7.63636364, 10.90909091};
        BootstrapUtil.scaleAdjustment(r, 6);
        assertArrayEquals(expected, r, TestConfig.EPSILON);
    }

    @Test
    public void testScaleAdjustment_doubleArrArr_doubleArr() {
        double[] means = {6, 7, 8};
        BootstrapUtil.scaleAdjustment(reserves, means);
        double[][] expected = {
            { 1.09090909,  2.15384615,  3.20000000},
            { 4.36363636,  5.38461538,  6.40000000},
            { 7.63636364,  8.61538462,  9.60000000},
            {10.90909091, 11.84615385, 12.80000000}
        };
        for(int i=0; i<4; i++)
            assertArrayEquals(expected[i], reserves[i], TestConfig.EPSILON);
    }

    @Test
    public void testPercentile() {
        double[] values = new double[50];
        for(int i=0; i<50; i++)
            values[i] = i+1;
        
        double[] percentiles = {
            0  , 0.1, 0.2,  0.3,  0.4,  0.5,  0.6,  0.7,  0.8,  0.9,  1};
        double[] expected = {
            1.0, 5.9, 10.8, 15.7, 20.6, 25.5, 30.4, 35.3, 40.2, 45.1, 50.0

        };
        
        for(int i=0; i<percentiles.length; i++) {
            double p = percentiles[i];
            double found = BootstrapUtil.percentile(values, p);
            assertEquals(expected[i], found, TestConfig.EPSILON);
        }
    }

}