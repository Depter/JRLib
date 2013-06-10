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

import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.AbstractVector;

/**
 * EmptyOdpResidualScale has a scale parameter of 1, for each valid 
 * development period, and NaN for development periods outside the boudns.
 * Valid developmetn periods are 
 * <pre>
 * `0 <= d && dt < {@link OdpResidualTriangle#getDevelopmentCount() source.getDevelopmentCount()}`.
 * </pre>
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyOdpResidualScale 
    extends AbstractVector<OdpResidualTriangle> 
    implements OdpResidualScale {

    private final static double SCALE = 1d;

    public EmptyOdpResidualScale(OdpResidualTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source;
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
    
    @Override
    public double getValue(int development) {
        return (0<=development && development<length)? 
                SCALE : 
                Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        length = source.getDevelopmentCount();
    }

}
