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
package org.jreserve.jrlib.triangle.factor;

import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * DevelopmentFactors are the first links in calculating link-ratios. An
 * instance of this class takes a {@link ClaimTriangle ClaimTriangle} as
 * input and calculates the development factors.
 * 
 *               c(a,d+1)
 *      f(a,d) = --------
 *                c(a,d)
 * where:
 * -   `f(a,d)` is the development factor for accident period `a` and
 *     development period `d`.
 * -   `c(a,d)` is the value from the claim triangle for accident period 
 *     `a` and development period `d`.
 * 
 * A factor triangle always has one less development period then the 
 * claim triangle for each accident period. If the claim triangle 
 * contained only one development period for it's last accident period, 
 * the factors triangle will also contain one less accident periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentFactors extends AbstractTriangle<ClaimTriangle> implements FactorTriangle {
    
    private double[][] factors;
    private int accidents;
    private int developments;

    /**
     * Creates an instance for the given claim triangle.
     */
    public DevelopmentFactors(ClaimTriangle source) {
        super(source);
        doRecalculate();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
    }
    
    /**
     * Returns the number of accident periods. If the claim triangle 
     * contained only one development period for it's last accident 
     * period, the factors triangle will also contain one less accident 
     * periods.
     */
    @Override
    public int getAccidentCount() {
        return accidents;
    }
    
    /**
     * Retuns the number of development periods. A factor triangle always 
     * has one less development period then the claim triangle for each 
     * accident period.
     */
    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    /**
     * Retuns the number of development periods in the given accident
     * period. A factor triangle always has one less development period 
     * then the claim triangle for each accident period.
     */
    @Override
    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return factors[accident].length;
        return 0;
    }
    
    /**
     * Returns the development factor for the given accident and 
     * development period.
     * 
     *               c(a,d+1)
     *      f(a,d) = --------
     *                c(a,d)
     * where:
     * -   `f(a,d)` is the development factor for accident period `a` and
     *     development period `d`.
     * -   `c(a,d)` is the value from the claim triangle for accident period 
     *     `a` and development period `d`.
     */
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return factors[accident][development];
        return Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        recalculateDevelopments();
        recalculateAccidents();
        if(accidents > 0 && developments > 0) {
            recalculateFactors();
        } else {
            factors = new double[0][];
        }
    }
    
    private void recalculateDevelopments() {
        developments = source==null? 0 : source.getDevelopmentCount() - 1;
        if(developments < 0)
            developments = 1;
    }
    
    private void recalculateAccidents() {
        if(developments == 0 || source==null) {
            accidents = 0;
        } else {
            accidents = source.getAccidentCount();
            while(accidents>0 && source.getDevelopmentCount(accidents-1) < 2)
                accidents--;
        }
    }
    
    private void recalculateFactors() {
        double[][] input = getSourceData();
        factors = new double[accidents][];
        for(int a=0; a<accidents; a++)
            factors[a] = calculateFactors(input[a]);
    }
    
    private double[][] getSourceData() {
        if(source == null)
            return new double[0][];
        return source.toArray();
    }
    
    private double[] calculateFactors(double[] input) {
        int size = Math.max(input.length-1, 0);
        double[] result = new double[size];
        for(int d=0; d<size; d++) {
            double denom = input[d];
            result[d] = (denom==0d)? Double.NaN : input[d+1]/input[d];
        }
        return result;
    }
}
