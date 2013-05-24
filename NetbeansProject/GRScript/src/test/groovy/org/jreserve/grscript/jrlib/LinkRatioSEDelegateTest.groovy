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
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSE
import org.jreserve.jrlib.linkratio.standarderror.SimpleLinkRatioSE

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioSEDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"        +
        "cummulate(data)\n"         +
        "lrs = linkRatio(data)\n"   +
        "scales = scale(lrs)\n";

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LinkRatioSEDelegate())
    }
    
    @Test
    public void testStandardError() {
        LinkRatioSE ses = runScript("ses = standardError(scales)")
        
        assertTrue(ses instanceof SimpleLinkRatioSE)

        assertEquals(7, ses.getLength())
        for(d in 0..<7)
            assertFalse(Double.isNaN(ses.getValue(d)))
    }
    
    private LinkRatioSE runScript(String script) {
        script = BASE_SCRIPT + script
        return (LinkRatioSE) executor.runScript(script)
    }
    
    @Test
    public void testStandardError_String() {
        String script = 
        "ses1 = standardError(scales, \"LogLin\")\n"        +
        "ses2 = standardError(scales, \"Log Lin\")\n"       +
        "ses3 = standardError(scales, \"Log-Lin\")\n"       +
        "ses4 = standardError(scales, \"LogLinear\")\n"     +
        "ses5 = standardError(scales, \"Log Linear\")\n"    +
        "ses6 = standardError(scales, \"Log-Linear\")\n"    +
        "ses7 = standardError(scales, \"FixedRate\")\n"     +
        "ses8 = standardError(scales, \"Fixed Rate\")\n"    +
        "ses9 = standardError(scales, \"Fixed-Rate\")\n"    ;
        
        runScript(script)
        def indices = 1..9
        def ses = indices.collect {executor.getVariable("ses${it}")}
        
        for(LinkRatioSE se in ses) {
            assertEquals(7, se.getLength())
            for(d in 0..<7)
                assertFalse(Double.isNaN(se.getValue(d)))
        }
    }
    
    @Test
    public void testStandardError_Builder() {
        String script = 
        "standardError(scales) {\n"     +
        "   logLinear(0)\n"             +
        "   logLinear(0..2)\n"          +
        "   fixedRate(0)\n"             +
        "   fixedRate(0..2)\n"          +
        "   fixed(6, 0.02)\n"           +
        "   fixed(0:0.02, 1:0.03)\n"    +
        "}";
        
        LinkRatioSE ses = runScript(script)
        for(d in 0..<7)
            assertFalse(Double.isNaN(ses.getValue(d)))
    }
}

