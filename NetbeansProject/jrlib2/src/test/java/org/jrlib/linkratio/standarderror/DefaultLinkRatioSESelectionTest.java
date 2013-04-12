package org.jrlib.linkratio.standarderror;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
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
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
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
        assertEquals(length, ses.getLength());
        
        assertEquals(Double.NaN, ses.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], ses.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), TestConfig.EPSILON);
    }
}
