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
package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualTriangle extends Triangle {

    public LinkRatio getSourceLinkRatios();
    
    public ClaimTriangle getSourceTriangle();
    
    /**
     * Retunrs the incremental fitted value for the given accident and 
     * development period. If the location falls outside of the bounds, 
     * then NaN is returned.
     */
    public double getFittedValue(int accident, int development);
    
    /**
     * Returns the array, containing the incremental fitted values. 
     * The returned array should have the same location as returned by
     * {@link Triangle#toArray() toArray()}.
     */
    public double[][] toArrayFittedValues();
}
