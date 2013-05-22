package org.jreserve.jrlib.bootstrap.odp.scaledresiduals;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.odp.residuals.InputOdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scale.EmptyOdpResidualScale;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CenteredOdpScaledResidualTriangleTest {
    private final static double[][] EXPECTED = {
        { 168.32384219,  114.40753735, -112.53785349, -312.23281664,  169.63196411,  520.43391169, -236.11790158, -99.24091144, -87.51201012, -0.60230684},
        {- 39.74691063, - 55.11190464, - 48.33602455,  130.15758590, -178.34896717, -136.07677710,  251.42184139,  24.51166898,  73.04102570},
        {-134.69010236,   76.74480115, - 46.30908218, -22.273813240,  230.66765469, -404.37037592,  206.61019427,  58.16317162},
        {- 93.26751940,  203.31590669, -185.10890085,  532.55690306, -391.46751940, - 72.37149277, -262.51837852},
        { 183.69193796, -158.35140400,  121.89071741, -174.78191634, - 21.19312471,  175.54946506},
        {  70.56991496,   58.96164747, - 79.12403360, -183.80786540,  214.70425144},
        {  77.66045618, -130.46758643,  107.42896709, - 29.21908572},
        {-161.35844318, -100.51556730,  196.55572150},
        {- 22.80377269,   13.46558715},
        {-  0.60230684}
    };
    
    private OdpScaledResidualTriangle residuals;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        OdpResidualTriangle res = new InputOdpResidualTriangle(lrs);
        OdpResidualScale scales = new EmptyOdpResidualScale(res);
        residuals = new CenteredOdpScaledResidualTriangle(scales);
    }
    
    @Test
    public void testRecalculate() {
        assertEquals(Double.NaN, residuals.getValue(-1, 0), TestConfig.EPSILON);
        int accidents = EXPECTED.length;
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, residuals.getValue(a, -1), TestConfig.EPSILON);
            int devs = EXPECTED[a].length;
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], residuals.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, residuals.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getValue(accidents, 0), TestConfig.EPSILON);
    }    
}
