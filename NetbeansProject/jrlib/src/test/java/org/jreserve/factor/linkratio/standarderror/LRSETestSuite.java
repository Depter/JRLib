package org.jreserve.factor.linkratio.standarderror;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    LinkRatioSECalculatorTest.class,
    DefaultLinkRatioSEFunctionTest.class,
    DefaultLinkRatioSESelectionTest.class
})
public class LRSETestSuite {
}