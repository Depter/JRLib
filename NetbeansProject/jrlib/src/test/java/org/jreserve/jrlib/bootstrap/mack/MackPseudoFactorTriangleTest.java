package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.mack.MackPseudoFactorTriangle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.FixedRandom;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
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
public class MackPseudoFactorTriangleTest {
    
    private final static double[][] EXPECTED = {
        {3.14320047, 1.54280629, 1.27829865, 1.23771948, 1.06711352, 1.03082430, 1.04080036, 1.09032513, 1.01212179},
        {2.73725487, 1.54608746, 1.56476805, 1.13892269, 1.03669309, 1.03492956, 1.06144185, 1.06862326},
        {2.66120743, 1.55050936, 1.56358829, 1.13829064, 1.03828607, 1.03500947, 1.06145733},
        {3.11771684, 1.56523046, 1.29815563, 1.22292470, 1.07352273, 1.03800837},
        {2.79792099, 1.88801459, 1.38452223, 1.09302714, 1.03065532},
        {3.98166335, 1.65999951, 1.30251440, 1.09171698},
        {3.95610167, 1.65849572, 1.31036750},
        {2.72151322, 1.87313183},
        {3.15199873}
    };
    
    private MackPseudoFactorTriangle factors;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        LRResidualTriangle residuals = new LinkRatioResiduals(scales);
        factors = new MackPseudoFactorTriangle(new FixedRandom(), residuals);
        factors.recalculate();
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(EXPECTED.length, factors.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(EXPECTED[0].length, factors.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = EXPECTED.length;
        assertEquals(0, factors.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(EXPECTED[a].length, factors.getDevelopmentCount(a));
        assertEquals(0, factors.getDevelopmentCount(accidents));
    }
    
    @Test
    public void testRecalculate() {
        int accidents = EXPECTED.length;
        
        assertEquals(Double.NaN, factors.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = EXPECTED[a].length;
            assertEquals(Double.NaN, factors.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
               assertEquals(EXPECTED[a][d], factors.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, factors.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, factors.getValue(accidents, 0), TestConfig.EPSILON);
    }
}
