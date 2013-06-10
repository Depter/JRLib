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
 * Adjusts the input residuals in order to compensate for
 * the bootstrap bias.
 * 
 * The adjustment factor `a` is calculated with the following formula:
 *               N
 *     adj^2 = -----
 *             N - p
 * where:
 * -   `N` is the number of cells, where the residual is not NaN.
 * -   `p` is equals to the number of accident periods in the source
 *     claims triangle, plus the number of development periods in
 *     the source triangle plus 1
 *     ({@link ClaimTriangle#getAccidentCount() cik.getAccidentCount()} + {@link ClaimTriangle#getDevelopmentCount() cik.getDevelopmentCount()} - 1).
* 
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedOdpScaledResidualTriangle
    extends AbstractTriangleModification<OdpScaledResidualTriangle> 
    implements ModifiedOdpScaledResidualTriangle {
    
    private double adjustment;
    private int accidents;
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpScaledResidualTriangle(OdpResidualScale source) {
        this(new DefaultOdpScaledResidualTriangle(source));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpScaledResidualTriangle(OdpScaledResidualTriangle source) {
        super(source);
        doRecalculate();
    }
    
    public double getAdjustment() {
        return adjustment;
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
        return source.getValue(accident, development) * adjustment;
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
        int devs = source.getDevelopmentCount();
        
        int[] pA = new int[accidents];
        int[] pD = new int[devs];
        
        int n = 0;
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<devs; d++) {
                if(!Double.isNaN(source.getValue(a, d))) {
                    n++;
                    pA[a] = 1;
                    pD[d] = 1;
                }
            }
        }
        
        double p = sum(pA) + sum(pD) - 1;
        double dN = n;
        adjustment = Math.sqrt(dN / (dN - p));
    }
    
    private int sum(int[] arr) {
        int sum = 0;
        for(int i : arr)
            sum += i;
        return sum;
    }
}
