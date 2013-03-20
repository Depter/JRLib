package org.jreserve.linkratio.standarderror;

import org.jreserve.linkratio.standarderror.DefaultLinkRatioSESelection;
import org.jreserve.linkratio.standarderror.LinkRatioSECalculator;
import org.jreserve.linkratio.standarderror.UserInputLinkRatioSEFunction;
import org.jreserve.linkratio.standarderror.DefaultLinkRatioSEFunction;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultLinkRatioSESelectionTest {
    private final static double[] EXPECTED_MACK = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };

    private DefaultLinkRatioSESelection ses;
    
    public DefaultLinkRatioSESelectionTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(new DevelopmentFactors(cik));
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), lrs.getDevelopmentCount()-1);
        ses = new DefaultLinkRatioSESelection(new LinkRatioSECalculator(scales));
    }

    @Test
    public void testSetDefaultMethod() {
        assertTrue(ses.getDefaultMethod() instanceof DefaultLinkRatioSEFunction);
        
        ses.setDefaultMethod(new UserInputLinkRatioSEFunction());
        assertTrue(ses.getDefaultMethod() instanceof UserInputLinkRatioSEFunction);
        
        ses.setDefaultMethod(null);
        assertTrue(ses.getDefaultMethod() instanceof DefaultLinkRatioSEFunction);
    }

    @Test
    public void testRecalculateLayer() {
        int length = EXPECTED_MACK.length;
        assertEquals(length, ses.getDevelopmentCount());
        
        assertEquals(Double.NaN, ses.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], ses.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), JRLibTestUtl.EPSILON);
    }

}