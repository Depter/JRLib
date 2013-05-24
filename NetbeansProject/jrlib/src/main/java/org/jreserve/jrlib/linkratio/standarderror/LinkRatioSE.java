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
package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * Calculates the standard error of a link-ratio for a given development period.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @see org.jreserve.jrlib.scale.Scale
 * @see LinkRatioSECalculator
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends Vector {
    
    /**
     * Returns Mack's sigma parameters, used for the calculation.
     */
    public LinkRatioScale getSourceLRScales();
    
    /**
     * Returns the input for the {@link LinkRatioScale LinkRatioScale}.
     */
    public LinkRatioScaleInput getSourceLrScaleInput();
    
    /**
     * Returns the LinkRatios used for this calculation.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the development factors used for this calculation.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the claims used for this calculation.
     */
    public ClaimTriangle getSourceTriangle();
}
