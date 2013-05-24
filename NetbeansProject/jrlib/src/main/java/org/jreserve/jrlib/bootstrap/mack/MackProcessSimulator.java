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
package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.estimate.Estimate;

/**
 * MackProcessSimulators calculate the process variance for development
 * period `d` from the claim and scale parameter from development period
 * `d-1`. The mean and variance are calculated as follows:
 * <p><pre>
 *     mean(a,d) = C(a,d)
 *     var(a,d) = sigma(d-1)^2 * C(a,d-1)
 * </pre>
 * where:
 * <ul>
 *   <li>`C(a,d)` is the observed/estimated cummulated claim for accident 
 *       period 'a' and development period `d`.
 *   </li>
 *   <li>`sigma(d)` is Mack's scale parameter {@link org.jreserve.jrlib.linkratio.scale.LinkRatioScale LinkRatioScale}
 *       for development period `d`.
 *   </li>
 * </ul>
 * </p>
 * @author Peter Decsi
 * @version 1.0
 */
public interface MackProcessSimulator extends ProcessSimulator {

    /**
     * Sets the used estimates.
     * 
     * @throws NullPointerException if `estimate` is null.
     */
    public void setEstimate(Estimate estimate);
    
}
