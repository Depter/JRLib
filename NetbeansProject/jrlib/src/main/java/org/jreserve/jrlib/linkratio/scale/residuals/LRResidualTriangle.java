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
package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * LRResidualTriangle are triangles, containing scaled residuals of
 * link ratios.
 * 
 * @see org.jreserve.jrlib.scale.residuals.ScaleResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public interface LRResidualTriangle extends Triangle {

    /**
     * Returns the {@link LinkRatioScale LinkRatioScale} used to
     * scale the residuals.
     */
    public LinkRatioScale getSourceLinkRatioScales();
    
    /**
     * Returns the {@link LinkRatio LinkRatios} used to
     * calculate the residuals.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the {@link FactorTriangle FactorTriangle} used to
     * calculate the link-ratios.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used to
     * calculate the link-ratios.
     */
    public ClaimTriangle getSourceTriangle();
}
