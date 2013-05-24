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
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MackBootstrapDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                        +
        "paidData = cummulate(paidData)\n"              +
        "paidTriangle = triangle(paidData)\n"           +
        ""                                              +
        "paidLr = linkRatio(paidData)\n"                +
        "paidLr = smooth(paidLr, 10, \"exponential\")\n"+
        "paidLrScales = scale(paidLr)\n"                +
        ""                                              +
        "paidLrRes = residuals(paidLrScales) {\n"       +
        "    exclude(accident:0, development:6)\n"      +
        "    exclude(accident:6, development:0)\n"      +
        "    adjust()\n"                                +
        "}\n"                                           +
        ""                                              +
        "bootstrap = mackBootstrap {\n"                 +
        "   count 1000\n"                               +
        "   random \"Java\", 10\n"                      +
        "   residuals paidLrRes\n"                      +
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
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LRResidualTriangleDelegate())
        executor.addFunctionProvider(new MackBootstrapDelegate())
    }
    
    @Test
    public void testBootstrap() {
        def bs = executor.runScript(BASE_SCRIPT)
        assertTrue(bs instanceof EstimateBootstrapper)
    }
    
    @Test
    public void testWorking() {
        String script = 
        "bootstrap.run()\n"+
        "bootstrap.getReserves()\n";
        
        double[][] res = (double[][]) executor.runScript(BASE_SCRIPT + script)
        def paidLrRes = executor.getVariable("paidLrRes")
        double mean = BootstrapUtil.getMeanTotalReserve(res)
        assertEquals(1000, res.length)
    }
}

