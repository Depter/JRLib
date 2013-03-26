package org.jreserve.bootstrap.mack;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.FixedProcessSimulator;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.util.MathUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MackBootstrapperTest {

    private final static int N = 1;
    
    private MackBootstrapper bootstrap;
    private ChainLadderEstimate estimate;

    public MackBootstrapperTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        estimate = new ChainLadderEstimate(lrs);
        estimate.detach();
        bootstrap = new MackBootstrapper(estimate, N, new FixedProcessSimulator());
    }

    @Test
    public void testCalculatePseudoReserve() {
        double[][] reserves = new double[N][];
        for(int i=0; i<N; i++)
            reserves[i] = bootstrap.calculatePseudoReserve();
        
        double expected = estimate.getReserve();
        for(int i=0; i<N; i++) {
            double found = MathUtil.sum(reserves[i], false);
            assertEquals(expected, found, JRLibTestUtl.EPSILON);
        }
    }

}