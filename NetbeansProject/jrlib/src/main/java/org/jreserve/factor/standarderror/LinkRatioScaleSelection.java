package org.jreserve.factor.standarderror;

import org.jreserve.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScaleSelection extends LinkRatioScale, MethodSelection<LinkRatioScale, LinkRatioScaleEstimator> {
    
    /**
     * Sets the development count for the link ratio selection.
     * If <i>developments</i> is less than 0, 0 should be used.
     * 
     * <p>Calling this method should fire a change event.</p>
     */
    public void setDevelopmentCount(int developments);
}
