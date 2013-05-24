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
package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.vector.AbstractVector;

/**
 * Calculates the ratio as the ratio of column totals in the input triangles.
 * 
 * The formula used to calculate the ratio `r(d)` for development period `d` is:
 * <pre>
 *             sum(R(a,d) * D(a,d))
 *      r(d) = --------------------
 *                 sum(D(a,d))
 * </pre>
 * where:<ul>
 * <li>`R(a,d)` is the value from the {@link RatioTriangle RatioTriangle} 
 *     triangle for accident period `a` and development period `d`.</li>
 * <li>`D(a,d)` is the value from the 
 *     {@link RatioTriangle#getSourceDenominatorTriangle() denominator} 
 *     triangle for accident period `a` and development period `d`.</li>
 * <li>If either `R(a,d)`, `D(a,d)` is NaN, then the cell is ignored.</li>
 * </ul>
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioCalculator extends AbstractVector<RatioTriangle> implements ClaimRatio {

    private int developments;
    private double[] values;
    
    /**
     * Creates a new instance from the given source.
     * 
     * @see DefaultRatioTriangle#DefaultRatioTriangle(ClaimTriangle, ClaimTriangle)  
     * @throws NullPointerException if one of the parameters is null.
     */
    public ClaimRatioCalculator(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(new DefaultRatioTriangle(numerator, denominator));
    }
    
    /**
     * Creates a new instance from the given source.
     * 
     * @see DefaultRatioTriangle#DefaultRatioTriangle(RatioTriangleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public ClaimRatioCalculator(RatioTriangleInput source) {
        this(new DefaultRatioTriangle(source));
    }
    
    /**
     * Creates a new instance from the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public ClaimRatioCalculator(RatioTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }

    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    @Override
    public int getLength() {
        return developments;
    }

    public double getValue(int development) {
        return withinBonds(development)? values[development] : Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        ClaimTriangle dik = source.getSourceDenominatorTriangle();
        developments = source.getDevelopmentCount();
        values = new double[developments];
        
        int accidents = source.getAccidentCount();
        for(int d=0; d<developments; d++) {
            double sr = 0d;
            double sw = 0d;
            for(int a=0; a<accidents; a++) {
                double r = source.getValue(a, d);
                double w = dik.getValue(a, d);
                if(!Double.isNaN(r) && !Double.isNaN(w)) {
                    sr += r * w;
                    sw += w;
                }
            }
            
            values[d] = sr / sw;
        }
    }
}