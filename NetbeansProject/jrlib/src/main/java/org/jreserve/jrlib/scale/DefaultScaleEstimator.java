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
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * This class simply mirrors the fitted {@link Scale Scale}. For 
 * development periods, where there is no input scale returns 
 * Double.NaN.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleEstimator<T extends ScaleInput> implements ScaleEstimator<T> {
    
    private double[] scales;
    private int developments;
    
    @Override
    public void fit(Scale<T> source) {
        scales = source.toArray();
        developments = source.getLength();
    }

    /**
     * This class simply mirrors the fitted {@link Scale Scale}. For 
     * development periods, where there is no input scale returns 
     * Double.NaN.
     */
    @Override
    public double getValue(int development) {
        if(0 <= development && development < developments)
            return scales[development];
        return Double.NaN;
    }

    /**
     * The method copies the inner state (length and values) of the given 
     * original instance.
     * 
     * @throws NullPointerException if `original` is null.
     */
    protected void copyStateFrom(DefaultScaleEstimator<T> original) {
        developments = original.developments;
        if(developments > 0)
            scales = TriangleUtil.copy(original.scales);
    }
}
