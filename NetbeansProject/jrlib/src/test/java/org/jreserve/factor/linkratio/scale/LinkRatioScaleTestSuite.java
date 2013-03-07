package org.jreserve.factor.linkratio.scale;

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
    SimpleLinkRatioScaleTest.class,
    DefaultLinkRatioScaleSelectionTest.class
})
public class LinkRatioScaleTestSuite {
}