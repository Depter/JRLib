package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.mack.MackBootstrapEstimate;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
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
public class MackBootstrapEstimateTest {
    
    private ChainLadderEstimate cl;
    private MackBootstrapEstimate mack;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        
        cl = new ChainLadderEstimate(lrs);
        mack = new MackBootstrapEstimate(lrs, new DummyMackProcessSimulator());
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
                
                assertEquals(expected, mack.getValue(a, d), TestConfig.EPSILON);
            }
        }
    }
}
