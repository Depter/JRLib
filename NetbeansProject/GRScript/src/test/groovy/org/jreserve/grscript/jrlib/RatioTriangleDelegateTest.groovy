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
class RatioTriangleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                +
        "incurredData = apcIncurred()\n"        +
        "cummulate(paidData)\n"                 +
        "cummulate(incurredData)\n"             +
        "paid = triangle(paidData)\n"           +
        "incurred = triangle(incurredData)\n"   ;
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new RatioTriangleDelegate())
    }
    
    @Test
    public void testConstructor() {
        Triangle t = runScript("pPerI = ratioTriangle(paid, incurred)")
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
    public void testConstructor_Map_1() {
        Triangle t = runScript("pPerI = ratioTriangle(numerator:paid, denominator:incurred)")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
    
    @Test
    public void testConstructor_Map_2() {
        Triangle t = runScript("pPerI = ratioTriangle(num:paid, denom:incurred)")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
    
    @Test
    public void testConstructor_Map_3() {
        Triangle t = runScript("pPerI = ratioTriangle(n:paid, d:incurred)")
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
	
    
    @Test
    public void testExclude() {
        String script = 
        "pPerI = ratioTriangle(paid, incurred)\n"+
        "pPerI = exclude(pPerI, 0, 1)";
        Triangle t = runScript(script)
        
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testExclude_Map() {
        String script = 
        "pPerI = ratioTriangle(paid, incurred)\n"+
        "pPerI = exclude(pPerI, [aCCidenT:0, d:1])";
        Triangle t = runScript(script)
        
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testCorrigate() {
        String script = 
        "pPerI = ratioTriangle(paid, incurred)\n"+
        "pPerI = corrigate(pPerI, 0, 1, 5)";
        Triangle t = runScript(script)
        
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertEquals(5d, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    @Test
    public void testCorrigate_Map() {
        String script = 
        "pPerI = ratioTriangle(paid, incurred)\n"+
        "pPerI = corrigate(pPerI, [aCCidenT:0, d:1, value:0.5])";
        Triangle t = runScript(script)
        
        assertEquals(8, t.getAccidentCount())
        assertEquals(8, t.getDevelopmentCount())
        assertEquals(0.5, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    
    @Test
    public void testSmooth() {
        String script = 
        "pPerI = ratioTriangle(paid, incurred)\n"+
        "pPerIS = smooth(pPerI) {\n"+
        "   type(type:\"exponential\", alpha:0.5)\n"+
        "   cell(1, 0, false)\n"+
        "   cell(2, 0, false)\n"+
        "   cell(a:3, d:0, applied:true)"+
        "}\n";
        
        Triangle smoothed = runScript(script)
        Triangle original = (Triangle) executor.getVariable("pPerI")
        for(a in 0..7) {
            for(d in 0..<(7-a)) {
                double s = smoothed.getValue(a, d)
                double o = original.getValue(a, d)
                if(a==3 && d==0) {
                    assertNotEquals(o, s, TestConfig.EPSILON)
                } else {
                    assertEquals(o, s, TestConfig.EPSILON)
                }
            }
        }
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "original = ratioTriangle(paid, incurred)\n"        +
        "t = ratioTriangle(paid, incurred) {\n"             +
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
        Triangle original = (Triangle) executor.getVariable("original")
        for(a in 0..8) {
            for(d in 0..<(8-a)) {
                double o = original.getValue(a, d)
                if(a==0 && d==2) {
                    assertEquals(5, t.getValue(a,d), TestConfig.EPSILON)
                } else if(a==0 && d==3) {
                    assertTrue(Double.isNaN(t.getValue(a, d)))
                } else if(a==3 && d==0) {
                    assertNotEquals(o, t.getValue(a,d), TestConfig.EPSILON)
                } else {
                    assertEquals(o, t.getValue(a,d), TestConfig.EPSILON)
                }
            }
        }
    }
}

