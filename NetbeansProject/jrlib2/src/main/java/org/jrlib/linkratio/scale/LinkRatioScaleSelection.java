package org.jrlib.linkratio.scale;

import org.jrlib.scale.ScaleSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScaleSelection 
    extends LinkRatioScale, ScaleSelection<LinkRatioScaleInput> {
    
    @Override
    public LinkRatioScaleSelection copy();
}
