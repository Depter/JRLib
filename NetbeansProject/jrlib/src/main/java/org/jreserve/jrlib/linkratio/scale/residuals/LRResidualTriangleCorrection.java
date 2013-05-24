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
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * Instances of LRResidualTriangleCorrection modifiying one value
 * of their source triangle, without modifying it's dimensions.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LRResidualTriangleCorrection 
    extends TriangleCorrection<LRResidualTriangle>
    implements ModifiedLRResidualTriangle {

    /**
     * Creates a new instance.
     * 
     * @throws NullPointerException if `source` or `cell` is null.
     */
    public LRResidualTriangleCorrection(LRResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    /**
     * Creates a new instance.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public LRResidualTriangleCorrection(LRResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
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