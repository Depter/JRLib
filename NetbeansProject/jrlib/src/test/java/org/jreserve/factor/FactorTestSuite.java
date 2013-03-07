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
    AbstractFactorTriangleModificationTest.class,
    FactorTriangleCorrectionTest.class,
    org.jreserve.factor.linkratio.LinkRatioTestSuite.class
})
public class FactorTestSuite {
}