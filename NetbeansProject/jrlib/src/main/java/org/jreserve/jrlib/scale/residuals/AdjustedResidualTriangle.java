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
package org.jreserve.jrlib.scale.residuals;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * Adjust the residuals for bootstrap-bias. A separate adjustment factor is 
 * calculated for each development period, with the following formula
 * <pre>
 *             n
 *      a^2 = ---, if n > 1
 *            n-1
 *      a = 0, if n <= 1
 * </pre>
 * where 'n' is the number of non NaN values in the development period.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedResidualTriangle<T extends Triangle> 
    extends AbstractTriangleModification<T> {
    
    private double[] adjustments;
    private int accidents;
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedResidualTriangle(T source) {
        super(source);
        doRecalculate();
    }
    
    public double getAdjustmentFactor(int development) {
        return 0 <= development && development < getDevelopmentCount()?
                adjustments[development] :
                Double.NaN;
    }
    
    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                source.getValue(accident, development) * adjustments[development] :
                Double.NaN;
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
        int developments = source.getDevelopmentCount();
        accidents = source.getAccidentCount();
        adjustments = new double[developments];
        for(int d=0; d<developments; d++)
            recalculateAdjustment(accidents, d);
    }
    
    private void recalculateAdjustment(int accidents, int development) {
        int n = 0;
        for(int a=0; a<accidents; a++)
            if(!Double.isNaN(source.getValue(a, development)))
                n++;
        if(n > 1)
            adjustments[development] = calculateAdjustment(n);
    }
    
    private double calculateAdjustment(double n) {
        return Math.sqrt(n / (n-1));
    }
}
