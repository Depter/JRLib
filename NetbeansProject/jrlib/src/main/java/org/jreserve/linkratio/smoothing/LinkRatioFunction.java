package org.jreserve.linkratio.smoothing;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.util.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioFunction extends SelectableMethod<LinkRatio> {
    
    @Override
    public void fit(LinkRatio lr);
    
    /**
     * Retunrs the smoothed link ratio for the given development period. It is
     * important, that the index here is 1 based!.
     * 
     */
    @Override
    public double getValue(int development);
    
    @Override
    public LinkRatioFunction copy();
}
