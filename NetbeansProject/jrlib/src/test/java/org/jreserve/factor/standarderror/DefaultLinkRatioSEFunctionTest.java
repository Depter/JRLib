package org.jreserve.factor.standarderror;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultLinkRatioSEFunctionTest {

    private final static double[] EXPECTED_MACK = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };
    
    public DefaultLinkRatioSEFunctionTest() {
    }

    @Test
    public void testFit() {
        LinkRatioScale scales = createScales();
        DefaultLinkRatioSEFunction se = new DefaultLinkRatioSEFunction();
        se.fit(scales);
        
        int length = EXPECTED_MACK.length;
        assertEquals(Double.NaN, se.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], se.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, se.getValue(length), JRLibTestSuite.EPSILON);
    }
    
    private LinkRatioScale createScales() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(cik));
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(new LinkRatioScaleCaclulator(lr));
        int developments = lr.getDevelopmentCount();
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), developments-1);
        scales.setDevelopmentCount(developments);
        return scales;
    }
}