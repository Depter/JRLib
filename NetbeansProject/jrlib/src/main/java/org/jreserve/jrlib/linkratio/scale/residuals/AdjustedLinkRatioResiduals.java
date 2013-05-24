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
import org.jreserve.jrlib.scale.residuals.AdjustedResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * Adjust the source residual triangle.
 * 
 * @see AdjustedResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedLinkRatioResiduals 
    extends AdjustedResidualTriangle<LRResidualTriangle> 
    implements ModifiedLRResidualTriangle {
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedLinkRatioResiduals(LinkRatioScale source) {
        this(new LinkRatioResiduals(source));
    }
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedLinkRatioResiduals(LRResidualTriangle source) {
        super(source);
    }

    @Override
    public LRResidualTriangle getSourceLRResidualTriangle() {
        return source;
    }

    @Override
    public LinkRatioScale getSourceLinkRatioScales() {
        return source.getSourceLinkRatioScales();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
}