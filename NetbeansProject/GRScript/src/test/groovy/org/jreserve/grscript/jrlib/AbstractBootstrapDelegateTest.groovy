package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.jrlib.util.random.Random
import org.jreserve.jrlib.util.random.JavaRandom

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractBootstrapDelegateTest {
    
    private ScriptExecutor executor
    private DummyBS dummy
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        dummy = new DummyBS()
        executor.addFunctionProvider(dummy)
    }
    
    @Test
    public void testSetCount() {
        String script = 
        "bootstrap {\n"+
        "   count 1000\n"+
        "}\n";
        executor.runScript(script)
        assertEquals(1000, dummy.getCount())
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_Invalid() {
        String script = 
        "bootstrap {\n"+
        "   count 0\n"+
        "}\n";
        executor.runScript(script)
    }
    
    @Test
    public void testSetRandom_Rnd() {
        String script = 
        "def rnd = jRnd\n"+
        "bootstrap {\n"+
        "   random rnd\n"+
        "}\n";
        JavaRandom jRnd = new JavaRandom()
        executor.setVariable("jRnd", jRnd)
        executor.runScript(script)
        assertTrue(jRnd.is(dummy.getRandom()))
    }
    
    @Test
    public void testDefaultRandom() {
        String script = 
        "bootstrap {\n"+
        "}\n";
        executor.runScript(script)
        Random rnd = dummy.getRandom()
        assertTrue(rnd instanceof JavaRandom)
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetRandom_Null() {
        String script = 
        "def rnd = null\n"+
        "bootstrap {\n"+
        "   random rnd\n"+
        "}\n";
        executor.runScript(script)
    }
    
    @Test
    public void testSetRandom_String_long() {
        String script = 
        "bootstrap {\n"+
        "   random \"Java\", 10\n"+
        "}\n";
        executor.runScript(script)
        Random rnd = dummy.getRandom()
        assertTrue(rnd instanceof JavaRandom)
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetRandom_String_long_Unknown() {
        String script = 
        "bootstrap {\n"+
        "   random \"MyRandom\", 10\n"+
        "}\n";
        executor.runScript(script)
    }
    
    @Test
    public void testRandom_Closure() {
        String script = 
        "bootstrap {\n"             +
        "   random {\n"             +
        "       type \"Java\"\n"    +
        "       seed 10\n"          +
        "   }\n"                    +
        "}\n"                       ;
        executor.runScript(script)
        Random rnd = dummy.getRandom()
        assertTrue(rnd instanceof JavaRandom)
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCheckState() {
        String script = 
        "bootstrap {\n"             +
        "   random \"Java\", 10\n"  +
        "}\n"                       ;
        executor.runScript(script)
        dummy.checkState()
    }
    
    public void testSegments() {
        String script = 
        "bootstrap {\n"             +
        "   segment {\n"            +
        "       cell(1, 1)\n"       +
        "       cell(2, 2)\n"       +
        "   }\n"                    +
        "   segment {\n"            +
        "       from(0, 3)\n"       +
        "       to(2, 3)\n"         +
        "   }\n"                    +
        "}\n"                       ;
        executor.runScript(script)
        List<int[][]> segments = executor.getSegments()
        assertEquals(2, segments.size())
        
        int[][] segment = segments.get(0);
        assertEquals(2, segment.length)
        assertEquals(1, segment[0][0])
        assertEquals(1, segment[0][1])
        assertEquals(2, segment[1][0])
        assertEquals(2, segment[1][1])
        
        segment = segments.get(1);
        assertEquals(3, segment.length)
        assertEquals(0, segment[0][0])
        assertEquals(3, segment[0][1])
        assertEquals(1, segment[1][0])
        assertEquals(3, segment[1][1])
        assertEquals(2, segment[2][0])
        assertEquals(3, segment[2][1])
    }
    
    private class DummyBS extends AbstractBootstrapDelegate implements FunctionProvider {
        
        void initFunctions(Script script, ExpandoMetaClass emc) {
            emc.bootstrap << this.&bootstrap
        }
        
        void bootstrap(Closure cl) {
            cl.delegate = this
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
        }
    }
}

