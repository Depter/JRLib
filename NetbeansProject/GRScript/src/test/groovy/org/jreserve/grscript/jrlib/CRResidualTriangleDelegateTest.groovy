package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CRResidualTriangleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                        +
        "incurredData = apcIncurred()\n"                +
        "cummulate(paidData)\n"                         +
        "cummulate(incurredData)\n"                     +
        "paid = triangle(paidData)\n"                   +
        "incurred = triangle(incurredData)\n"           +
        "lrPaid = smooth(linkRatio(paid), 10)\n"        +
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "crs = ratios(lrPaid, lrIncurred)\n"            +
        "scales = scale(crs)\n";

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
        executor.addFunctionProvider(new ClaimRatioScaleDelegate())
        executor.addFunctionProvider(new CRResidualTriangleDelegate())
    }
    
    @Test
    public void testResiduals() {
        CRResidualTriangle res = runScript("res = residuals(scales)")
        
        assertTrue(res instanceof AdjustedClaimRatioResiduals)
        assertEquals(8, res.getAccidentCount())
        assertEquals(8, res.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<(8-a))
                assertFalse(Double.isNaN(res.getValue(a, d)))
    }
    
    private CRResidualTriangle runScript(String script) {
        script = BASE_SCRIPT + script
        return (CRResidualTriangle) executor.runScript(script)
    }
    
    @Test
    public void testResiduals_Boolean() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "adjusted = residuals(scales, true)";
        
        CRResidualTriangle adjusted = runScript(script)
        CRResidualTriangle res = (CRResidualTriangle) executor.getVariable("res")
        
        assertTrue(res instanceof ClaimRatioResiduals)
        assertTrue(adjusted instanceof AdjustedClaimRatioResiduals)
        
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
        "res = residuals(scales, false)\n" + 
        "adjusted = adjust(res)";
        
        AdjustedClaimRatioResiduals adjusted = (AdjustedClaimRatioResiduals) runScript(script)
        CRResidualTriangle res = (CRResidualTriangle) executor.getVariable("res")
        
        assertTrue(res instanceof ClaimRatioResiduals)
        assertTrue(adjusted instanceof AdjustedClaimRatioResiduals)
        assertTrue(res.is(adjusted.getSourceResidualTriangle()))
    }
    
    @Test
    public void testExclude_int_int() {
        String script = 
        "res = residuals(scales, false)\n" + 
        "res = exclude(res, 0, 2)";
        
        CRResidualTriangle res = runScript(script)
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
        "res = residuals(scales, false)\n" + 
        "res = exclude(res, [accident:0, d:2])";
        
        CRResidualTriangle res = runScript(script)
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
    public void testBuilder() {
        String script = 
        "res = residuals(scales) {\n"                  +
        "   adjust()\n"                             +
        "   exclude(0, 0)\n"                        +
        "   exclude(a:1, d:0)\n"                    +
        "   exclude(accident:2, development:0)\n"   +
        "}\n";
        
        CRResidualTriangle res = runScript(script)
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

