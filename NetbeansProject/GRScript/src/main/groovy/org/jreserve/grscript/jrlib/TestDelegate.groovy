package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.test.Test
import org.jreserve.jrlib.test.CalendarEffectTest
import org.jreserve.jrlib.test.UncorrelatedDevelopmentFactorsTest

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TestDelegate implements FunctionProvider {
    
    private Script script
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.testCalendarEffect          << this.&testCalendarEffect
        emc.testUncorrelatedDevFactors  << this.&testUncorrelatedDevFactors
        emc.summary                     << this.&summary
        this.script = script
    }
    
    Test testCalendarEffect(ClaimTriangle claims) {
        return new CalendarEffectTest(claims)
    }
    
    Test testCalendarEffect(ClaimTriangle claims, double alpha) {
        return new CalendarEffectTest(claims, alpha)
    }
    
    Test testCalendarEffect(FactorTriangle factors) {
        return new CalendarEffectTest(factors)
    }
    
    Test testCalendarEffect(FactorTriangle factors, double alpha) {
        return new CalendarEffectTest(factors, alpha)
    }
    
    Test testUncorrelatedDevFactors(ClaimTriangle claims) {
        return new UncorrelatedDevelopmentFactorsTest(claims)
    }
    
    Test testUncorrelatedDevFactors(ClaimTriangle claims, double alpha) {
        return new UncorrelatedDevelopmentFactorsTest(claims, alpha)
    }
    
    Test testUncorrelatedDevFactors(FactorTriangle factors) {
        return new UncorrelatedDevelopmentFactorsTest(factors)
    }
    
    Test testUncorrelatedDevFactors(FactorTriangle factors, double alpha) {
        return new UncorrelatedDevelopmentFactorsTest(factors, alpha)
    }
	
    void summary(Test test) {
        test.getTestValue()
        test.getAlpha()
        test.getPValue()
        test.isTestPassed()
        script.println getTestTitle(test)
        script.println "\t Test value: ${test.getTestValue()}"
        script.println "\t alpha: ${test.getAlpha()}"
        script.println "\t p-value: ${test.getPValue()}"
        script.println "\t passed: ${test.isTestPassed()}"
    }
    
    private String getTestTitle(Test test) {
        switch(test?.getClass()) {
            case CalendarEffectTest.class:
                return "Test for calendar year effect"
            case UncorrelatedDevelopmentFactorsTest.class:
                return "Test for uncorrelatednes of subsequent development factors"
            default:
                return "Unknown test"
        }
    }
}

