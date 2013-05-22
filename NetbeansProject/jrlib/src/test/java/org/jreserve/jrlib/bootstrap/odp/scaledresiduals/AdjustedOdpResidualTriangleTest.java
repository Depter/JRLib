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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedOdpResidualTriangleTest {
    private final static double[][] EXPECTED = {
        { 208.79830848,  142.15597208, -138.35603862, -385.18561921,  210.41519045,  644.01800260, -291.10506623, -121.92057837, -107.42326833, 0.00000000},
        {- 48.38402523, - 67.37566613, - 59.00045418,  161.62355312, -219.70075224, -167.45092690,  311.51018447,   31.04170492,   91.02559523},
        {-165.73695105,   95.60358421, - 56.49508635, - 26.78669883,  285.85732314, -499.07069051,  256.12150621,   72.63607540},
        {-114.53726820,  252.04965778, -228.05625389,  659.00242098, -483.12233300, - 88.70908802, -323.73692909},
        { 227.79378324, -194.98310262,  151.40543019, -215.29175941, - 25.45093205,  217.72941738},
        {  87.97122061,   73.62301798, - 97.05545186, -226.44813104,  266.12602863},
        {  96.73536412, -160.51778167,  133.53022838, -35.37128538},
        {-198.69990252, -123.49609518,  243.69384525},
        {- 27.44174624,   17.38838235},
        {   0.00000000}
    };
    
    private OdpScaledResidualTriangle residuals;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        OdpResidualTriangle res = new InputOdpResidualTriangle(lrs);
        OdpResidualScale scales = new EmptyOdpResidualScale(res);
        residuals = new AdjustedOdpScaledResidualTriangle(scales);
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
