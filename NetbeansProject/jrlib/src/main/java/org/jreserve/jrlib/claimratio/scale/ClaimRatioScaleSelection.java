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
package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.scale.ScaleSelection;

/**
 * Implementations of this interface enables to use different methods
 * to calculate rhos for different development periods (for example
 * to manually set a value for a given development period).
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioScaleSelection 
    extends ClaimRatioScale, ScaleSelection<ClaimRatioScaleInput> {
}
