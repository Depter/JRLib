package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.LinkRatio;
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
}
