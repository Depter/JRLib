package org.jrlib.scale;

import org.jrlib.util.SelectableMethod;

/**
 * Implementation of this interface can estimate the scale parameters
 * where needed. Such situation emerges for example when `n` is less then
 * 2 (for example by tail factors).
 * 
 * @see Scale
 * @author Peter Decsi
 * @version 1.0
 */
public interface ScaleEstimator<T extends ScaleInput> extends SelectableMethod<Scale<T>> {
    
    @Override
    public ScaleEstimator<T> copy();

}
