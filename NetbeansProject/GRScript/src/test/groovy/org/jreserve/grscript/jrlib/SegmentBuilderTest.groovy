package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.FunctionProvider

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SegmentBuilderTest {

    private ScriptExecutor executor
    private DummyBS dummy
	
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        dummy = new DummyBS()
        executor.addFunctionProvider(dummy)
    }
    
    @Test
    public void testCell() {
        String script = 
        "bootstrap {\n"         +
        "   segment {\n"        +
        "       cell(1, 0)\n"   +
        "       cell(1, 0)\n"   +
        "   }\n"                +
        "}\n"                   ;
        int[][] segments = getSegments(script)
        assertEquals(1, segments.length)
        
        int[] segment = segments[0]
        assertEquals(2, segment.length)
        assertEquals(1, segment[0])
        assertEquals(0, segment[1])
    }
    
    private int[][] getSegments(String script) {
        executor.runScript(script)
        return dummy.builder.getCells()
    }
    
    @Test
    public void testCell_Map() {
        String script = 
        "bootstrap {\n"         +
        "   segment {\n"        +
        "       cell(a:1, d:0)\n"   +
        "       cell(a:2, d:0)\n"   +
        "       cell(accident:1, development:0)\n"   +
        "   }\n"                +
        "}\n"                   ;
        int[][] segments = getSegments(script)

        assertEquals(2, segments.length)
        assertEquals(1, segments[0][0])
        assertEquals(0, segments[0][1])
        assertEquals(2, segments[1][0])
        assertEquals(0, segments[1][1])
    }
    
    @Test
    public void testFromTo() {
        String script = 
        "bootstrap {\n"         +
        "   segment {\n"        +
        "       from(1, 1)\n"   +
        "       to(3, 3)\n"   +
        "   }\n"                +
        "}\n"                   ;
        int[][] segments = getSegments(script)
        assertEquals(9, segments.length)
        
        int count = 0
        for(a in 1..3) {
            for(d in 1..3) {
                int[] segment = segments[count++]
                assertEquals(a, segment[0])
                assertEquals(d, segment[1])
            }
        }
    }
    
    private class DummyBS implements FunctionProvider {
        
        SegmentBuilder builder = new SegmentBuilder()
        
        void initFunctions(Script script, ExpandoMetaClass emc) {
            emc.bootstrap << this.&bootstrap
        }
        
        void bootstrap(Closure cl) {
            cl.delegate = this
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
        }
        
        void segment(Closure cl) {
            cl.delegate = builder
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
        }
    }
}

