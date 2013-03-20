package org.jreserve.linkratio.standarderror;

import org.jreserve.linkratio.standarderror.DefaultLinkRatioSESelection;
import org.jreserve.linkratio.standarderror.FixedRatioSEFunction;
import org.jreserve.linkratio.standarderror.LinkRatioSE;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class FixedRatioSEFunctionTest {

    private final static double[] EXPECTED = {
        0.51168420, 0.18857194, 0.07870063, 0.05879438, 
        0.05248108, 0.04924554, 0.04729351, 0.04712522
    };
    
    private FixedRatioSEFunction sef;
    private LinkRatioSE ses;
    
    public FixedRatioSEFunctionTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), lrs.getDevelopmentCount()-1);
        ses = new DefaultLinkRatioSESelection(scales);
        
        sef = new FixedRatioSEFunction();
    }

    @Test
    public void testFit() {
        sef.setExcluded(0, true);
        sef.fit(ses);

        assertEquals(0.04608000, sef.getRatio(), JRLibTestUtl.EPSILON);
        
        assertEquals(Double.NaN, sef.getValue(-1), JRLibTestUtl.EPSILON);
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], sef.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, sef.getValue(devs), JRLibTestUtl.EPSILON);
    }
}