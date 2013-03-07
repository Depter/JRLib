package org.jreserve.factor.linkratio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MinLRMethodTest.class,
    MaxLRMethodTest.class,
    AverageLRMethodTest.class,
    WeightedAverageLRMethodTest.class,
    MackRegressionLRMethodTest.class,
    AbstractVectorUserInputTest.class,
    UserInputLRMethodTest.class,
    DefaultLinkRatioSelectionTest.class,
    SimpleLinkRatioTest.class,
    org.jreserve.factor.linkratio.curve.CurvesTestSuite.class,
    org.jreserve.factor.linkratio.scale.LinkRatioScaleTestSuite.class,
    org.jreserve.factor.linkratio.standarderror.LRSETestSuite.class
})
public class LinkRatioTestSuite {
}