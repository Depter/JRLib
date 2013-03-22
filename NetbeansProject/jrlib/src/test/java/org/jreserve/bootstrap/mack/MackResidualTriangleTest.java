package org.jreserve.bootstrap.mack;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MackResidualTriangleTest {

    private final static double[][] EXPECTED = {
        {-0.51909471, -1.11661064, -1.15179795,  0.77198984,  1.49016632, -0.84967367, -1.18935563, -0.75943607, 0.00000000},
        { 0.02960695,  0.04670296,  0.63189598, -0.60820819, -0.32152712,  0.93859206,  0.34615404,  0.65058194},
        { 1.28953441, -0.17915491,  0.00613582,  0.84995471, -1.14091105,  0.75814702,  0.68235663},
        { 1.49982777, -1.22807744,  1.83969400, -1.59412945, -0.28204991, -0.90681458},
        {-1.54043261,  0.68935341, -0.68272896,  0.00505286,  0.54294980},
        {-0.19654194, -0.66365231, -0.63617093,  0.87773415},
        {-0.94166879,  0.76409783, -0.13683939},
        { 0.69291414,  1.64658460},
        { 0.19710471}
    };
    
    private ClaimTriangle claims;
    private LinkRatioScale scales;
    private MackResidualTriangle residuals;
    
    public MackResidualTriangleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        claims = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(claims);
        scales = new SimpleLinkRatioScale(lrs);
        residuals = new MackResidualTriangle(scales);
    }

    @Test
    public void testGetSourceLinkRatioScale() {
        assertTrue(scales == residuals.getSourceLinkRatioScale());
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(scales.getSourceFactors().getAccidentCount(), residuals.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(scales.getSourceFactors().getDevelopmentCount(), residuals.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        assertEquals(0, residuals.getDevelopmentCount(-1));
        FactorTriangle factors = scales.getSourceFactors();
        int accidents = factors.getAccidentCount();
        for(int a=0; a<accidents; a++)
            assertEquals(factors.getDevelopmentCount(a), residuals.getDevelopmentCount(a));
        assertEquals(0, residuals.getDevelopmentCount(accidents));
    }

    @Test
    public void testGetValue() {
        int accidents = scales.getSourceFactors().getAccidentCount();
        assertEquals(Double.NaN, residuals.getValue(-1, 0), JRLibTestUtl.EPSILON);
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, residuals.getValue(a, -1), JRLibTestUtl.EPSILON);
            int devs = EXPECTED[a].length;
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], residuals.getValue(a, d), JRLibTestUtl.EPSILON);
            assertEquals(Double.NaN, residuals.getValue(a, devs), JRLibTestUtl.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getValue(accidents, 0), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetWeight() {
        LinkRatio lrs = scales.getSourceLinkRatios();
        
        int accidents = scales.getSourceFactors().getAccidentCount();
        assertEquals(Double.NaN, residuals.getWeight(-1, 0), JRLibTestUtl.EPSILON);
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, residuals.getWeight(a, -1), JRLibTestUtl.EPSILON);
            int devs = EXPECTED[a].length;
            for(int d=0; d<devs; d++)
                assertEquals(lrs.getWeight(a, d), residuals.getWeight(a, d), JRLibTestUtl.EPSILON);
            assertEquals(Double.NaN, residuals.getWeight(a, devs), JRLibTestUtl.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getWeight(accidents, 0), JRLibTestUtl.EPSILON);
    }
}