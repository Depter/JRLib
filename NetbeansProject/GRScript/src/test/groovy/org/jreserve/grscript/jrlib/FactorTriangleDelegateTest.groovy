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
class FactorTriangleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"+
        "cummulate(data)\n"+
        "t = factors(data)\n";
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new FactorTriangleDelegate())
    }
	
    
    @Test
    public void testConstructor() {
        Triangle t = runScript("")
        assertEquals(7, t.getAccidentCount())
        assertEquals(7, t.getDevelopmentCount())
        for(a in 0..<7)
            for(d in 0..<(7-a))
                assertFalse(Double.isNaN(t.getValue(a, d)))
    }
    
    private Triangle runScript(String script) {
        script = BASE_SCRIPT + script
        return (Triangle) executor.runScript(script)
    }
    
    @Test
    public void testExclude() {
        Triangle t = runScript("t = exclude(t, 0, 1)\n")
        assertEquals(7, t.getAccidentCount())
        assertEquals(7, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testExclude_Map() {
        Triangle t = runScript("t = exclude(t, [aCCidenT:0, d:1])\n")
        assertEquals(7, t.getAccidentCount())
        assertEquals(7, t.getDevelopmentCount())
        assertTrue(Double.isNaN(t.getValue(0, 1)))
    }
    
    @Test
    public void testCorrigate() {
        Triangle t = runScript("t = corrigate(t, 0, 1, 5)\n")
        assertEquals(7, t.getAccidentCount())
        assertEquals(7, t.getDevelopmentCount())
        assertEquals(5d, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    @Test
    public void testCorrigate_Map() {
        Triangle t = runScript("t = corrigate(t, [aCCidenT:0, d:1, value:5])\n")
        assertEquals(7, t.getAccidentCount())
        assertEquals(7, t.getDevelopmentCount())
        assertEquals(5d, t.getValue(0, 1), TestConfig.EPSILON)
    }
    
    @Test
    public void testSmooth() {
        String script = 
        "smoothed = smooth(t) {\n"+
        "   type(type:\"ma\", length:3)\n"+
        "   cell(1, 0, false)\n"+
        "   cell(2,0, false)\n"+
        "   cell(a:3, d:0, applied:true)"+
        "}\n";
        
        Triangle smoothed = runScript(script)
        Triangle t = (Triangle) executor.getVariable("t")
        for(a in 0..7) {
            for(d in 0..<(7-a)) {
                double expected = t.getValue(a,d)
                double found = smoothed.getValue(a,d)
                if(a==3 && d==0) {
                    assertNotEquals(expected, found, TestConfig.EPSILON)
                } else {
                    assertEquals(expected, found, TestConfig.EPSILON)
                }
            }
        }
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "factors = factors(data) {\n"                       +
        "   corrigate(0, 2, 5)\n"                           +
        "   exclude(a:0, d:3)\n"                            +
        "   smooth {\n"                                     +
        "       type(type:\"exponential\", alpha:0.5)\n"    +
        "       cell(1, 0, false)\n"                        +
        "       cell(2,0, false)\n"                         +
        "       cell(a:3, d:0, applied:true)\n"             +
        "   }\n"                                            +
        "}\n";
        
        Triangle factors = runScript(script)
        Triangle source = (Triangle) executor.getVariable("t");
        for(a in 0..7) {
            for(d in 0..<(7-a)) {
                double found = factors.getValue(a,d)
                double expected = source.getValue(a,d)
                if(a==0 && d==2) {
                    assertEquals(5, found, TestConfig.EPSILON)
                } else if(a==0 && d==3) {
                    assertTrue(Double.isNaN(found))
                } else if(a==3 && d==0) {
                    assertNotEquals(expected, found, TestConfig.EPSILON)
                } else {
                    assertEquals(expected, found, TestConfig.EPSILON)
                }
            }
        }
    }
}

