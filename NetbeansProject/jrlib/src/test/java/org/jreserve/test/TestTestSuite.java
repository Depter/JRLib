package org.jreserve.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    NormalUtilTest.class,
    UncorrelatedDevelopmentFactorsTestTest.class,
    CalendarEffectTestTest.class
})
public class TestTestSuite {
}