package org.jreserve.jrlib.linkratio.scale.residuals;

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
public class AdjustedLinkRatioResidualsTest {
    
    private final static double[] ADJUSTMENTS = {
        1.06066017, 1.06904497, 1.08012345, 1.09544512, 1.11803399, 
        1.15470054, 1.22474487, 1.41421356, 0.00000000
    };
    
    private final static double[][] EXPECTED = {
        {-0.55058309, -1.19370698, -1.24408397,  0.84567250,  1.66605659, -0.98111865, -1.45665721, -1.07400479, 0.00000000},
        { 0.03140291,  0.04992757,  0.68252567, -0.66625869, -0.35947825,  1.08379276,  0.42395039,  0.92006180},
        { 1.36775779, -0.19152465,  0.00662744,  0.93107873, -1.27557733,  0.87543277,  0.83571278},
        { 1.59080758, -1.31287001,  1.98709663, -1.74628132, -0.31534139, -1.04709929},
        {-1.63387552,  0.73694980, -0.73743156,  0.00553514,  0.60703633},
        {-0.20846420, -0.70947416, -0.68714314,  0.96150959},
        {-0.99879058,  0.81685494, -0.14780344},
        { 0.73494643,  1.76027298},
        { 0.20906112},
    };
    
    private AdjustedLinkRatioResiduals residuals;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        residuals = new AdjustedLinkRatioResiduals(scales);
    }
    
    @Test
    public void testGetAdjustment() {
        int devs = EXPECTED[0].length;
        assertEquals(Double.NaN, residuals.getAdjustmentFactor(-1), TestConfig.EPSILON);
        for(int d=0; d<1; d++)
            assertEquals(ADJUSTMENTS[d], residuals.getAdjustmentFactor(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, residuals.getAdjustmentFactor(devs), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetValue() {
        int accidents = EXPECTED.length;
        assertEquals(Double.NaN, residuals.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = EXPECTED[a].length;
            assertEquals(Double.NaN, residuals.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], residuals.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, residuals.getValue(a, devs), TestConfig.EPSILON);            
        }
        assertEquals(Double.NaN, residuals.getValue(accidents, 0), TestConfig.EPSILON);
    }
}