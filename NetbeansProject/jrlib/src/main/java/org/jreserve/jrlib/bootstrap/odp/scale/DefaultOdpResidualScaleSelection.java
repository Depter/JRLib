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
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.util.method.AbstractMethodSelection;

/**
 * DefaultOdpResidualScaleSelection allows to use different 
 * {@link OdpRSMethod OdpRSMethods} for different development
 * periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultOdpResidualScaleSelection 
    extends AbstractMethodSelection<OdpResidualScale, OdpRSMethod> 
    implements OdpResidualScaleSelection {
    
    private int developments;
    private double[] values;

    /**
     * Creates an instance for the given input, which uses a
     * {@link DefaultOdpRSMethod DefaultOdpRSMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultOdpResidualScaleSelection(OdpResidualScale source) {
        this(source, new DefaultOdpRSMethod());
    }

    /**
     * Creates an instance for the given input, which uses the supplied 
     * {@link OdpRSMethod OdpRSMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultOdpResidualScaleSelection(OdpResidualScale source, OdpRSMethod defaultMethod) {
        super(source, defaultMethod);
        doRecalculate();
    }
    
    @Override
    public OdpResidualScale getOdpSourceResidualScale() {
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
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        return (0<=development && development<developments)? 
                values[development] : 
                Double.NaN;
    }

    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }


    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        developments = source.getLength();
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
}
