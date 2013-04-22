package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.bootstrap.odp.scale.VariableOdpResidualScale;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
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
public class VariableOdpResidualScaleTest {
    
    private final static double[] EXPECTED = {
        139.90948681, 142.27330302, 153.04622417, 318.11324978, 282.58785767, 
        386.56072129, 296.72570659,  83.87334564,  99.56258727,   0.00000000
    };
    
    private OdpResidualTriangle residuals;
    private OdpResidualScale scales;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        residuals = new AdjustedOdpResidualTriangle(lrs);
        scales = new VariableOdpResidualScale(residuals);
    }
    
    @Test
    public void testRecalculate() {
        int devs = EXPECTED.length;
        assertEquals(devs, scales.getLength());
        
        assertEquals(Double.NaN, scales.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<10; d++)
            assertEquals(EXPECTED[d], scales.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, scales.getValue(devs), TestConfig.EPSILON);
    }

}
