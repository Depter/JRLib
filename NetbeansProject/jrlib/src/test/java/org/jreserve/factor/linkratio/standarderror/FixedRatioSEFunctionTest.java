package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.triangle.Triangle;
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
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
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

        assertEquals(0.04608000, sef.getRatio(), JRLibTestSuite.EPSILON);
        
        assertEquals(Double.NaN, sef.getValue(-1), JRLibTestSuite.EPSILON);
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], sef.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, sef.getValue(devs), JRLibTestSuite.EPSILON);
    }
}