package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.FixedRandom;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.estimate.mcl.MclEstimateInput;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclPseudoDataTest {


    private final static double[][] PAID_LR = {
        {1.21582326, 1.01662234, 0.99981929, 1.00606193, 1.00218851, 1.0038334, 0.9997944, Double.NaN},
        {1.22776731, 1.01042516, 1.01030501, 1.0001398, 1.00268624, 0.99999318, Double.NaN},
        {1.25103501, 1.00772819, 1.00618285, 1.00439495, 1.0036468, Double.NaN},
        {1.2006619, 1.0128477, 1.00419236, 1.01027704, Double.NaN},
        {1.20135468, 1.01302196, 1.00448616, Double.NaN},
        {1.25060502, 1.00860147, Double.NaN},
        {1.23130962, Double.NaN},
        {Double.NaN},
    };
    
    private final static double[][] PAID_CR = {
        {1.05067331, 1.01482745, 0.98461964, 1.00838366, 1.02202514, 0.98550925, 0.93817525, Double.NaN},
        {0.99273096, 1.05567868, 0.97622313, 1.00848813, 1.00009870, 1.01268499, Double.NaN},
        {1.00480192, 1.03531094, 0.99665069, 1.03622913, 1.00568821, Double.NaN},
        {1.01912897, 1.03543308, 1.03000960, 1.01389165, Double.NaN},
        {1.02240735, 1.04332097, 1.03579342, Double.NaN},
        {1.02932814, 1.04992814, Double.NaN},
        {1.03806396, Double.NaN},
        {Double.NaN},
    };
    
    private final static double[][] INCURRED_LR = {
        {1.16783245, 0.98184459, 1.01626443, 1.0163654, 0.99869228, 1.00007425, 0.98797939, Double.NaN},
        {1.22600071, 0.98337766, 0.98744149, 1.02462197, 1.00816568, 1.00010284, Double.NaN},
        {1.16812341, 1.02678885, 1.00261677, 1.0056269, 0.9988537, Double.NaN},
        {1.26152203, 1.00805281, 0.98874796, 1.00525547, Double.NaN},
        {1.25964626, 1.00743552, 0.989192, Double.NaN},
        {1.17217609, 1.02281323, Double.NaN},
        {1.21833782, Double.NaN},
        {Double.NaN},
    };
    
    private final static double[][] INCURRED_CR = {
        {0.94519202, 0.98016959, 1.01041044, 0.98972134, 0.97946696, 1.0098367, 1.05246977, Double.NaN},
        {0.97549262, 0.94568634, 1.01092904, 0.98766904, 0.99257158, 0.9845530, Double.NaN},
        {0.97361666, 0.95873932, 0.99151685, 0.96570512, 0.99090579, Double.NaN},
        {0.95723856, 0.95513118, 0.96962144, 0.98379078, Double.NaN},
        {0.95303846, 0.95018603, 0.96347673, Double.NaN},
        {0.94832840, 0.94544760, Double.NaN},
        {0.93437849, Double.NaN},
        {Double.NaN},
    };
    
    private MclPseudoData data;
    private FactorTriangle paidF;
    private FactorTriangle incurredF;
    private RatioTriangle paidR;
    private RatioTriangle incurredR;
    
    
    @Before
    public void setUp() {
        MclResidualBundle bundle = createBundle();
        data = new MclPseudoData(new FixedRandom(), bundle);
        data.recalculate();
        
        paidF = data.getPaidFactorTriangle();
        paidR = data.getPaidRatioTriangle();
        incurredF = data.getIncurredFactorTriangle();
        incurredR = data.getIncurredRatioTriangle();
    }
    
    private MclResidualBundle createBundle() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle paidLR = createLRResiduals(paid);
        CRResidualTriangle paidCR = createCRResiduals(incurred, paid);
        LRResidualTriangle incurredLR = createLRResiduals(incurred);
        CRResidualTriangle incurredCR = createCRResiduals(paid, incurred);
        
        return new MclResidualBundle(paidLR, paidCR, incurredLR, incurredCR);
    }
    
    private LRResidualTriangle createLRResiduals(ClaimTriangle cik) {
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        return new AdjustedLinkRatioResiduals(scales);
    }
    
    private CRResidualTriangle createCRResiduals(ClaimTriangle numerator, ClaimTriangle denominator) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denominator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle res = new ClaimRatioResiduals(scales);
        return new AdjustedClaimRatioResiduals(res);
    }
    
