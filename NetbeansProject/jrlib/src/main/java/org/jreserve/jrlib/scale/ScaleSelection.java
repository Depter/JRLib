package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.util.method.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ScaleSelection<T extends ScaleInput> extends Scale<T>, MethodSelection<Scale<T>, ScaleEstimator<T>> {
}
