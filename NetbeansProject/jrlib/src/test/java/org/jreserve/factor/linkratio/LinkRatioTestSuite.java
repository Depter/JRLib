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
    DefaultLinkRatioSelectionTest.class,
    SimpleLinkRatioTest.class
})
public class LinkRatioTestSuite {
}