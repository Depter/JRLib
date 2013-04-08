package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothingSelection extends LinkRatioSmoothing, MethodSelection<LinkRatio, LinkRatioCurve> {
    
    @Override
    public LinkRatioSmoothingSelection copy();
}
