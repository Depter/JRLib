package org.jreserve.factor.standarderror;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    LinkRatioScaleCalculatorTest.class,
    LinkRatioScaleExtrapolationTest.class,
    LinkRatioScaleMinMaxEstimateTest.class,
    DefaultLinkRatioScaleSelectionTest.class,
    DefaultLinkRatioSEFunctionTest.class
})
public class FactorScaleTestSuite {
}