//    private CRResidualTriangle excludeLastDiagonal(CRResidualTriangle res) {
//        for(int a=0; a<res.getAccidentCount(); a++)
//            res = new ClaimRatioResidualTriangleCorrection(res, a, res.getDevelopmentCount(a)-1, Double.NaN);
//        return res;
//    }
    
    @Test
    public void testAccidentCounts() {
        int accidents = PAID_LR.length;
        assertEquals(accidents-1, paidF.getAccidentCount());
        assertEquals(accidents-1, incurredF.getAccidentCount());
        assertEquals(accidents, paidR.getAccidentCount());
        assertEquals(accidents, incurredR.getAccidentCount());
    }
    
    @Test
    public void testDevelopmentCounts() {
        int developments = PAID_LR[0].length;
        assertEquals(developments-1, paidF.getDevelopmentCount());
        assertEquals(developments-1, incurredF.getDevelopmentCount());
        assertEquals(developments, paidR.getDevelopmentCount());
        assertEquals(developments, incurredR.getDevelopmentCount());
    }
    
    @Test
    public void testDevelopmentCounts_int() {
        assertEquals(0, paidF.getDevelopmentCount(-1));
        assertEquals(0, incurredF.getDevelopmentCount(-1));
        assertEquals(0, paidR.getDevelopmentCount(-1));
        assertEquals(0, incurredR.getDevelopmentCount(-1));
        
        int accidents = PAID_LR.length;
        for(int a=0; a<accidents; a++)
            assertDevelopmentEquals(a, PAID_LR[a].length);
        
        assertEquals(0, paidF.getDevelopmentCount(accidents));
        assertEquals(0, incurredF.getDevelopmentCount(accidents));
        assertEquals(0, paidR.getDevelopmentCount(accidents));
        assertEquals(0, incurredR.getDevelopmentCount(accidents));
    }
    
    private void assertDevelopmentEquals(int accident, int developments) {
        assertEquals(developments-1, paidF.getDevelopmentCount(accident));
        assertEquals(developments-1, incurredF.getDevelopmentCount(accident));
        assertEquals(developments, paidR.getDevelopmentCount(accident));
        assertEquals(developments, incurredR.getDevelopmentCount(accident));
    }
    
    @Test
    public void testRecalculate() {
        data.recalculate();
        
        assertNaN(-1, 0);
        int accidents = PAID_LR.length;
        for(int a=0; a<accidents; a++) {
            assertNaN(a, -1);
            
            int devs = PAID_LR[a].length;
            for(int d=0; d<devs; d++)
                assertCellEquals(a, d);
            assertNaN(a, devs);
        }
        
        assertNaN(accidents, 0);
    }
    
    private void assertNaN(int accident, int development) {
        double found = paidF.getValue(accident, development);
        assertEquals(Double.NaN, found, TestConfig.EPSILON);
        
        found = paidR.getValue(accident, development);
        assertEquals(Double.NaN, found, TestConfig.EPSILON);
        
        found = incurredF.getValue(accident, development);
        assertEquals(Double.NaN, found, TestConfig.EPSILON);
        
        found = incurredR.getValue(accident, development);
        assertEquals(Double.NaN, found, TestConfig.EPSILON);
    }
    
    private void assertCellEquals(int accident, int development) {
        double expected = PAID_LR[accident][development];
        double found = paidF.getValue(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = PAID_CR[accident][development];
        found = paidR.getValue(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = INCURRED_LR[accident][development];
        found = incurredF.getValue(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = INCURRED_CR[accident][development];
        found = incurredR.getValue(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
    }
    
    @Test
    public void testCreatePseudoBundle() {
        MclEstimateInput bundle = data.createPseudoBundle();
        MclCorrelation c = bundle.getSourceIncurredCorrelation();
        assertSources(c, incurredR, incurredF);

        c = bundle.getSourcePaidCorrelation();
        assertSources(c, paidR, paidF);
    }
    
    private void assertSources(MclCorrelation c, RatioTriangle r, FactorTriangle f) {
        assertEquals(r, c.getSourceRatioTriangle());
        assertEquals(f, c.getSourceFactors());
    }
}
 