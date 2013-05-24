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
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpScaledResidualTriangleCorrection 
    extends TriangleCorrection<OdpScaledResidualTriangle> 
    implements ModifiedOdpScaledResidualTriangle {
    
    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if `source` or `cell` is null.
     */
    public OdpScaledResidualTriangleCorrection(OdpScaledResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public OdpScaledResidualTriangleCorrection(OdpScaledResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    @Override
    public OdpScaledResidualTriangle getSourceOdpScaledResidualTriangle() {
        return source;
    }

    @Override
    public OdpResidualScale getSourceOdpResidualScales() {
        return source.getSourceOdpResidualScales();
    }

    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source.getSourceOdpResidualTriangle();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public double getFittedValue(int accident, int development) {
        return source.getFittedValue(accident, development);
    }

    @Override
    public double[][] toArrayFittedValues() {
        return source.toArrayFittedValues();
    }
}
