package org.jreserve.estimate.mcl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    RhoCalculatorTest.class,
    DefaultMclRhoSelectionTest.class,
    MclRhoErrorTriangleTest.class,
    MCLLambdaCalculatorTest.class
})
public class MclTestSuite {
}