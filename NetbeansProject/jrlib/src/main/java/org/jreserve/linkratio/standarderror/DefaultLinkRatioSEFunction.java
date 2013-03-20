package org.jreserve.linkratio.standarderror;

/**
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
