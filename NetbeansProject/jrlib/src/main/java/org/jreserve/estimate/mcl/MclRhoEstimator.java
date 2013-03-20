package org.jreserve.estimate.mcl;

import org.jreserve.util.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclRhoEstimator extends SelectableMethod<MclRho> {
    
    @Override
    public MclRhoEstimator copy();
}
