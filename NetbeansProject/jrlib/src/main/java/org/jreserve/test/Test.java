package org.jreserve.test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Test {
    
    /**
     * Returns true, when the given test passes with an appropriate result.
     */
    public boolean isTestPassed();
    
    /**
     * Returns the test value.
     */
    public double getTestValue();
    
    /**
     * Returns the alpha used to decide wether the test passed or not.
     */
    public double getAlpha();
    
    /**
     * Returns the p-value of the test.
     */
    public double getPValue();
}