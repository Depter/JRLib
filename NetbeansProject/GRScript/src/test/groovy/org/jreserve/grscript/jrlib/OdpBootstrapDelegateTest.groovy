package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OdpBootstrapDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                        +
        "paidData = cummulate(paidData)\n"              +
        "paidTriangle = triangle(paidData)\n"           +
        "paidLr = linkRatio(paidData)\n"                +
        "paidLr = smooth(paidLr, 10, \"exponential\")\n"+
        "paidResScale = variableScale(paidLr) {\n"      +
        "   fixed(7:172)\n"                             +
        "}\n"                                           +
        "paidRes = residuals(paidResScale) {\n"         +
        "   exclude(0, 7)\n"                            +
        "   exclude(7, 0)\n"                            +
        "}\n"                                           +
        "\n"                                            +
        "bootstrap = odpBootstrap {\n"                  +
        "   count 1000\n"                               +
        "   random \"Java\", 10\n"                      +
        "   residuals paidRes\n"                        +
        "   process \"Gamma\"\n"                        +
        "   segment {\n"                                +
        "       from(accident:0, development:0)\n"      +
        "       to(a:8, d:2)\n"                         +
        "   }\n"                                        +
        "}\n"                                           ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
        executor.addFunctionProvider(new ClaimResidualScaleDelegate())
        executor.addFunctionProvider(new ScaledClaimResidualDelegate())
        executor.addFunctionProvider(new OdpBootstrapDelegate())
    }
    
    @Test
    public void testBootstrap() {
        def bs = runScript("")
        assertTrue(bs instanceof EstimateBootstrapper<OdpEstimate>)
    }
    
    private def runScript(String script) {
        script = BASE_SCRIPT + script
        return executor.runScript(script)
    }	
    
    @Test
    public void testWorking() {
        String script = 
        "bootstrap.run()\n"+
        "bootstrap.getReserves()\n";
        
        double[][] res = (double[][]) executor.runScript(BASE_SCRIPT + script)
        def paidResScale = executor.getVariable("paidResScale")
        double mean = BootstrapUtil.getMeanTotalReserve(res)
        assertEquals(1000, res.length)
    }
}

