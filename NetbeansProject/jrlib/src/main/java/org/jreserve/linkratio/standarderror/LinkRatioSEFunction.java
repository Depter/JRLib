package org.jreserve.linkratio.standarderror;

import org.jreserve.util.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSEFunction extends SelectableMethod<LinkRatioSE> {
    
    @Override
    public void fit(LinkRatioSE ses);
    
    @Override
    public double getValue(int development);
    
    @Override
    public LinkRatioSEFunction copy();
}
