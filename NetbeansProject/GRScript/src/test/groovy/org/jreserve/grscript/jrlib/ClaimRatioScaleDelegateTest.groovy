package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimRatioScaleDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                        +
        "incurredData = apcIncurred()\n"                +
        "cummulate(paidData)\n"                         +
        "cummulate(incurredData)\n"                     +
        "paid = triangle(paidData)\n"                   +
        "incurred = triangle(incurredData)\n"           +
        "lrPaid = smooth(linkRatio(paid), 10)\n"        +
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"+
        "crs = ratios(lrPaid, lrIncurred)\n"            ;

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
    }
    
    @Test
    public void testScale() {
        ClaimRatioScale scales = runScript("scales = scale(crs)")
        
        assertTrue(scales instanceof SimpleClaimRatioScale)

        assertEquals(10, scales.getLength())
        for(d in 0..<10)
            assertFalse(Double.isNaN(scales.getValue(d)))
    }
    
    private ClaimRatioScale runScript(String script) {
        script = BASE_SCRIPT + script
        return (ClaimRatioScale) executor.runScript(script)
    }	
    
    @Test
    public void testScale_String() {
        String script = 
        "minMax1 = scale(crs, \"MinMax\")\n"+
        "minMax2 = scale(crs, \"Min Max\")\n"+
        "minMax3 = scale(crs, \"Min-Max\")\n"+
        "lin1 = scale(crs, \"Extrapolation\")\n"+
        "lin2 = scale(crs, \"LogLin\")\n"+
        "lin3 = scale(crs, \"Log Lin\")\n"+
        "lin4 = scale(crs, \"Log-Lin\")\n"+
        "lin5 = scale(crs, \"LogLinear\")\n"+
        "lin6 = scale(crs, \"Log Linear\")\n"+
        "lin7 = scale(crs, \"Log-Linear\")\n"
        ;
        
        ClaimRatioScale lin7 = runScript(script)
        ClaimRatioScale minMax = (ClaimRatioScale) executor.getVariable("minMax3")
        
        assertEquals(10, lin7.getLength())
        assertEquals(10, minMax.getLength())
        for(d in 0..<10) {
            assertFalse(Double.isNaN(lin7.getValue(d)))
            assertFalse(Double.isNaN(minMax.getValue(d)))
        }
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "scale(crs) {\n"                +
        "   minMax(0)\n"                +
        "   minMax(5..6)\n"             +
        "   logLinear(0)\n"             +
        "   logLinear(0..2)\n"          +
        "   fixed(3, 50.42)\n"          +
        "   fixed(1:200.45, 2:122.5)\n" +
        "}\n"
        ;
        
        ClaimRatioScale scale = runScript(script)
        assertEquals(10, scale.getLength())
        for(d in 0..<7) {
            assertFalse(Double.isNaN(scale.getValue(d)))
        }
    }
}

