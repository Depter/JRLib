package org.jreserve.factor.linkratio;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.triangle.TriangleFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioTest {

    private final static double[] EXPECTED_WA = {
        1.19471971, 0.99540619, 0.99507566, 1.01018160, 
        1.00310913, 1.00031727, 0.98794674
    };
    
    private final static double[] EXPECTED_A = {
        1.19745695, 0.99507487, 0.99595362, 1.01091703, 
        1.00373787, 1.00032791, 0.98794674
    };
    
    private FactorTriangle source;
    private SimpleLinkRatio lr;
    
    public SimpleLinkRatioTest() {
    }

    @Before
    public void setUp() {
        source = new DevelopmentFactors(TriangleFactory.create(TestData.INCURRED).cummulate().build());
        lr = new SimpleLinkRatio(source);
    }

    @Test
    public void testGetDevelopmentFactors() {
        assertTrue(source == lr.getInputFactors());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(source.getDevelopmentCount(), lr.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        int length = EXPECTED_WA.length;
        assertEquals(Double.NaN, lr.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_WA[d], lr.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, lr.getValue(length), JRLibTestSuite.EPSILON);
        
        lr = new SimpleLinkRatio(source, new AverageLRMethod());
        length = EXPECTED_A.length;
        assertEquals(Double.NaN, lr.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_A[d], lr.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, lr.getValue(length), JRLibTestSuite.EPSILON);        
    }

    @Test
    public void testToArray() {
        double[] found = lr.toArray();
        assertArrayEquals(EXPECTED_WA, found, JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetMackAlpha() {
        double expected = WeightedAverageLRMethod.DEFAULT_MACK_ALPHA;
        assertEquals(expected, lr.getMackAlpha(0), JRLibTestSuite.EPSILON);
        
        expected = AverageLRMethod.MACK_ALPHA;
        double found = new SimpleLinkRatio(source, new AverageLRMethod()).getMackAlpha(5);
        assertEquals(expected, found, JRLibTestSuite.EPSILON);
    }
}