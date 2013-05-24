/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.util.method.SelectableMethod;

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
}
