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
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.util.method.AbstractMethodSelection;

/**
 * DefaultLinkRatioSelection allows to use different 
 * {@link ScaleEstimator ScaleEstimators} for different development
 * periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleSelection<T extends ScaleInput>extends AbstractMethodSelection<Scale<T>, ScaleEstimator<T>> implements ScaleSelection<T> {
    
    protected int length;
    protected double[] values;
    
    /**
     * Creates an instance for the given source, and with a
     * {@link DefaultScaleEstimator DefaultScaleEstimator} as default method.
     * 
     * @see DefaultScaleCalculator
     * @see #DefaultScaleSelection(Scale, ScaleEstimator) 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultScaleSelection(T source) {
        this(new DefaultScaleCalculator<T>(source));
    }
    
    /**
     * Creates an instance for the given source, and with a
     * {@link DefaultScaleEstimator DefaultScaleEstimator} as default method.
     * 
     * @see #DefaultScaleSelection(Scale, int, ScaleEstimator) 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultScaleSelection(Scale<T> source) {
        this(source, new DefaultScaleEstimator<T>());
    }
    
    /**
     * Creates an instance for the given source, and method.
     * 
     * @see DefaultScaleCalculator
     * @see #DefaultScaleSelection(Scale, ScaleEstimator) 
     * @throws NullPointerException if `source` or `method` is null.
     */
    public DefaultScaleSelection(T source, ScaleEstimator<T> method) {
        this(new DefaultScaleCalculator<T>(source), method);
    }
    
    /**
     * Creates an instance for the given source and method, with the 
     * original length taken from the source.
     * 
     * @see #DefaultScaleSelection(Scale, int, ScaleEstimator) 
     * @throws NullPointerException if `source` or `method` is null.
     */
    public DefaultScaleSelection(Scale<T> source, ScaleEstimator<T> method) {
        this(source, source.getLength(), method);
    }
    
    /**
     * Creates an instance for the given source, method and length.
     * 
     * @see #DefaultScaleSelection(Scale, int, ScaleEstimator) 
     * @throws NullPointerException if `source` or `method` is null.
     */
    public DefaultScaleSelection(Scale<T> source, int length, ScaleEstimator<T> method) {
        super(source, method);
        this.length = length<0? 0 : length;
        doRecalculate();
    }
    
    @Override
    public boolean isAccident() {
        return false;
    }

    @Override
    public T getSourceInput() {
        return source.getSourceInput();
    }
    
    @Override
    public int getLength() {
        return length;
    }
    
    /**
     * Sets the default method to be used. If `method` is null,
     * the the value is reset to 
     * {@link DefaultScaleEstimator DefaultScaleEstimator}.
     * 
     * @param method 
     */
    @Override
    public void setDefaultMethod(ScaleEstimator<T> method) {
        if(method == null)
            method = new DefaultScaleEstimator<T>();
        super.setDefaultMethod(method);
    }
    
    /**
     * Sets the number of development periods. If the 
     * input value is les then 0, then 0 will be used insted.
     * 
     * Calling this method recalculates this instance and 
     * fires a change event.
     */
    protected void setLength(int length) {
        setState(CalculationState.INVALID);
        this.length = (length<0)? 0 : length;
        doRecalculate();
        setState(CalculationState.VALID);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        length = source==null? 0 : source.getLength();
        super.fitMethods();
        values = super.getFittedValues(length);
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0)
            return Double.NaN;
        if(development < length)
            return values[development];
        return 1d;
    }
    
    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
}