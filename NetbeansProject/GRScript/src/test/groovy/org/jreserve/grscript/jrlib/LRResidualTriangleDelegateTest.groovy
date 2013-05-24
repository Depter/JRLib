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
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LRResidualTriangleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"        +
        "cummulate(data)\n"         +
        "lrs = linkRatio(data)\n"   +
        "scales = scale(lrs)\n"   ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LRResidualTriangleDelegate())
    }
    
    @Test
    public void testResiduals() {
        LRResidualTriangle res = runScript("res = residuals(scales)")
        
        assertTrue(res instanceof AdjustedLinkRatioResiduals)
        assertEquals(7, res.getAccidentCount())
        assertEquals(7, res.getDevelopmentCount())
        for(a in 0..<7)
            for(d in 0..<(7-a))
                assertFalse(Double.isNaN(res.getValue(a, d)))
    }
    
    private LRResidualTriangle runScript(String script) {
        script = BASE_SCRIPT + script
        return (LRResidualTriangle) executor.runScript(script)
    }
    
    @Test
    public void testResiduals_Boolean() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "adjusted = residuals(scales, true)";
        
        LRResidualTriangle adjusted = runScript(script)
        LRResidualTriangle res = (LRResidualTriangle) executor.getVariable("res")
        
        assertTrue(res instanceof LinkRatioResiduals)
        assertTrue(adjusted instanceof AdjustedLinkRatioResiduals)
        
        assertEquals(7, res.getAccidentCount())
        assertEquals(7, res.getDevelopmentCount())
        assertEquals(7, adjusted.getAccidentCount())
        assertEquals(7, adjusted.getDevelopmentCount())
        
        for(a in 0..<7) {
            for(d in 0..<(7-a)) {
                assertFalse(Double.isNaN(res.getValue(a, d)))
                assertFalse(Double.isNaN(adjusted.getValue(a, d)))
            }
        }
    }
    
    @Test
    public void testAdjust() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "adjusted = adjust(res)";
        
        AdjustedLinkRatioResiduals adjusted = (AdjustedLinkRatioResiduals) runScript(script)
        LRResidualTriangle res = (LRResidualTriangle) executor.getVariable("res")
        
        assertTrue(res instanceof LinkRatioResiduals)
        assertTrue(adjusted instanceof AdjustedLinkRatioResiduals)
        assertTrue(res.is(adjusted.getSourceLRResidualTriangle()))
    }
    
    @Test
    public void testCenter() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "centered = center(res)";
        
        CenteredLinkRatioResiduals centered = (CenteredLinkRatioResiduals) runScript(script)
        LRResidualTriangle res = (LRResidualTriangle) executor.getVariable("res")
        
        assertTrue(res instanceof LinkRatioResiduals)
        assertTrue(centered instanceof CenteredLinkRatioResiduals)
        assertTrue(res.is(centered.getSourceLRResidualTriangle()))
    }
    
    @Test
    public void testExclude_int_int() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "res = exclude(res, 0, 2)";
        
        LRResidualTriangle res = runScript(script)
        for(a in 0..<7) {
            for(d in 0..<(7-a)) {
                if(a==0 && d==2) {
                    assertTrue(Double.isNaN(res.getValue(a, d)))
                } else {
                    assertFalse(Double.isNaN(res.getValue(a, d)))
                }
            }
        }
    }
    
    @Test
    public void testExclude_Map() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "res = exclude(res, [accident:0, d:2])";
        
        LRResidualTriangle res = runScript(script)
        for(a in 0..<7) {
            for(d in 0..<(7-a)) {
                if(a==0 && d==2) {
                    assertTrue(Double.isNaN(res.getValue(a, d)))
                } else {
                    assertFalse(Double.isNaN(res.getValue(a, d)))
                }
            }
        }
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "res = residuals(scales) {\n"                  +
        "   adjust()\n"                             +
        "   exclude(0, 0)\n"                        +
        "   exclude(a:1, d:0)\n"                    +
        "   exclude(accident:2, development:0)\n"   +
        "   center()\n"                             +
        "}\n";
        
        LRResidualTriangle res = runScript(script)
        for(a in 0..<7) {
            for(d in 0..<(7-a)) {
                if((a==0 || a==1 || a==2) && d==0) {
                    assertTrue(Double.isNaN(res.getValue(a, d)))
                } else {
                    assertFalse(Double.isNaN(res.getValue(a, d)))
                }
            }
        }
    }
}

