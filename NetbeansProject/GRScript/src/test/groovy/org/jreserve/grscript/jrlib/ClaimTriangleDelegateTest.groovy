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
import org.jreserve.grscript.TestConfig
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.triangle.Triangle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimTriangleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"+
        "cummulate(data)\n"+
        "t = triangle(data)\n";
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
    }
    
    @Test
    public void testConstructor() {
        Triangle t = runScript("")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
    
    private Triangle runScript(String script) {
        script = BASE_SCRIPT + script
        return (Triangle) executor.runScript(script)
    }
    
    @Test
    public void testExclude() {
        Triangle t = runScript("t = exclude(t, 0, 1)\n")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testExclude_Map() {
        Triangle t = runScript("t = exclude(t, [aCCidenT:0, d:1])\n")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testCorrigate() {
        Triangle t = runScript("t = corrigate(t, 0, 1, 5)\n")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertEquals(5d, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    @Test
    public void testCorrigate_Map() {
        Triangle t = runScript("t = corrigate(t, [aCCidenT:0, d:1, value:5])\n")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertEquals(5d, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    @Test
    public void testSmooth() {
        String script = 
        "t = smooth(t) {\n"+
        "   type(type:\"exponential\", alpha:0.5)\n"+
        "   cell(1, 0, false)\n"+
        "   cell(2,0, false)\n"+
        "   cell(a:3, d:0, applied:true)"+
        "}\n";
        
        Triangle t = runScript(script)
        double[][] data = (double[][]) executor.getVariable("data")
        for(a in 0..7) {
            for(d in 0..<(8-a)) {
                if(a==3 && d==0) {
                    assertNotEquals(data[a][d], t.getValue(a,d), TestConfig.EPSILON)
                } else {
                    assertEquals(data[a][d], t.getValue(a,d), TestConfig.EPSILON)
                }
            }
        }
    }
    
    @Test
    public void testCummulate() {
        String script = 
            "data = apcPaid()\n"    +
            "t = triangle(data)\n"  +
            "t = cummulate(t)"      ;
        
        Triangle t = executor.runScript script
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "t = triangle(data) {\n"                            +
        "   corrigate(0, 2, 5)\n"                           +
        "   exclude(a:0, d:3)\n"                            +
        "   smooth {\n"                                     +
        "       type(type:\"exponential\", alpha:0.5)\n"    +
        "       cell(1, 0, false)\n"                        +
        "       cell(2,0, false)\n"                         +
        "       cell(a:3, d:0, applied:true)\n"             +
        "   }\n"                                            +
        "}\n";
        
        Triangle t = runScript(script)
        double[][] data = (double[][]) executor.getVariable("data")
        for(a in 0..8) {
            for(d in 0..<(8-a)) {
                if(a==0 && d==2) {
                    assertEquals(5, t.getValue(a,d), TestConfig.EPSILON)
                } else if(a==0 && d==3) {
                    assertTrue(Double.isNaN(t.getValue(a, d)))
                } else if(a==3 && d==0) {
                    assertNotEquals(data[a][d], t.getValue(a,d), TestConfig.EPSILON)
                } else {
                    assertEquals(data[a][d], t.getValue(a,d), TestConfig.EPSILON)
                }
            }
        }
    }
}

