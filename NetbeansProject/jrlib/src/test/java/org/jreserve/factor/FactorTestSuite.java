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
    org.jreserve.factor.linkratio.LinkRatioTestSuite.class,
    org.jreserve.factor.curve.CurvesTestSuite.class,
    org.jreserve.factor.standarderror.FactorScaleTestSuite.class
})
public class FactorTestSuite {
}