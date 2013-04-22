package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.odp.residuals.DefaultOdpScaledResidualTriangle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.odp.scale.VariableOdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultOdpScaledResidualTriangleTest {
    
    private final static double[][] EXPECTED = {
        { 0.91039211,  0.61982147, -0.60325319, -1.67946738,  0.91744196,  2.80801559, -1.26926198, -0.53159210, -0.46838165, 0.00000000},
        {-0.21096165, -0.29376806, -0.25725088,  0.70470306, -0.95792840, -0.73011129,  1.35823137,  0.13534651,  0.39688532},
        {-0.72263810,  0.41684604, -0.24632709, -0.11679405,  1.24638103, -2.17602346,  1.11672838,  0.31670424},
        {-0.49939976,  1.09897451, -0.99435965,  2.87334992, -2.10648621, -0.38678500, -1.41154182},
        { 0.99321524, -0.85015573,  0.66015050, -0.93870453, -0.11096990,  0.94933309},
        { 0.38356779,  0.32100746, -0.42317640, -0.98734799,  1.16034961},
        { 0.42178077, -0.69988173,  0.58221193, -0.15422414},
        {-0.86636153, -0.53846159,  1.06254191},
        {-0.11965015,  0.07581597},
        { 0.00000000}
    };
    
    private final static double[][] EXPECTED_VSCALE = {
        { 1.49238135,  0.99917531, -0.90401471, -1.21084431,  0.74460096,  1.66602028, -0.98105779, -1.45362722, -1.07895216, Double.NaN},
        {-0.34582376, -0.47356507, -0.38550741,  0.50806923, -0.77745999, -0.43318143,  1.04982540,  0.37010214,  0.91425502},
        {-1.18460124,  0.67197136, -0.36913741, -0.08420491,  1.01156973, -1.29105381,  0.86315914,  0.86602096},
        {-0.81865262,  1.77158787, -1.49011356,  2.07159690, -1.70963585, -0.22948293, -1.09103095},
        { 1.62815109, -1.37048272,  0.98927910, -0.67677709, -0.09006378,  0.56324765},
        { 0.62877238,  0.51747599, -0.63415777, -0.71184753,  0.94174616},
        { 0.69141390, -1.12823543,  0.87248300, -0.11119086},
        {-1.42020321, -0.86802016,  1.59228917},
        {-0.19613928,  0.12221817},
        { 0.00000000}
    };
    
    private DefaultOdpScaledResidualTriangle residuals;
    private DefaultOdpScaledResidualTriangle vScale;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        residuals = new DefaultOdpScaledResidualTriangle(lrs);
        vScale = new DefaultOdpScaledResidualTriangle(new VariableOdpResidualScale(lrs));
    }
    
    @Test
    public void testRecalculate_Constant() {
        int accidents = EXPECTED.length;
        assertEquals(accidents, residuals.getAccidentCount());
        
        assertEquals(Double.NaN, residuals.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = EXPECTED[a].length;
            assertEquals(devs, residuals.getDevelopmentCount(a));
            assertEquals(Double.NaN, residuals.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], residuals.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, residuals.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getValue(accidents, 0), TestConfig.EPSILON);
    }
    
    @Test
    public void testRecalculate_Variable() {
        int accidents = EXPECTED_VSCALE.length;
        assertEquals(accidents, vScale.getAccidentCount());
        
        assertEquals(Double.NaN, vScale.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = EXPECTED_VSCALE[a].length;
            assertEquals(devs, vScale.getDevelopmentCount(a));
            assertEquals(Double.NaN, vScale.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED_VSCALE[a][d], vScale.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, vScale.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, vScale.getValue(accidents, 0), TestConfig.EPSILON);
    }
}
