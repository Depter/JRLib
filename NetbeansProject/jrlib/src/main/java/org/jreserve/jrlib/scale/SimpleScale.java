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

import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * This class takes an instance of {@link Scale Scale} calculation
 * and fills in the missing values (NaNs) with a given 
 * {@link ScaleEstimator ScaleEstimator}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleScale<T extends ScaleInput> extends AbstractSimpleMethodSelection<Scale<T>, ScaleEstimator<T>> implements Scale<T> {
    
    /**
     * Creates an instance where the origina scales are calculated with
     * {@link DefaultScaleCalculator DefaultScaleCalculator}, and the 
     * missing values are filled in with 
     * {@link MinMaxScaleEstimator MinMaxScaleEstimator}.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleScale(T source) {
        this(new DefaultScaleCalculator<T>(source));
    }

    /**
     * Creates an instance where the origina scales are calculated with
     * {@link DefaultScaleCalculator DefaultScaleCalculator}, and the 
     * missing values are filled in with the supplied
     * {@link ScaleEstimator ScaleEstimator}.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `estimator` is null.
     */
    public SimpleScale(T source, ScaleEstimator<T> estimator) {
        this(new DefaultScaleCalculator<T>(source), estimator);
    }

    /**
     * Creates an instance where the missing values are filled in with 
     * {@link MinMaxScaleEstimator MinMaxScaleEstimator}.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleScale(Scale<T> source) {
        this(source, new MinMaxScaleEstimator<T>());
    }
    
    /**
     * Creates an instance where the missing values are filled in with 
     * the supplied {@link ScaleEstimator ScaleEstimator}.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `estimator` is null.
     */
    public SimpleScale(Scale<T> source, ScaleEstimator<T> estimator) {
        super(source, new DefaultScaleEstimator<T>(), estimator);
    }
    
    /**
     * Gets the number of development periods from the source.
     */
    @Override
    protected void initCalculation() {
        length = source.getLength();
    }
    
    /**
     * Sets the length, fires a change event. If `length` is less
     * then 0, it is escaped to 0.
     */
    protected void setLength(int length) {
        setState(CalculationState.INVALID);
        this.length = (length<0)? 0 : length;
        super.recalculateLayer();
        setState(CalculationState.VALID);
    }
    
    /**
     * Returns the {@link Scale Scale} instance used as input.
     */
    public Scale<T> getSourceScales() {
        return source;
    }
    
    /**
     * Retunrs the underlying {@link ScaleInput ScaleInput}
     * instance.
     */
    @Override
    public T getSourceInput() {
        return source.getSourceInput();
    }
}