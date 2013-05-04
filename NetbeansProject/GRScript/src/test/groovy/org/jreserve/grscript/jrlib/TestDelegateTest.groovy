package org.jreserve.grscript.jrlib

import org.junit.Before
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.grscript.TestConfig
import org.jreserve.jrlib.test.CalendarEffectTest
import org.jreserve.jrlib.test.UncorrelatedDevelopmentFactorsTest
import org.jreserve.jrlib.test.Test

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TestDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcPaid()\n"+
        "cummulate(data)\n"+
        "claims = triangle(data)\n"+
        "factors = factors(claims)\n";
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new FactorTriangleDelegate())
        executor.addFunctionProvider(new TestDelegate())
    }
    
    @org.junit.Test
    public void testCalendarEffect_ClaimTriangle() {
        Test test = runScript("test = testCalendarEffect(claims)")
        
        assertTrue(test instanceof CalendarEffectTest)
        double alpha = ((CalendarEffectTest)test).getAlpha()
        assertEquals(CalendarEffectTest.DEFAULT_ALPHA, alpha, TestConfig.EPSILON)
    }
    
    private Test runScript(String script) {
        script = BASE_SCRIPT + script
        return (Test) executor.runScript(script)
    }
    
    @org.junit.Test
    public void testCalendarEffect_ClaimTriangle_double() {
        Test test = runScript("test = testCalendarEffect(claims, 0.2)")
        
        assertTrue(test instanceof CalendarEffectTest)
        double alpha = ((CalendarEffectTest)test).getAlpha()
        assertEquals(0.2, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testCalendarEffect_FactorTriangle() {
        Test test = runScript("test = testCalendarEffect(factors)")
        
        assertTrue(test instanceof CalendarEffectTest)
        double alpha = ((CalendarEffectTest)test).getAlpha()
        assertEquals(CalendarEffectTest.DEFAULT_ALPHA, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testCalendarEffect_FactorTriangle_double() {
        Test test = runScript("test = testCalendarEffect(factors, 0.2)")
        
        assertTrue(test instanceof CalendarEffectTest)
        double alpha = ((CalendarEffectTest)test).getAlpha()
        assertEquals(0.2, alpha, TestConfig.EPSILON)
    }
	
    
    @org.junit.Test
    public void testUncorrelatedDevelopmentFactors_ClaimTriangle() {
        Test test = runScript("test = testUncorrelatedDevFactors(claims)")
        
        assertTrue(test instanceof UncorrelatedDevelopmentFactorsTest)
        double alpha = ((UncorrelatedDevelopmentFactorsTest)test).getAlpha()
        assertEquals(UncorrelatedDevelopmentFactorsTest.DEFAULT_ALPHA, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testUncorrelatedDevelopmentFactors_ClaimTriangle_double() {
        Test test = runScript("test = testUncorrelatedDevFactors(claims, 0.2)")
        
        assertTrue(test instanceof UncorrelatedDevelopmentFactorsTest)
        double alpha = ((UncorrelatedDevelopmentFactorsTest)test).getAlpha()
        assertEquals(0.2, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testUncorrelatedDevelopmentFactors_FactorTriangle() {
        Test test = runScript("test = testUncorrelatedDevFactors(factors)")
        
        assertTrue(test instanceof UncorrelatedDevelopmentFactorsTest)
        double alpha = ((UncorrelatedDevelopmentFactorsTest)test).getAlpha()
        assertEquals(UncorrelatedDevelopmentFactorsTest.DEFAULT_ALPHA, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testUncorrelatedDevelopmentFactors_FactorTriangle_double() {
        Test test = runScript("test = testUncorrelatedDevFactors(factors, 0.2)")
        
        assertTrue(test instanceof UncorrelatedDevelopmentFactorsTest)
        double alpha = ((UncorrelatedDevelopmentFactorsTest)test).getAlpha()
        assertEquals(0.2, alpha, TestConfig.EPSILON)
    }
    
    @org.junit.Test
    public void testSummary_CalendarEffectTest() {
        StringWriter writer = new StringWriter()
        executor.setOutput(new PrintWriter(writer))
        
        runScript("summary(testCalendarEffect(factors, 0.2))\n")
        
        assertTrue(writer.toString().length() > 0)
    }
    
    @org.junit.Test
    public void testSummary_UncorrelatedDevelopmentFactors() {
        StringWriter writer = new StringWriter()
        executor.setOutput(new PrintWriter(writer))
        
        runScript("summary(testUncorrelatedDevFactors(factors, 0.2))\n")
        
        assertTrue(writer.toString().length() > 0)
    }
}

