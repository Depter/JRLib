package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestConfig
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioCurveDelegateTest {
    
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
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
    }
    
    @Test
    public void testSmooth() {
        LinkRatioSmoothing curve = runScript("curve = smooth(lrs)")
        
        assertTrue(curve instanceof SimpleLinkRatioSmoothing)
        SimpleLinkRatioSmoothing sCurve = (SimpleLinkRatioSmoothing) curve

        assertEquals(7, curve.getLength())
        for(d in 0..<7)
            assertFalse(Double.isNaN(curve.getValue(d)))
    }
    
    private LinkRatioSmoothing runScript(String script) {
        script = BASE_SCRIPT + script
        return (LinkRatioSmoothing) executor.runScript(script)
    }
    
    @Test
    public void testSmooth_int() {
        LinkRatioSmoothing curve = runScript("curve = smooth(lrs, 10)")
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        
        assertEquals(7, lrs.getLength())
        assertEquals(10, curve.getLength())
        for(d in 0..<10) {
            if(d < 7) {
                assertEquals(lrs.getValue(d), curve.getValue(d), TestConfig.EPSILON)
            } else {
                assertFalse(Double.isNaN(curve.getValue(d)))
            }
        }
    }
    
    @Test
    public void testSmooth_int_String() {
        String script = 
        "exp = smooth(lrs, 10, \"exponential\")\n"+
        "pow = smooth(lrs, 10, \"pow\")\n"+
        "invPow = smooth(lrs, 10, \"inverse-power\")\n"+
        "weibul = smooth(lrs, 10, \"wei\")\n"
        ;
        
        LinkRatioSmoothing weibull = runScript(script)
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        
        assertEquals(7, lrs.getLength())
        assertEquals(10, weibull.getLength())
        for(d in 0..<10) {
            if(d < 7) {
                assertEquals(lrs.getValue(d), weibull.getValue(d), TestConfig.EPSILON)
            } else {
                assertFalse(Double.isNaN(weibull.getValue(d)))
            }
        }
    }
    
    @Test
    public void testSmooth_int_String_int() {
        String script = 
        "inc = smooth(lrs, 10, \"exponential\")\n" +
        "exc = smooth(lrs, 10, \"exponential\", 0)\n";
        
        LinkRatioSmoothing exc = runScript(script)
        LinkRatioSmoothing inc = (LinkRatioSmoothing) executor.getVariable("inc")
        
        assertEquals(10, exc.getLength())
        assertEquals(10, inc.getLength())
        for(d in 7..<10) {
            double i = inc.getValue(d)
            double e = exc.getValue(d)
            assertNotEquals(i, e, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmooth_int_String_Collection() {
        String script = 
        "inc = smooth(lrs, 10, \"exponential\")\n" +
        "exc = smooth(lrs, 10, \"exponential\", 0..2)\n";
        
        LinkRatioSmoothing exc = runScript(script)
        LinkRatioSmoothing inc = (LinkRatioSmoothing) executor.getVariable("inc")
        
        assertEquals(10, exc.getLength())
        assertEquals(10, inc.getLength())
        for(d in 7..<10) {
            double i = inc.getValue(d)
            double e = exc.getValue(d)
            assertNotEquals(i, e, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmoothAll() {
        LinkRatioSmoothing curve = runScript("curve = smoothAll(lrs)")
        assertTrue(curve instanceof DefaultLinkRatioSmoothing)
        
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        
        assertEquals(7, curve.getLength())
        for(d in 0..<7) {
            double c = curve.getValue(d)
            double l = lrs.getValue(d)
            assertFalse(Double.isNaN(c))
            assertNotEquals(l, c, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmoothAll_int() {
        LinkRatioSmoothing curve = runScript("curve = smoothAll(lrs, 10)")
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        
        assertEquals(7, lrs.getLength())
        assertEquals(10, curve.getLength())
        for(d in 0..<10) {
            double c = curve.getValue(d)
            double l = lrs.getValue(d)
            assertFalse(Double.isNaN(c))
            assertNotEquals(l, c, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmoothAll_int_String() {
        String script = 
        "exp = smoothAll(lrs, 10, \"exponential\")\n"+
        "pow = smoothAll(lrs, 10, \"pow\")\n"+
        "invPow = smoothAll(lrs, 10, \"inverse-power\")\n"+
        "weibul = smoothAll(lrs, 10, \"wei\")\n"
        ;
        
        LinkRatioSmoothing weibull = runScript(script)
        LinkRatio lrs = (LinkRatio) executor.getVariable("lrs")
        
        assertEquals(7, lrs.getLength())
        assertEquals(10, weibull.getLength())
        for(d in 0..<10) {
            double c = weibull.getValue(d)
            double l = lrs.getValue(d)
            assertFalse(Double.isNaN(c))
            assertNotEquals(l, c, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmoothAll_int_String_int() {
        String script = 
        "inc = smoothAll(lrs, 10, \"exponential\")\n" +
        "exc = smoothAll(lrs, 10, \"exponential\", 0)\n";
        
        LinkRatioSmoothing exc = runScript(script)
        LinkRatioSmoothing inc = (LinkRatioSmoothing) executor.getVariable("inc")
        
        assertEquals(10, exc.getLength())
        assertEquals(10, inc.getLength())
        for(d in 0..<10) {
            double i = inc.getValue(d)
            double e = exc.getValue(d)
            assertNotEquals(i, e, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testSmoothAll_int_String_Collection() {
        String script = 
        "inc = smoothAll(lrs, 10, \"exponential\")\n" +
        "exc = smoothAll(lrs, 10, \"exponential\", 0..2)\n";
        
        LinkRatioSmoothing exc = runScript(script)
        LinkRatioSmoothing inc = (LinkRatioSmoothing) executor.getVariable("inc")
        
        assertEquals(10, exc.getLength())
        assertEquals(10, inc.getLength())
        for(d in 0..<10) {
            double i = inc.getValue(d)
            double e = exc.getValue(d)
            assertNotEquals(i, e, TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testRSquare() {
        String script = 
        "curve = smoothAll(lrs, 10, \"exp\")\n"+
        "rS = rSquare(curve)\n";
        
        double rSquare = (Double) executor.runScript(BASE_SCRIPT + script)
        assertTrue(0 <= rSquare && rSquare <= 1)
    }
}

