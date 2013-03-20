package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothing extends LinkRatio {
    
    public LinkRatio getSourceLinkRatios();
    
    public void setDevelopmentCount(int developments);
}
