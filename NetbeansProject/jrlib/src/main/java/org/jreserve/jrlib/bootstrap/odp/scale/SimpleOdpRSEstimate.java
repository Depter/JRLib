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
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * Simple estimator for the ODP residual scales, which fills in all
 * NaNs with the same method.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleOdpRSEstimate extends AbstractSimpleMethodSelection<OdpResidualScale, OdpRSMethod> implements OdpResidualScale {
    
    /**
     * Creates an instance for the given source, using the given method to
     * fill NaN values.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleOdpRSEstimate(OdpResidualScale source, OdpRSMethod method) {
        super(source, 
              (method instanceof DefaultOdpRSMethod)? method : new DefaultOdpRSMethod(),
              method);
        super.recalculateLayer();
    }
    
    public OdpResidualScale getSourceOdpResidualScales() {
        return source;
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

    @Override
    protected void initCalculation() {
        length = source.getLength();
    }
}
