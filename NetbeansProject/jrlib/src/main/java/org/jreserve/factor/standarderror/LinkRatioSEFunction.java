package org.jreserve.factor.standarderror;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSEFunction {
    
    public void fit(LinkRatioScale scales);
    
    public double getValue(int development);
}
