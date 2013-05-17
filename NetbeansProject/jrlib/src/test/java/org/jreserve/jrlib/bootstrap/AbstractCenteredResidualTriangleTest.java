package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCenteredResidualTriangleTest {
    
    private final static double MEAN = 0.01316671;
    
    private final static double[][] EXPECTED = {
        {-0.56374980, -1.20687369, -1.25725068,  0.83250578,  1.65288988, -0.99428536, -1.46982392, -1.08717150, -0.01316671},
        { 0.01823620,  0.03676086,  0.66935896, -0.67942540, -0.37264496,  1.07062605,  0.41078368,  0.90689509},
        { 1.35459108, -0.20469136, -0.00653927,  0.91791202, -1.28874404,  0.86226606,  0.82254607},
        { 1.57764087, -1.32603672,  1.97392991, -1.75944803, -0.32850810, -1.06026600},
        {-1.64704223,  0.72378308, -0.75059827, -0.00763158,  0.59386962},
        {-0.22163092, -0.72264087, -0.70030985,  0.94834288},
        {-1.01195729,  0.80368823, -0.16097015},
        { 0.72177972,  1.74710627},
        { 0.19589441},
    };
    
    private AbstractCenteredResidualTriangle residuals;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        residuals = new AbstractCenteredResidualTriangle(new AdjustedLinkRatioResiduals(scales));
    }
    
    @Test
    public void testGetMean() {
        assertEquals(MEAN, residuals.getMean(), TestConfig.EPSILON);
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