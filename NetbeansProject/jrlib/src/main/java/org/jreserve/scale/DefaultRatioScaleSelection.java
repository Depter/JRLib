package org.jreserve.scale;

import org.jreserve.triangle.TriangleUtil;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultRatioScaleSelection<T extends RatioScaleInput>extends AbstractMethodSelection<RatioScale<T>, RatioScaleEstimator<T>> implements RatioScaleSelection<T> {
    
    private int developments;
    private double[] values;
    
    public DefaultRatioScaleSelection(T input) {
        this(input, null);
    }
    
    public DefaultRatioScaleSelection(T input, RatioScaleEstimator<T> defaultMethod) {
        this(new RatioScaleCalculator<T>(input), defaultMethod);
    }
    
    public DefaultRatioScaleSelection(RatioScale<T> source) {
        this(source, source.getDevelopmentCount(), null);
    }
    
    public DefaultRatioScaleSelection(RatioScale<T> source, RatioScaleEstimator<T> defaultMethod) {
        this(source, source.getDevelopmentCount(), defaultMethod);
    }
    
    public DefaultRatioScaleSelection(RatioScale<T> source, int developmentCount, RatioScaleEstimator<T> defaultMethod) {
        super(source, defaultMethod==null? new EmptyRatioScaleEstimator<T>() : defaultMethod);
        this.developments = developmentCount<0? 0 : developmentCount;
        doRecalculate();
    }
    
    private DefaultRatioScaleSelection(DefaultRatioScaleSelection<T> original) {
        super(original);
        this.developments = original.developments;
        doRecalculate();
    }

    @Override
    public T getSourceInput() {
        return source.getSourceInput();
    }
    
    @Override
    public void setDefaultMethod(RatioScaleEstimator<T> defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new EmptyRatioScaleEstimator<T>();
        super.setDefaultMethod(defaultMethod);
    }
    
    @Override
    public int getDevelopmentCount() {
        return developments;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = (source==null)? 0 : source.getDevelopmentCount();
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0)
            return Double.NaN;
        if(development < developments)
            return values[development];
        return 1d;
    }
    
    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
    
    @Override
    public DefaultRatioScaleSelection<T> copy() {
        return new DefaultRatioScaleSelection(this);
    }
} 
