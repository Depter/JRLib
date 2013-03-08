package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothing extends LinkRatio {
    
    public LinkRatio getSourceLinkRatios();
    
    public void setDevelopmentCount(int developments);
}
