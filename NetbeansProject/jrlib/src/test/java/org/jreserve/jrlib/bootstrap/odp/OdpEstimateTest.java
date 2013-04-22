package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.bootstrap.odp.OdpEstimate;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.DummyProcessSimulator;
import org.jreserve.jrlib.estimate.ChainLadderEstimate;
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
public class OdpEstimateTest {
    
    private ChainLadderEstimate cl;
    private OdpEstimate odp;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        
        cl = new ChainLadderEstimate(lrs);
        odp = new OdpEstimate(lrs, new DummyProcessSimulator());
    }
    
    @Test
    public void testRecalculate() {
        int accidents = cl.getAccidentCount();
        int developments = cl.getDevelopmentCount();
        
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                double expected = cl.getValue(a, d);
                if(d >= cl.getObservedDevelopmentCount(a))
                    expected += 1d;
                
                assertEquals(expected, odp.getValue(a, d), TestConfig.EPSILON);
            }
        }
    }
}
