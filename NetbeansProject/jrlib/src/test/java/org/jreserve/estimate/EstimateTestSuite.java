package org.jreserve.estimate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 * @Version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AbstractEstimateTest.class,
    EstimateUtilTest.class,
    ChainLadderEstimateTest.class,
    ExpectedLossRatioEstimateTest.class,
    AverageCostEstimateTest.class,
    BornhuetterFergusonEstimateTest.class,
    MackEstimateTest.class,
    org.jreserve.estimate.mcl.MclTestSuite.class
})
public class EstimateTestSuite {
}