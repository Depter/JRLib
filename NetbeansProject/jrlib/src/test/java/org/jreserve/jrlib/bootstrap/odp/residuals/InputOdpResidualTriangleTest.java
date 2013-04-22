package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.odp.residuals.InputOdpResidualTriangle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
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
    
    private final static double[][] FITTED = {
        {270061.41564463,  672616.73014839,  704494.14934589,  753437.75093016, 417350.16011141, 292570.58232966, 268343.51418461, 182034.67538945, 272606.02191581, 67948.00000000},
        {376125.00625328,  936779.40341548,  981176.32130314, 1049341.97320864, 581259.75236784, 407474.39557626, 373732.41827156, 253526.75151961, 379668.97808419},
        {372325.31550378,  927315.86875467,  971264.28001447, 1038741.30874239, 575387.74899820, 403358.00696666, 369956.89792890, 250965.57309094},
        {366723.95609649,  913365.08633754,  956652.32620611, 1023114.21286900, 566731.46524180, 397289.78363414, 364391.16961493},
        {336287.25224512,  837559.23242786,  877253.79481104,  938199.59579658, 519694.89322610, 364316.23149329},
        {353798.10072748,  881171.86631788,  922933.36838677,  987052.68451322, 546755.98005465},
        {391841.65717152,  975923.39710402, 1022175.47178445, 1093189.47394001},
        {469647.52095104, 1169707.19090082, 1225143.28814814},
        {390560.77540666,  972733.22459334},
        {344014.00000000}
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
    public void testRecalculate_Residuals() {
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
    
    @Test
    public void testRecalculate_Fitted() {
        assertEquals(Double.NaN, residuals.getFittedValue(-1, 0), TestConfig.EPSILON);
        int accidents = FITTED.length;
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, residuals.getFittedValue(a, -1), TestConfig.EPSILON);
            int devs = FITTED[a].length;
            for(int d=0; d<devs; d++)
                assertEquals(FITTED[a][d], residuals.getFittedValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, residuals.getFittedValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getFittedValue(accidents, 0), TestConfig.EPSILON);
    }
}
