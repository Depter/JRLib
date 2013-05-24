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
package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.util.method.MethodSelection;

/**
 * A ClaimRatioSelection allows the use of different calculation methods
 * for different development periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioSelection extends ClaimRatio, MethodSelection<ClaimRatio, ClaimRatioMethod> {
    
    /**
     * Returns the claim-ratios used by this instance.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Sets the number of development periods, for which an
     * estimate is maid.
     */
    public void setDevelopmentCount(int developments);

}
