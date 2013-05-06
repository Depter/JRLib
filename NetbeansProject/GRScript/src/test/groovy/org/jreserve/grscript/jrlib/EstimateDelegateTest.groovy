package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.estimate.ChainLadderEstimate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EstimateDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                +
        "cummulate(paidData)\n"                 +
        "paid = triangle(paidData)\n"           +
        "exposure = vector(apcExposure())\n"    +
        "lrs = smooth(linkRatio(paid), 10)\n"   +
        "scale = scale(lrs)\n"                  +
        "lrSE = standardError(scale)\n"         ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new VectorDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LinkRatioSEDelegate())
        executor.addFunctionProvider(new EstimateDelegate())
    }
    
    @Test
    public void testChainLadder() {
        Estimate estimate = runScript("estimate = chainLadderEstimate(lrs)\n")
        
        assertTrue(estimate instanceof ChainLadderEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    private Estimate runScript(String script) {
        script = BASE_SCRIPT + script
        return (Estimate) executor.runScript(script)
    }
	
}

