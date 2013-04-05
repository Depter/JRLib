package org.jreserve.scale;

import org.jreserve.util.AbstractSimpleEstimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleRatioScale<T extends RatioScaleInput> extends AbstractSimpleEstimator<RatioScale<T>, RatioScaleEstimator<T>> implements RatioScale<T> {
    
    private RatioScaleCalculator<T> calcSource;
    
    public SimpleRatioScale(T input) {
        this(input, new MinMaxRatioScaleEstimator<T>());
    }
    
    public SimpleRatioScale(T input, RatioScaleEstimator<T> estimator) {
        this(new RatioScaleCalculator(input), estimator);
    }
    
    public SimpleRatioScale(RatioScaleCalculator<T> calculator) {
        this(calculator, new MinMaxRatioScaleEstimator<T>());
    }
    
    public SimpleRatioScale(RatioScaleCalculator<T> calculator, RatioScaleEstimator<T> estimator) {
        super(calculator, estimator, calculator.getDevelopmentCount());
        this.calcSource = calculator;
    }
    
    @Override
    public T getSourceInput() {
        return source.getSourceInput();
    }
    
    @Override
    public SimpleRatioScale<T> copy() {
        return new SimpleRatioScale<T>(calcSource.copy(), estimator.copy());
    }
    
    @Override
    protected void initCalculation() {
        developments = source.getDevelopmentCount();
        values = source.toArray();
    }
}
