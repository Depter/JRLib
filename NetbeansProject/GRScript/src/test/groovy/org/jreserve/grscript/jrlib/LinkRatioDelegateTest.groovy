package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestConfig
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.linkratio.LinkRatio


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioDelegateTest {
    
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
    }
    
    @Test
    public void testConstructor() {
        LinkRatio lrs = runScript("")
        assertEquals(7, lrs.getLength())
        for(d in 0..<7)
            assertFalse(Double.isNaN(lrs.getValue(d)))
    }
    
    private LinkRatio runScript(String script) {
        script = BASE_SCRIPT + script
        return (LinkRatio) executor.runScript(script)
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "lrs2 = linkRatio(data) {\n"  +
        "   average(0)\n"             +
        "   mack(1)\n"                +
        "   max(2)\n"                 +
        "   min(3)\n"                 +
        "   weightedAverage(4)\n"     +
        "   fixed(5, 1d)\n"           +
        "}";
        
        LinkRatio lrs2 = runScript(script)
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        assertEquals(7, lrs2.getLength())
        for(d in 0..<7) {
            double found = lrs2.getValue(d)
            double expected = lrs.getValue(d)
            
            if(d==4 || d > 5) {
                assertEquals(expected, found, TestConfig.EPSILON)
            } else {
                assertNotEquals(expected, found, TestConfig.EPSILON)
            }
        }
    }
    
    @Test
    public void testBuilder_Indices() {
        String script = 
        "lrs2 = linkRatio(data) {\n"      +
        "   average(0..2)\n"              +
        "   fixed(5..6, 1.02, 1.01)\n"    +
        "}";
        
        LinkRatio lrs2 = runScript(script)
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        assertEquals(7, lrs.getLength())
        for(d in 0..<7) {
            double found = lrs2.getValue(d)
            double expected = lrs.getValue(d)
            
            if(2 < d && d < 5) {
                assertEquals(expected, found, TestConfig.EPSILON)
            } else {
                assertNotEquals(expected, found, TestConfig.EPSILON)
            }
        }
    }
}