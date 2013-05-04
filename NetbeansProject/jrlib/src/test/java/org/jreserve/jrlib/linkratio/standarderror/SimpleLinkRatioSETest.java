package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSETest {
    private final static double[] EXPECTED_SES = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };

    private SimpleLinkRatioSE ses;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        ses = new SimpleLinkRatioSE(scales);
    }

    @Test
    public void testRecalculateLayer() {
        int length = EXPECTED_SES.length;
        assertEquals(length, ses.getLength());
        
        assertEquals(Double.NaN, ses.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_SES[d], ses.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), TestConfig.EPSILON);
    }
}
