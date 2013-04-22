package org.jreserve.jrlib.linkratio.standarderror;

/**
 * This class simply mirrors the fitted {@link LinkRatioSE LinkRatioSE}. 
 * For development periods, where there is no input link-ratio scale NaN
 * is returned.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSEFunction implements LinkRatioSEFunction {
    
    private LinkRatioSE source;
    
    @Override
    public void fit(LinkRatioSE source) {
        this.source = source;
    }
    
    @Override
    public double getValue(int development) {
        return source.getValue(development);
    }
    
    public DefaultLinkRatioSEFunction copy() {
        DefaultLinkRatioSEFunction copy = new DefaultLinkRatioSEFunction();
        copy.source = source;
        return copy;
    }
}
