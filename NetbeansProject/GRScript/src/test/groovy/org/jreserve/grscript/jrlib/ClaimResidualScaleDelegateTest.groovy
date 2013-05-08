package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.grscript.TestConfig
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.ConstantOdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.VariableOdpResidualScale

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimResidualScaleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"                        +
        "cummulate(data)\n"                         +
        "lrs = linkRatio(data)\n"                   +
        "paidResiduals = residuals(lrs) {\n"  +
        "   exclude(accident:0, development:8)\n"   +
        "   exclude(accident:8, development:0)\n"   +
        "   adjust()\n"                             +
        "}\n"                                       ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new ClaimResidualDelegate())
        executor.addFunctionProvider(new ClaimResidualScaleDelegate())
    }
    
    @Test
    public void testConstantScale() {
        OdpResidualScale scales = runScript("scales = constantScale(paidResiduals)")
        
        assertTrue(scales instanceof ConstantOdpResidualScale)
        assertEquals(8, scales.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    private OdpResidualScale runScript(String script) {
        script = BASE_SCRIPT + script
        return (OdpResidualScale) executor.runScript(script)
    }
    
    @Test
    public void testConstantScale_LinkRatio() {
        OdpResidualScale scales = runScript("scales = constantScale(lrs)")
        
        assertTrue(scales instanceof ConstantOdpResidualScale)
        assertEquals(8, scales.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    @Test
    public void testConstantScale_double() {
        OdpResidualScale scales = runScript("scales = constantScale(paidResiduals, 100)")
        
        assertEquals(8, scales.getLength())
        for(d in 0..<8)
            assertEquals(100d, scales.getValue(d), TestConfig.EPSILON)
    }
    
    @Test
    public void testVariableScale() {
        OdpResidualScale scales = runScript("scales = variableScale(paidResiduals)")
        
        assertTrue(scales instanceof VariableOdpResidualScale)
        assertEquals(8, scales.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    @Test
    public void testVariableScale_LinkRatio() {
        OdpResidualScale scales = runScript("scales = variableScale(lrs)")
        
        assertTrue(scales instanceof VariableOdpResidualScale)
        assertEquals(8, scales.getLength())
        for(d in 0..<8)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    @Test
    public void testVariableScale_Builder() {
        String script = 
        "paidResScale = variableScale(paidResiduals) {\n"   +
        "   fixed(0, 100.45)\n"                             +
        "   fixed(1:200.45, 2:122.5)\n"                     +
        "}\n"                                               ;
                    
        OdpResidualScale scales = runScript(script)
        
        assertEquals(8, scales.getLength())
        for(d in 0..<8) {
            if(d==0) {
                assertEquals(100.45, scales.getValue(d), TestConfig.EPSILON)
            } else if(d==1) {
                assertEquals(200.45, scales.getValue(d), TestConfig.EPSILON)
            } else if(d==2) {
                assertEquals(122.5, scales.getValue(d), TestConfig.EPSILON)
            } else {
                assertFalse(Double.isNaN(scales.getValue(d)))
            }
        }
    }
}

