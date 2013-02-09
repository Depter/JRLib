package org.jreserve.factor.curve;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    RegressionTest.class,
    InversePowerLRFunctionTest.class,
    ExponentialLRFunctionTest.class,
    PowerLRFunctionTest.class
})
public class CurvesTestSuite {
}