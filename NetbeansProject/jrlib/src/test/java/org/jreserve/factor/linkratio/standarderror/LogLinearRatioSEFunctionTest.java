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
public class LogLinearRatioSEFunctionTest {

    private final static double[] EXPECTED = {
        1.95905199, 0.47955194, 0.13293869, 0.06596653, 
        0.03911156, 0.02437721, 0.01555010, 0.01029200
    };
    
    private LogLinearRatioSEFunction sef;
    private LinkRatioSE ses;

    public LogLinearRatioSEFunctionTest() {
    }

    @Before
    public void setUp() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), lrs.getDevelopmentCount()-1);
        ses = new DefaultLinkRatioSESelection(scales);
        
        sef = new LogLinearRatioSEFunction();
    }

    @Test
    public void testFit() {
        sef.fit(ses);
        assertEquals(-1.325732323, sef.getIntercept(), JRLibTestSuite.EPSILON);
        assertEquals(-0.409135714, sef.getSlope(), JRLibTestSuite.EPSILON);
        
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], sef.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, sef.getValue(devs), JRLibTestSuite.EPSILON);
    }
}