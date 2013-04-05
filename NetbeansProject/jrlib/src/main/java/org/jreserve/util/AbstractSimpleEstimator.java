package org.jreserve.util;

import java.util.Arrays;
import org.jreserve.AbstractCalculationData;
import org.jreserve.CalculationData;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSimpleEstimator<T extends CalculationData, E extends SelectableMethod<T>> extends AbstractCalculationData<T> {

    protected int developments;
    protected double[] values;
    protected E estimator;
    
    protected AbstractSimpleEstimator(T source, E estimator, int developments) {
        super(source);
        this.estimator = estimator;
        this.developments = developments<0? 0 : developments;
        doRecalculate();
    }
    
    public int getDevelopmentCount() {
        return developments;
    }
    
    protected void setDevelopmentCount(int developments) {
        if(developments < 0) developments = 0;
        if(this.developments != developments) {
            this.developments = developments;
            recalculateLayer();
            fireChange();
        }
    }
    
    public double getValue(int development) {
        if(development >= 0 && development < developments)
            return values[development];
        return Double.NaN;
    }
    
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initCalculation();
        checkValues();
        estimator.fit(source);
        fillNaNs();
    }
    
    protected abstract void initCalculation();
    
    private void checkValues() {
        if(values == null) {
            values = new double[developments];
            Arrays.fill(values, Double.NaN);
        }
    }
    
    private void fillNaNs() {
        for(int d=0; d<developments; d++)
            if(Double.isNaN(values[d]))
                values[d] = estimator.getValue(d);
    }
}
