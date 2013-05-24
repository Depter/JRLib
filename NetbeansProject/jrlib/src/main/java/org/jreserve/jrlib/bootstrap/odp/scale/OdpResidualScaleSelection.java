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
package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.util.method.MethodSelection;

/**
 * Instances of this interface are able to extrapolate/interpolate residual
 * scales with different methods per development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualScaleSelection extends MethodSelection<OdpResidualScale, OdpRSMethod>, OdpResidualScale {
    
    /**
     * Retunrs the {@link OdpResidualScale OdpResidualScale} being 
     * extrapolated/interpolated by this instance.
     */
    public OdpResidualScale getOdpSourceResidualScale();
}