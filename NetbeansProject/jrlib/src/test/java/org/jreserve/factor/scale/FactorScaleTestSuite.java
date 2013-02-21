package org.jreserve.factor.scale;

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
    DefaultLinkRatioScaleSelectionTest.class
})
public class FactorScaleTestSuite {
}