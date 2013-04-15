package org.jrlib.linkratio.standarderror;

import org.jrlib.util.method.SelectableMethod;

/**
 * Implementations of this interfae are able to extrapolate the standard error
 * of a link-ratio for a given development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSEFunction extends SelectableMethod<LinkRatioSE> {
}
