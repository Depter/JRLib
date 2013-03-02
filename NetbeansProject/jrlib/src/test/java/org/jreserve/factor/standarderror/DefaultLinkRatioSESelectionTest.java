package org.jreserve.factor.standarderror;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
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
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(new DevelopmentFactors(cik));
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), lrs.getDevelopmentCount()-1);
        ses = new DefaultLinkRatioSESelection(scales);
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
        
        assertEquals(Double.NaN, ses.getValue(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], ses.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), JRLibTestSuite.EPSILON);
    }

}