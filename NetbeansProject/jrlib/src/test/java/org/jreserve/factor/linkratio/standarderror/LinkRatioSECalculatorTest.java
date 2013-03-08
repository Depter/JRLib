package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.scale.*;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class LinkRatioSECalculatorTest {

    private final static double[] EXPECTED_MACK = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };
    
    private LinkRatioScale scales;
    private LinkRatioSECalculator ses;
    
    public LinkRatioSECalculatorTest() {
    }

    @Before
    public void setUp() {
        scales = createScales();
        ses = new LinkRatioSECalculator(scales);
    }
    
    private LinkRatioScale createScales() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(cik));
        LinkRatioScaleSelection result = new DefaultLinkRatioScaleSelection(new LinkRatioScaleCaclulator(lr));
        
        int developments = lr.getDevelopmentCount();
        result.setMethod(new LinkRatioScaleMinMaxEstimator(), developments-1);
        return result;
    }

    @Test
    public void testGetSourceLRScales() {
        assertEquals(scales, ses.getSourceLRScales());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(scales.getDevelopmentCount(), ses.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        int length = EXPECTED_MACK.length;
        assertEquals(Double.NaN, ses.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], ses.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(EXPECTED_MACK, ses.toArray(), JRLibTestSuite.EPSILON);
    }
}