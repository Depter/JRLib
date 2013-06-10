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
package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Adjusts the input residuals in order to compensate for
 * the bootstrap bias.
 * 
 * The adjustment factor `a` is calculated with the following formula:
 * <pre>
 *               N
 *     adj^2 = -----
 *             N - p
 * </pre>
 * where:
 * <ul>
 * <li>`N` is the number of cells, where the residual is not NaN.</li>
 * <li>`p` is equals to the number of accident periods in the source
 *     claims triangle, plus the number of development periods in
 *     the source triangle plus 1
 *     ({@link ClaimTriangle#getAccidentCount() cik.getAccidentCount()} + {@link ClaimTriangle#getDevelopmentCount() cik.getDevelopmentCount()} - 1).</li>
 * </ul>
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedOdpResidualTriangle extends AbstractTriangleModification<OdpResidualTriangle> implements ModifiedOdpResidualTriangle {

    private double adjustment;
    private int accidents;
    
    /**
     * Creates an instance for the given link-ratios.
     * 
     * @throws NullPointerException if `source` is null.
     * @see InputOdpResidualTriangle#InputOdpResidualTriangle(LinkRatio) 
     */
    public AdjustedOdpResidualTriangle(LinkRatio lrs) {
        this(new InputOdpResidualTriangle(lrs));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpResidualTriangle(OdpResidualTriangle source) {
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
    
    public double getAdjustment() {
        return adjustment;
    }

    @Override
    public double getValue(int accident, int development) {
        double v = source.getValue(accident, development);
        return Double.isNaN(v)? Double.NaN : adjustment * v;
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