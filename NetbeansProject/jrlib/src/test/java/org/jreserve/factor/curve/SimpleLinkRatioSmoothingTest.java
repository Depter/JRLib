package org.jreserve.factor.curve;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class SimpleLinkRatioSmoothingTest {

    private final static int DEVELOPMENTS = 10;
    
    //Until input is not Double.NaN, return input.
    private final static double[] EXPECTED = {
        1.24694402, 1.01584722, 1.00946012, 1.00954295, 1.00347944, 
        1.00335199, 1.00191164, 1.00210157, 1.00168570, 1.00138394
    };
    
    private FactorTriangle factors;
    private LinkRatio source;
    private SimpleLinkRatioSmoothing smoothing;
    private ChangeCounter counter;
    
    public SimpleLinkRatioSmoothingTest() {
    }

    @Before
    public void setUp() {
        factors = new DevelopmentFactors(TestData.getCummulatedTriangle(TestData.PAID));
        source = new SimpleLinkRatio(factors);
        smoothing = new SimpleLinkRatioSmoothing(source, DEVELOPMENTS, new InversePowerLRFunction());
        counter = new ChangeCounter();
        smoothing.addChangeListener(counter);
    }

    @Test
    public void testSetDevelopmentCount() {
        smoothing.setDevelopmentCount(DEVELOPMENTS+1);
        assertEquals(1, counter.getChangeCount());
    }

    @Test
    public void testGetInputFactors() {
        assertEquals(factors, smoothing.getInputFactors());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(DEVELOPMENTS, smoothing.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, smoothing.getValue(-1), JRLibTestSuite.EPSILON);
        int length = EXPECTED.length;
        for(int d=0; d<length; d++)
            assertEquals("Ar d="+d, EXPECTED[d], smoothing.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(1d, smoothing.getValue(length), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testToArray() {
        double[] found = smoothing.toArray();
        assertEquals(EXPECTED.length, found.length);
        assertArrayEquals(EXPECTED, found, JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetMackAlpha() {
        int maxDev = source.getDevelopmentCount()-1;
        for(int d=0; d<DEVELOPMENTS; d++) {
            double expected = source.getMackAlpha(d<maxDev? d : maxDev);
            assertEquals(expected, smoothing.getMackAlpha(d), JRLibTestSuite.EPSILON);
        }
    }
}