package org.jreserve.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MathUtilTest.class,
    RegressionUtilTest.class,
    SigmaFilterTest.class,
    AbstractCalculationDataFilterTest.class,
    AbstractMethodSelectionTest.class
})
public class UtilTestSuite {
}