package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputOdpResidualTriangleTest {

    private final static double[][] EXPECTED = {
        { 168.92614903,  115.00984419, -111.93554665, -311.63050980,  170.23427095,  521.03621853, -235.51559474, -98.63860460, -86.90970328, 0.00000000},
        {- 39.14460379, - 54.50959780, - 47.73371771,  130.75989274, -177.74666033, -135.47447026,  252.02414823,  25.11397582,  73.64333254},
        {-134.08779552,   77.34710799, - 45.70677534, - 21.67150640,  231.26996153, -403.76806908,  207.21250111,  58.76547846},
        {- 92.66521256,  203.91821353, -184.50659401,  533.15920990, -390.86521256, - 71.76918593, -261.91607168},
        { 184.29424480, -157.74909716,  122.49302425, -174.17960950, - 20.59081787,  176.15177190},
        {  71.17222180,   59.56395431, - 78.52172676, -183.20555856,  215.30655828},
        {  78.26276302, -129.86527959,  108.03127393, - 28.61677888},
        {-160.75613634, - 99.91326047,  197.15802834},
        {- 22.20146585,   14.06789399},
        {   0.00000000}
    };
    
    private ClaimTriangle cik;
    private LinkRatio lrs;
    private InputOdpResidualTriangle residuals;
    
    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        lrs = new SimpleLinkRatio(cik);
        residuals = new InputOdpResidualTriangle(lrs);
    }
    
    @Test
    public void testGetSourceLinkRatios() {
        assertTrue(lrs == residuals.getSourceLinkRatios());
    }

    @Test
    public void testGetSourceTriangle() {
        assertTrue(cik == residuals.getSourceTriangle());
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(cik.getAccidentCount(), residuals.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(cik.getDevelopmentCount(), residuals.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        assertEquals(0, residuals.getDevelopmentCount(-1));
        int accidents = cik.getAccidentCount();
        for(int a=0; a<accidents; a++)
            assertEquals(cik.getDevelopmentCount(a), residuals.getDevelopmentCount(a));
        assertEquals(0, residuals.getDevelopmentCount(accidents));
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
