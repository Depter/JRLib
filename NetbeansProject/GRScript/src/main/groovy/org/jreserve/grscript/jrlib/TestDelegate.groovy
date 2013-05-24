/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.test.Test
import org.jreserve.jrlib.test.CalendarEffectTest
import org.jreserve.jrlib.test.UncorrelatedDevelopmentFactorsTest
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TestDelegate extends AbstractDelegate {
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.testCalendarEffect          << this.&testCalendarEffect
        emc.testUncorrelatedDevFactors  << this.&testUncorrelatedDevFactors
        emc.summary                     << this.&summary
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
        super.script.println getTestTitle(test)
        super.script.println "\t Test value: ${test.getTestValue()}"
        super.script.println "\t alpha: ${test.getAlpha()}"
        super.script.println "\t p-value: ${test.getPValue()}"
        super.script.println "\t passed: ${test.isTestPassed()}"
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

