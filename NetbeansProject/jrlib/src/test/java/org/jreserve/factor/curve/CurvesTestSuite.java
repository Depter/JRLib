package org.jreserve.factor.curve;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    InversePowerLRFunctionTest.class,
    ExponentialLRFunctionTest.class,
    PowerLRFunctionTest.class,
    WeibulLRFunctionTest.class,
    DefaultLRFunctionTest.class,
    SimpleLinkRatioSmoothingTest.class,
    DefaultLinkRatioSmoothingTest.class
})
public class CurvesTestSuite {
}