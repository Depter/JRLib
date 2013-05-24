/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.grscript.util.PrintDelegate
import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapper
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclBootstrapDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = mclPaid()\n"                        +
        "paidData = cummulate(paidData)\n"              +
        ""                                              +
        "paidLr = linkRatio(paidData)\n"                +
        "paidLrScales = scale(paidLr) {\n"              +
        "    fixed(5, 0.1)\n"                           +
        "}\n"                                           +
        ""                                              +
        "paidLrRes = residuals(paidLrScales) {\n"       +
        "    adjust()\n"                                +
        "    center()\n"                                +
        "}\n"                                           +
        ""                                              +
        "incurredData = mclIncurred()\n"                +
        "incurredData = cummulate(incurredData)\n"      +
        ""                                              +
        "incurredLr = linkRatio(incurredData)\n"        +
        "incurredLrScales = scale(incurredLr) {\n"      +
        "    fixed(5, 0.1)\n"                           +
        "}\n"                                           +
        ""                                              +
        "incurredLrRes = residuals(incurredLrScales) {\n"       +
        "   adjust()\n"                                 +
        "   center()\n"                                 +
        "}\n"                                           +
        ""                                              +
        "pRatio = ratios(incurredLr, paidLr)\n"         +
        "pCrScale = scale(pRatio) {\n"                  +
        "   minMax(6)\n"                                +
        "}\n"                                           +
        ""                                              +
        "paidCrRes = residuals(pCrScale) {\n"           +
        "   adjust()\n"                                 +
        "   center()\n"                                 +
        "}\n"                                           +
        ""                                              +
        "iRatio = ratios(paidLr, incurredLr)\n"         +
        "iCrScale = scale(iRatio) {\n"                  +
        "   minMax(6)\n"                                +
        "}\n"                                           +
        ""                                              +
        "incurredCrRes = residuals(iCrScale) {\n"       +
        "   adjust()\n"                                 +
        "   center()\n"                                 +
        "}\n"                                           +
        ""                                              +
        "bootstrap = mclBootstrap {\n"                  +
        "   count 1000\n"                               +
        "   random \"Java\", 10\n"                      +
        "   residuals {\n"                              +
        "       paidLr paidLrRes\n"                     +
        "       paidCr paidCrRes\n"                     +
        "       incurredLr incurredLrRes\n"             +
        "       incurredCr incurredCrRes\n"             +
        "   }\n"                                        +
        "   process {\n"                                +
        "       paid \"Normal\"\n"                      +
        "       incurred \"Normal\"\n"                  +
        "   }\n"                                        +
        "   segment {\n"                                +
        "       cell(a:0, d:5)\n"                       +
        "   }\n"                                        +
        "}\n"                                           ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new PrintDelegate())
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LRResidualTriangleDelegate())
        
        executor.addFunctionProvider(new RatioTriangleDelegate())
        executor.addFunctionProvider(new ClaimRatioDelegate())
        executor.addFunctionProvider(new ClaimRatioScaleDelegate())
        executor.addFunctionProvider(new CRResidualTriangleDelegate())
        
        executor.addFunctionProvider(new MclBootstrapDelegate())
    }
    
    @Test
    public void testBootstrap() {
        def bs = executor.runScript(BASE_SCRIPT)
        def pLr = executor.getVariable("paidLrRes")
        assertTrue(bs instanceof MclBootstrapper)
    }
    
    @Test
    public void testWorking() {
        String script = 
        "bootstrap.run()\n"+
        "bootstrap.getPaidReserves()\n";
        
        double[][] res = (double[][]) executor.runScript(BASE_SCRIPT + script)
        double mean = BootstrapUtil.getMeanTotalReserve(res)
        assertEquals(1000, res.length)
    }
}

