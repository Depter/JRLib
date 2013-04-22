package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 * A link-ratio smoothing allows the use of curves to smooth link-ratios and
 * calculate tail factors.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothing extends LinkRatio {
    
    /**
     * Returns the link-ratios, used as source for this calculation.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Sets the number of link-ratios (i.e. tail-factors). 
     */
    public void setDevelopmentCount(int developments);
}
