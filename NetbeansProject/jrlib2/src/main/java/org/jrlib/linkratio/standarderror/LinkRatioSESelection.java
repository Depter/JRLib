package org.jrlib.linkratio.standarderror;

import org.jrlib.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSESelection 
    extends LinkRatioSE, MethodSelection<LinkRatioSE, LinkRatioSEFunction> {
    
    @Override
    public LinkRatioSESelection copy();
}
