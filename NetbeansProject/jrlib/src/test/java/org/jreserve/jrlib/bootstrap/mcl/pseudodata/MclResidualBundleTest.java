package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclResidualBundleTest {

    private final static double[][] PAID_LR = {
        {-0.53544715,  0.07687310, -0.88318295, -0.32853221,  0.75165510,  0.70651452, 0.00000000},
        {-0.53148094, -0.62744360, -0.37511590,  0.55345506,  0.46949846, -0.70769855},
        {-0.31361544, -0.61993017,  0.99245419,  1.03628787, -1.10208249},
        {-0.86407166,  1.51326125, -0.97735913, -1.22957779},
        { 2.13203521,  0.80608679,  1.06727679},
        {-0.15558769, -1.12975932},
        { 0.12676049}
    };
    
    private final static double[][] PAID_CR = {
        {-1.33089143, -1.80917390, -1.72446110, -1.73477463, -1.34141175, -0.95840097, -0.70536960},
        {-0.66745059, -0.57102319,  0.05194833,  0.70711037,  1.03845917,  1.03757548},
        {-1.18758856, -0.29273228,  0.21038152,  0.00328244, -0.06770761},
        {-0.21723010,  0.31651779, -0.40077902,  0.21503292},
        { 0.14074161, -0.09213231,  0.21948460},
        { 0.20104705,  0.66479279},
        { 1.47963872}
    };
    
    private final static double[][] INCURRED_LR = {
        {-0.38904405, -0.42505847,  1.12758752,  0.46441317,  1.11987853,  0.71993049, 0.00000000},
        {-0.12573874,  0.78019786,  0.22295750,  1.10340296, -0.14878945, -0.69404617},
        { 1.55096963,  0.43528490, -1.59492232, -0.16337761, -0.85072545},
        {-0.21747671, -1.86397872,  0.36381681, -1.24101977},
        { 1.01553275,  0.38969630,  0.05192759},
        {-0.06076649,  0.62838186},
        {-1.53134981}
    };
    
    private final static double[][] INCURRED_CR = {
        { 1.39958504,  1.86536889,  1.77570549,  1.75243837,  1.36352406,  0.97804564, 0.71982972},
        { 0.68114843,  0.56811963, -0.05077841, -0.68370632, -1.01089507, -1.01906564},
        { 1.23488527,  0.28878655, -0.20484734, -0.00321301,  0.06721063},
        { 0.21719074, -0.30762038,  0.39634032, -0.20978264},
        {-0.13884318,  0.09042502, -0.21374329},
        {-0.19801119, -0.64177892},
        {-1.39841391}
    };
    
    private MclResidualBundle bundle;
    
    @Before
    public void setUp() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle paidLR = createLRResiduals(paid);
        CRResidualTriangle paidCR = createCRResiduals(incurred, paid);
        LRResidualTriangle incurredLR = createLRResiduals(incurred);
        CRResidualTriangle incurredCR = createCRResiduals(paid, incurred);
        
        bundle = new MclResidualBundle(paidLR, paidCR, incurredLR, incurredCR);
    }
    
    private LRResidualTriangle createLRResiduals(ClaimTriangle cik) {
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        return new LinkRatioResiduals(scales);
    }
    
    private CRResidualTriangle createCRResiduals(ClaimTriangle numerator, ClaimTriangle denominator) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denominator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        return new ClaimRatioResiduals(scales);
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(PAID_LR.length, bundle.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(PAID_LR[0].length, bundle.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = bundle.getAccidentCount();
        assertEquals(0, bundle.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(PAID_LR[a].length, bundle.getDevelopmentCount(a));
        assertEquals(0, bundle.getDevelopmentCount(accidents));
    }
    
    @Test
    public void testRecalculate() {
        int accidents = bundle.getAccidentCount();
        assertNaN(-1, 0);
        for(int a=0; a<accidents; a++) {
            assertNaN(a, -1);
            int devs = bundle.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertNonNaN(a, d);
            assertNaN(a, devs);
        }
        assertNaN(accidents, 0);
    }
    
    private void assertNaN(int accident, int development) {
        assertEquals(Double.NaN, bundle.getPaidLRResidual(accident, development), TestConfig.EPSILON);
        assertEquals(Double.NaN, bundle.getPaidCRResidual(accident, development), TestConfig.EPSILON);
        assertEquals(Double.NaN, bundle.getIncurredLRResidual(accident, development), TestConfig.EPSILON);
        assertEquals(Double.NaN, bundle.getIncurredCRResidual(accident, development), TestConfig.EPSILON);
    }
    
    private void assertNonNaN(int accident, int development) {
        double expected = PAID_LR[accident][development];
        double found = bundle.getPaidLRResidual(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = PAID_CR[accident][development];
        found = bundle.getPaidCRResidual(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = INCURRED_LR[accident][development];
        found = bundle.getIncurredLRResidual(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
        
        expected = INCURRED_CR[accident][development];
        found = bundle.getIncurredCRResidual(accident, development);
        assertEquals(expected, found, TestConfig.EPSILON);
    }
}