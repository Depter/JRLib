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
    EstimateUtilTest.class,
    ChainLadderEstimateTest.class,
    MackEstimateTest.class
})
public class EstimateTestSuite {
}