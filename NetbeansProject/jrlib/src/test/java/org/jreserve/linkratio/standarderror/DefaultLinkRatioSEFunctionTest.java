package org.jreserve.linkratio.standarderror;

import org.jreserve.linkratio.standarderror.LinkRatioSECalculator;
import org.jreserve.linkratio.standarderror.DefaultLinkRatioSEFunction;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScaleCaclulator;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
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
        DefaultLinkRatioSEFunction se = new DefaultLinkRatioSEFunction();
        LinkRatioScale scales = createScales();
        se.fit(new LinkRatioSECalculator(scales));
        
        int length = EXPECTED_MACK.length;
        assertEquals(Double.NaN, se.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], se.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, se.getValue(length), JRLibTestUtl.EPSILON);
    }
    
    private LinkRatioScale createScales() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(cik));
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(new LinkRatioScaleCaclulator(lr));
        int developments = lr.getDevelopmentCount();
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), developments-1);
        return scales;
    }
}