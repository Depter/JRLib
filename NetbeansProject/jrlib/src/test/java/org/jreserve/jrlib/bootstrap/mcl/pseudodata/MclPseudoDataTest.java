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
import org.jreserve.jrlib.claimratio.scale.residuals.CenteredClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
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
        {1.222573822, 1.016509107, 1.001614612, 1.006643418, 1.002468533, 1.003763071, 1.000188673},
        {1.230970667, 1.011601285, 1.010181589, 1.001890861, 1.002818739, 1.000721752},
        {1.250437383, 1.009240123, 1.006730299, 1.005511630, 1.003622351},
        {1.209280583, 1.013348753, 1.005335016, 1.010169798},
        {1.209844351, 1.013493899, 1.005565084},
        {1.250070204, 1.009950773},
        {1.233921256}
    };
    
    private final static double[][] PAID_CR = {
        {1.096145803, 1.050456708, 1.017050830, 1.026791300, 1.039443564, 1.009039351, 0.970571839, Double.NaN},
        {1.042028657, 1.083701525, 1.012104518, 1.028398639, 1.018928425, 1.031143930, Double.NaN},
        {1.057965470, 1.064872213, 1.024183022, 1.051702202, 1.025771508, Double.NaN},
        {1.066770210, 1.062707343, 1.055163367, 1.033514800, Double.NaN},
        {1.069335467, 1.069010756, 1.059544295, Double.NaN},
        {1.076903814, 1.076309844, Double.NaN},
        {1.078255416, Double.NaN},
        {Double.NaN}
    };
    
    private final static double[][] INCURRED_LR = {
        {1.172030119, 0.983871008, 1.012025068, 1.015131713, 0.999381847, 1.000110563, 0.987972858},
        {1.219760034, 0.985255564, 0.988582216, 1.021732802, 1.007156875, 1.000136316},
        {1.172097521, 1.020509934, 1.001112290, 1.006337985, 0.999489558},
        {1.248156490, 1.005529760, 0.989735849, 1.005991548},
        {1.246656020, 1.005035626, 0.990110565},
        {1.175544639, 1.017329743},
        {1.213625917}
    };
    
    private final static double[][] INCURRED_CR = {
        {0.910373342, 0.950330085, 0.981364576, 0.972774293, 0.962946792, 0.988255456, 1.022462985, Double.NaN},
        {0.940224701, 0.922428249, 0.980494558, 0.969883105, 0.976105533, 0.967818780, Double.NaN},
        {0.934423884, 0.934995827, 0.968515470, 0.951367901, 0.972793835, Double.NaN},
        {0.923389192, 0.933841480, 0.947453222, 0.966145431, Double.NaN},
        {0.920139563, 0.929935482, 0.942864140, Double.NaN},
        {0.915107731, 0.924711979, Double.NaN},
        {0.907750133, Double.NaN},
        {Double.NaN},
    };
    
    private final static double PAID_LAMBDA =     0.25038130;
    private final static double INCURRED_LAMBDA = 0.12928930;
    
    private final static double[] PAID_LR_SCALE = {
        40.46646639, 6.66739124, 7.21581945, 8.42345536, 
         1.48140652, 5.07876309, 1.48140652
    };
    
    private final static double[] INCURRED_LR_SCALE = {
        86.47283684, 40.61146537, 25.38690835, 19.53756799, 
        11.26495390, 0.044876680, 0.00017878
    };
    
    private final static double[] PAID_CR_SCALE = {
        36.62101644, 27.61167624, 56.36355726, 28.74532478, 
        24.67800645, 36.91290868, 24.67800645, 16.49840189
    };
    
    private final static double[] INCURRED_CR_SCALE = {
        30.02025726, 25.04911906, 48.30719762, 24.90724891, 
        16.78107963, 35.61313638, 16.78107963, 7.90732471
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
        //data.recalculate();
        
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
        LRResidualTriangle res = new LinkRatioResiduals(scales);
        res = new LRResidualTriangleCorrection(res, 0, 6, Double.NaN);
        res = new AdjustedLinkRatioResiduals(res);
        return new CenteredLinkRatioResiduals(res);
    }
    
    private CRResidualTriangle createCRResiduals(ClaimTriangle numerator, ClaimTriangle denominator) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denominator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle res = new ClaimRatioResiduals(scales);
        res = new ClaimRatioResidualTriangleCorrection(res, 0, 7, Double.NaN);
        res = new AdjustedClaimRatioResiduals(res);
        return new CenteredClaimRatioResiduals(res);
    }
    
    @Test
    public void testAccidentCounts() {
        int accidents = PAID_LR.length;
        assertEquals(accidents, paidF.getAccidentCount());
        assertEquals(accidents, incurredF.getAccidentCount());
        assertEquals(accidents+1, paidR.getAccidentCount());
        assertEquals(accidents+1, incurredR.getAccidentCount());
    }
    
    @Test
    public void testDevelopmentCounts() {
        int developments = PAID_LR[0].length;
        assertEquals(developments, paidF.getDevelopmentCount());
        assertEquals(developments, incurredF.getDevelopmentCount());
        assertEquals(developments+1, paidR.getDevelopmentCount());
        assertEquals(developments+1, incurredR.getDevelopmentCount());
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
        assertEquals(1, paidR.getDevelopmentCount(accidents));
        assertEquals(1, incurredR.getDevelopmentCount(accidents));
        assertEquals(0, paidR.getDevelopmentCount(accidents+1));
        assertEquals(0, incurredR.getDevelopmentCount(accidents+1));
    }
    
    private void assertDevelopmentEquals(int accident, int developments) {
        assertEquals(developments, paidF.getDevelopmentCount(accident));
        assertEquals(developments, incurredF.getDevelopmentCount(accident));
        assertEquals(developments+1, paidR.getDevelopmentCount(accident));
        assertEquals(developments+1, incurredR.getDevelopmentCount(accident));
    }
    
    @Test
    public void testRecalculateLambda() {
        data.recalculate();
        assertEquals(PAID_LAMBDA, data.getPaidLambda(), TestConfig.EPSILON);
        assertEquals(INCURRED_LAMBDA, data.getIncurredLambda(), TestConfig.EPSILON);
    }
    
    @Test
    public void testRecalculateLRScale() {
        LinkRatioScale p = data.getPaidLinkRatioScale();
        LinkRatioScale i = data.getIncurredLinkRatioScale();
        data.recalculate();
        
        int length = PAID_LR_SCALE.length;
        assertEquals(Double.NaN, p.getValue(-1), TestConfig.EPSILON);
        assertEquals(Double.NaN, i.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++) {
            assertEquals(PAID_LR_SCALE[d], p.getValue(d), TestConfig.EPSILON);
            assertEquals(INCURRED_LR_SCALE[d], i.getValue(d), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, p.getValue(length), TestConfig.EPSILON);
        assertEquals(Double.NaN, i.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testRecalculateCRScale() {
        ClaimRatioScale p = data.getPaidClaimRatioScale();
        ClaimRatioScale i = data.getIncurredClaimRatioScale();
        data.recalculate();
        
        int length = PAID_CR_SCALE.length;
        assertEquals(Double.NaN, p.getValue(-1), TestConfig.EPSILON);
        assertEquals(Double.NaN, i.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++) {
            assertEquals(PAID_CR_SCALE[d], p.getValue(d), TestConfig.EPSILON);
            assertEquals(INCURRED_CR_SCALE[d], i.getValue(d), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, p.getValue(length), TestConfig.EPSILON);
        assertEquals(Double.NaN, i.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testRecalculateTriangles() {
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
    
}
 