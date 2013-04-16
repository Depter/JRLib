package org.jrlib.bootstrap.odp;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.bootstrap.FixedRandom;
import org.jrlib.bootstrap.odp.residuals.DefaultOdpScaledResidualTriangle;
import org.jrlib.bootstrap.odp.residuals.OdpScaledResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpPseudoClaimTriangleTest {
        
    private final static double[][] EXPECTED = {
        {277226.29694048,  957541.41331594, 1654367.46619246, 2385727.99096739, 2811985.08072443, 3109632.94395168, 3373243.91671246, 3544426.88957824, 3824231.46856785, 3894626.30085253},
        {370522.07909307, 1282684.23541253, 2277517.42934884, 3336474.95820314, 3910769.50403581, 4302008.20101951, 4684169.27113636, 4942422.39311270, 5316462.10962537},
        {356805.65912670, 1297398.27170445, 2277913.46418573, 3307343.63898643, 3863438.32760201, 4275552.68373157, 4651218.99424183, 4897607.83042829},
        {351321.48300805, 1277863.06529811, 2243696.45369087, 3257569.83760882, 3805153.91735982, 4211133.93414459, 4581191.40664011},
        {330989.34836172, 1145271.49628366, 2035438.68377134, 2982730.36449775, 3495839.23693574, 3844803.64104242},
        {361998.88671186, 1251982.17854628, 2166138.79049338, 3127922.30454141, 3684872.98577612},
        {397717.51346078, 1364615.71258363, 2361076.36098281, 3468681.19351854},
        {476080.34807360, 1635906.84491017, 2832897.80849804},
        {399177.09981233, 1381168.22979079},
        {338655.57778070}    
    };
    
    private OdpPseudoClaimTriangle claims;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        OdpScaledResidualTriangle residuals = new DefaultOdpScaledResidualTriangle(lrs);
        claims = new OdpPseudoClaimTriangle(new FixedRandom(), residuals);
        claims.recalculate();
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(EXPECTED.length, claims.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(EXPECTED[0].length, claims.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = EXPECTED.length;
        assertEquals(0, claims.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(EXPECTED[a].length, claims.getDevelopmentCount(a));
        assertEquals(0, claims.getDevelopmentCount(accidents));
    }
    
    @Test
    public void testRecalculate() {
        int accidents = EXPECTED.length;
        
        assertEquals(Double.NaN, claims.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = EXPECTED[a].length;
            assertEquals(Double.NaN, claims.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
               assertEquals(EXPECTED[a][d], claims.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, claims.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, claims.getValue(accidents, 0), TestConfig.EPSILON);
    }
}
