package org.jreserve.factor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    DevelopmentFactorsTest.class,
    MinLRMethodTest.class,
    MaxLRMethodTest.class,
    AverageLRMethodTest.class,
    WeightedAverageLRMethodTest.class,
    MackRegressionLRMethodTest.class,
    DefaultLinkRatioSelectionTest.class,
    SimpleLinkRatioTest.class,
    org.jreserve.factor.curve.CurvesTestSuite.class,
    org.jreserve.factor.scale.FactorScaleTestSuite.class
})
public class FactorTestSuite {
}