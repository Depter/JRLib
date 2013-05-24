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

import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.scale.ScaleInput;
import org.jreserve.jrlib.triangle.AbstractTriangle;

/**
 * Base class, that calculates a residual triangle based on a 
 * {@link Scale Scale} implementation.
 * 
 * The values are calculated as follows:
 * <pre>
 *     res(a,d) = w(a,d)^0.5 * (r(a,d) - r(d)) / s(d)
 * </pre>
 * where:
 * <ul>
 * <li>`res(a,d)` is the calculated residual for accident period `a` and
 *      development period `d`.</li>
 * <li>`w(a,d)` is the used weight, returned by 
 *     {@link ScaleInput#getWeight(int, int) ScaleInput.getWeight(a,d)}.</li>
 * <li>`r(a,d)` is the observed ratio returned by 
 *     {@link ScaleInput#getRatio(int, int) ScaleInput.getRatio(a,d)}.</li>
 * <li>`r(d)` is the estimated ratio returned by 
 *     {@link ScaleInput#getRatio(int) ScaleInput.getRatio(d)}.</li>
 * <li>`s(d)` is the scale parameter returned by
 *     {@link Scale#getValue(int) Scale.getValue(d)}.</li>
 * </ul>
 * 
 * The dimensions of the resulting triangle are decided based on the values 
 * obtained from {@link ScaleInput#getAccidentCount() ScaleInput.getAccidentCount()} and
 * {@link ScaleInput#getDevelopmentCount(int) ScaleInput.getDevelopmentCount(accident)}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleResidualTriangle<T extends ScaleInput, S extends Scale<T>> extends AbstractTriangle<S> {
    
    private T scaleInput;
    private int accidents;
    private double[][] values;
    
    /**
     * Creates a new instance with the given source.
     * 
     * @throws NullPointerException when `source` is null.
     */
    public ScaleResidualTriangle(S source) {
        super(source);
        this.scaleInput = source.getSourceInput();
        doRecalculate();
    }

    /**
     * Returns the number of accident periods in the triangle.
     * 
     * @see ScaleInput#getAccidentCount().
     */
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    /**
     * Returns the number of development periods in the triangle.
     * 
     * @see ScaleInput#getDevelopmentCount().
     */
    @Override
    public int getDevelopmentCount() {
        return (accidents > 0)? values[0].length : 0;
    }

    /**
     * Returns the number of development periods for the given 
     * accident period in the triangle.
     * 
     * @see ScaleInput#getDevelopmentCount(int).
     */
    @Override
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? values[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                values[accident][development] :
                Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        accidents = recalculateAccidentCounts();
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = calculateResiduals(a);
    }
    
    /**
     * Called when the residuals are recalculated. Default
     * implementation simple calls
     * {@link ScaleInput#getAccidentCount() ScaleInput.getAccidentCount()}.
     * 
     * @return the number of accident periods in the residual triangle.
     */
    protected int recalculateAccidentCounts() {
        return scaleInput.getAccidentCount();
    }
    
    private double[] calculateResiduals(int accident) {
        int devs = scaleInput.getDevelopmentCount(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++) 
            result[d] = calculateResidual(accident, d);
        return result;
    }
    
    /**
     * Called when the residuals are recalculated. Default
     * implementation simple calls
     * {@link ScaleInput#getDevelopmentCount(int) ScaleInput.getDevelopmentCount(accident)}.
     * 
     * @return the number of residuals in the accident period.
     */
    protected int recalculateDevelopmentCount(int accident) {
        return scaleInput.getDevelopmentCount(accident);
    }

    private double calculateResidual(int accident, int development) {
        double wad = scaleInput.getWeight(accident, development);
        double rad = scaleInput.getRatio(accident, development);
        double rd = scaleInput.getRatio(development);
        double scale = source.getValue(development);
        return Math.sqrt(wad) * (rad - rd) / scale;
    }
}
