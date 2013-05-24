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
package org.jreserve.jrlib.bootstrap.odp.scaledresiduals;

import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpScaledResidualTriangle extends Triangle {
    
    /**
     * Returns the source, used to scale the input.
     */
    public OdpResidualScale getSourceOdpResidualScales();
    
    /**
     * Retunrs the residuals used to calculate the scales.
     */
    public OdpResidualTriangle getSourceOdpResidualTriangle();
    
    /**
     * Retunrs the link-ratios used to calculate the residuals.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the claims, used to calculate the link-ratios.
     */
    public ClaimTriangle getSourceTriangle();
    
    /**
     * Retunrs the fitted value for the given accident
     * and development period.
     */
    public double getFittedValue(int accident, int development);
    
    /**
     * Returns the fitted values.
     */
    public double[][] toArrayFittedValues();
}
