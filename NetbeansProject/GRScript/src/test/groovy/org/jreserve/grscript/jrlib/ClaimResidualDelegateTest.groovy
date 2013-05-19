package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.CenteredOdpResidualTriangle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimResidualDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"        +
        "cummulate(data)\n"         +
        "lrs = linkRatio(data)\n"   ;

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new ClaimResidualDelegate())
    }
    
    @Test
    public void testResiduals() {
        OdpResidualTriangle res = runScript("res = residuals(lrs)")
        
        assertFalse(res instanceof AdjustedOdpResidualTriangle)
        assertEquals(8, res.getAccidentCount())
        assertEquals(8, res.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(res.getValue(a, d)))
    }
    
    private OdpResidualTriangle runScript(String script) {
        script = BASE_SCRIPT + script
        return (OdpResidualTriangle) executor.runScript(script)
    }
    
    @Test
    public void testResiduals_Boolean() {
        String script = 
        "res = residuals(lrs, false)\n" + 
        "adjusted = residuals(lrs, true)\n";
        
        OdpResidualTriangle adjusted = runScript(script)
        OdpResidualTriangle res = (OdpResidualTriangle) executor.getVariable("res")
        
        assertFalse(res instanceof AdjustedOdpResidualTriangle)
        assertTrue(adjusted instanceof AdjustedOdpResidualTriangle)
        
        assertEquals(8, res.getAccidentCount())
        assertEquals(8, res.getDevelopmentCount())
        assertEquals(8, adjusted.getAccidentCount())
        assertEquals(8, adjusted.getDevelopmentCount())
        
        for(a in 0..<8) {
            for(d in 0..<(8-a)) {
                assertFalse(Double.isNaN(res.getValue(a, d)))
                assertFalse(Double.isNaN(adjusted.getValue(a, d)))
            }
        }
    }
    
    @Test
    public void testAdjust() {
        String script = 
        "res = residuals(lrs, false)\n" + 
        "adjusted = adjust(res)";
        
        AdjustedOdpResidualTriangle adjusted = (AdjustedOdpResidualTriangle) runScript(script)
        OdpResidualTriangle res = (OdpResidualTriangle) executor.getVariable("res")
        assertTrue(res instanceof OdpResidualTriangle)
        assertTrue(adjusted instanceof AdjustedOdpResidualTriangle)
        assertTrue(res.is(adjusted.getSourceOdpResidualTriangle()))
    }
    
    @Test
    public void testCenter() {
        String script = 
        "res = residuals(lrs, false)\n" + 
        "centered = center(res)";
        
        CenteredOdpResidualTriangle centered = (CenteredOdpResidualTriangle) runScript(script)
        OdpResidualTriangle res = (OdpResidualTriangle) executor.getVariable("res")
        assertTrue(res instanceof OdpResidualTriangle)
        assertTrue(centered instanceof CenteredOdpResidualTriangle)
        assertTrue(res.is(centered.getSourceOdpResidualTriangle()))
    }
    
    @Test
    public void testExclude_int_int() {
        String script = 
        "res = residuals(lrs, false)\n" + 
        "res = exclude(res, 0, 2)";
        
        OdpResidualTriangle res = runScript(script)
        for(a in 0..<8) {
            for(d in 0..<(8-a)) {
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
        "res = residuals(lrs, false)\n" + 
        "res = exclude(res, [accident:0, d:2])";
        
        OdpResidualTriangle res = runScript(script)
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
        "res = residuals(lrs) {\n"                  +
        "   adjust()\n"                             +
        "   exclude(0, 0)\n"                        +
        "   exclude(a:1, d:0)\n"                    +
        "   exclude(accident:2, development:0)\n"   +
        "   center()\n"                             +
        "}\n";
        
        OdpResidualTriangle res = runScript(script)
        for(a in 0..<8) {
            for(d in 0..<(8-a)) {
                if((a==0 || a==1 || a==2) && d==0) {
                    assertTrue(Double.isNaN(res.getValue(a, d)))
                } else {
                    assertFalse(Double.isNaN(res.getValue(a, d)))
                }
            }
        }
    }	
}

