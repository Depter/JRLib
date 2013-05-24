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
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioScaleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"+
        "cummulate(data)\n"+
        "lrs = linkRatio(data)\n";

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
    }
    
    @Test
    public void testScale() {
        LinkRatioScale scales = runScript("scales = scale(lrs)")
        
        assertTrue(scales instanceof SimpleLinkRatioScale)

        assertEquals(7, scales.getLength())
        for(d in 0..<7)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    private LinkRatioScale runScript(String script) {
        script = BASE_SCRIPT + script
        return (LinkRatioScale) executor.runScript(script)
    }	
    
    @Test
    public void testScale_String() {
        String script = 
        "minMax1 = scale(lrs, \"MinMax\")\n"+
        "minMax2 = scale(lrs, \"Min Max\")\n"+
        "minMax3 = scale(lrs, \"Min-Max\")\n"+
        "lin1 = scale(lrs, \"Extrapolation\")\n"+
        "lin2 = scale(lrs, \"LogLin\")\n"+
        "lin3 = scale(lrs, \"Log Lin\")\n"+
        "lin4 = scale(lrs, \"Log-Lin\")\n"+
        "lin5 = scale(lrs, \"LogLinear\")\n"+
        "lin6 = scale(lrs, \"Log Linear\")\n"+
        "lin7 = scale(lrs, \"Log-Linear\")\n"
        ;
        
        LinkRatioScale lin7 = runScript(script)
        LinkRatioScale minMax = (LinkRatioScale) executor.getVariable("minMax3")
        
        assertEquals(7, lin7.getLength())
        assertEquals(7, minMax.getLength())
        for(d in 0..<7) {
            assertFalse(Double.isNaN(lin7.getValue(d)))
            assertFalse(Double.isNaN(minMax.getValue(d)))
        }
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "scale(lrs) {\n"                +
        "   minMax(0)\n"                +
        "   minMax(5..6)\n"             +
        "   logLinear(0)\n"             +
        "   logLinear(0..2)\n"          +
        "   fixed(3, 50.42)\n"          +
        "   fixed(1:200.45, 2:122.5)\n" +
        "}\n"
        ;
        
        LinkRatioScale scale = runScript(script)
        assertEquals(7, scale.getLength())
        for(d in 0..<7) {
            assertFalse(Double.isNaN(scale.getValue(d)))
        }
    }
}
