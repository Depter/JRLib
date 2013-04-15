package org.jrlib.scale;

import org.jrlib.util.method.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ScaleSelection<T extends ScaleInput> extends Scale<T>, MethodSelection<Scale<T>, ScaleEstimator<T>> {
}
