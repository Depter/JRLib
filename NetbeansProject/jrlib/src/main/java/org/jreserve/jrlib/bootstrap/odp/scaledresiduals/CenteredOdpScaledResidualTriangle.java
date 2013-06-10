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
import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Centers the input triangle in such a way, that the mean
 * of the residuals will be 0. The mean of the non NaN residuals
 * will be subtracted from each non NaN value.
 * 
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class CenteredOdpScaledResidualTriangle
    extends AbstractTriangleModification<OdpScaledResidualTriangle>
    implements ModifiedOdpScaledResidualTriangle {

    private double mean;
    private int accidents;
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public CenteredOdpScaledResidualTriangle(OdpResidualScale source) {
        this(new DefaultOdpScaledResidualTriangle(source));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public CenteredOdpScaledResidualTriangle(OdpScaledResidualTriangle source) {
        super(source);
        doRecalculate();
    }
    
    /**
     * Returns the mean of the input residuals.
     */
    public double getMean() {
        return mean;
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
    
    @Override
    public double getValue(int accident, int development) {
        return source.getValue(accident, development) - mean;
    }

    @Override
    protected boolean withinBounds(int accident) {
        return 0<=accident && accident<accidents;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        accidents = source.getAccidentCount();
        int n = 0;
        double sum = 0d;
        
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double v = source.getValue(a, d);
                if(!Double.isNaN(v)) {
                    sum +=v;
                    n++;
                }
            }
        }
        
        mean = n==0? 0d : sum / (double)n;
    }
}
