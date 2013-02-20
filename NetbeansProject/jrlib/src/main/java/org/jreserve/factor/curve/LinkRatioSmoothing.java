package org.jreserve.factor.curve;

import org.jreserve.factor.LinkRatio;
import org.jreserve.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothing extends LinkRatio, MethodSelection<LinkRatio, LinkRatioFunction> {
    
    @Override
    public LinkRatio getSource();
    
    /**
     * Sets the number of development periods. Calling this method fires a
     * change event.
     */
    public void setDevelopmentCount(int developments);
}
