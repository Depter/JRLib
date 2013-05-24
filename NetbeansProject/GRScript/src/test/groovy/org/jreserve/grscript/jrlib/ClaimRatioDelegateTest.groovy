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
import org.jreserve.jrlib.claimratio.ClaimRatio

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimRatioDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                    +
        "incurredData = apcIncurred()\n"            +
        "cummulate(paidData)\n"                     +
        "cummulate(incurredData)\n"                 +
        "paid = triangle(paidData)\n"               +
        "incurred = triangle(incurredData)\n"       +
        "ratios = ratioTriangle(paid, incurred)\n"  ;
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
        executor.addFunctionProvider(new RatioTriangleDelegate())
        executor.addFunctionProvider(new ClaimRatioDelegate())
    }
    
    @Test
    public void testConstructor_RatioTriangle() {
        ClaimRatio cr = runScript("cr = ratios(ratios)")
        assertEquals(8, cr.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    private ClaimRatio runScript(String script) {
        script = BASE_SCRIPT + script
        return (ClaimRatio) executor.runScript(script)
    }
    
    @Test
    public void testConstructor_ClaimTriangles() {
        ClaimRatio cr = runScript("cr = ratios(paid, incurred)")
        assertEquals(8, cr.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testConstructor_Map() {
        ClaimRatio cr = runScript("cr = ratios(numerator:paid, denominator:incurred)")
        assertEquals(8, cr.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testConstructor_RatioTriangle_int_LinkRatios() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(ratios, 10, lrPaid, lrIncurred)";
        
        ClaimRatio cr = runScript(script)
        assertEquals(10, cr.getLength())
        for(d in 0..<9)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testConstructor_RatioTriangle_LinkRatios() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(ratios, lrPaid, lrIncurred)";
        
        ClaimRatio cr = runScript(script)
        assertEquals(10, cr.getLength())
        for(d in 0..<9)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testConstructor_LinkRatios() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(lrPaid, lrIncurred)";
        
        ClaimRatio cr = runScript(script)
        assertEquals(10, cr.getLength())
        for(d in 0..<9)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testBuilder_RatioTriangle() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(ratios, 10) {\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 7, 8)\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 2..4)\n"+
        "   fixed(8, 0.9)\n"+
        "   fixed(8:0.9, 9:1)\n"+
        "}";
        
        ClaimRatio cr = runScript(script)
        assertEquals(10, cr.getLength())
        for(d in 0..<9)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testBuilder_LinkRatios_int() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(lrPaid, lrIncurred, 9) {\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 7, 8)\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 2..4)\n"+
        "   fixed(8, 0.9)\n"+
        "   fixed(8:0.9, 9:1)\n"+
        "}";
        
        ClaimRatio cr = runScript(script)
        assertEquals(9, cr.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
    
    @Test
    public void testBuilder_LinkRatios() {
        String script = 
        "lrPaid = smooth(linkRatio(paid), 10)\n"+
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "cr = ratios(lrPaid, lrIncurred) {\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 7, 8)\n"+
        "   lrExtrapolation(lrPaid, lrIncurred, 2..4)\n"+
        "   fixed(8, 0.9)\n"+
        "   fixed(8:0.9, 9:1)\n"+
        "}";
        
        ClaimRatio cr = runScript(script)
        assertEquals(10, cr.getLength())
        for(d in 0..<9)
            assertFalse(Double.isNaN(cr.getValue(d)))
    }
}

