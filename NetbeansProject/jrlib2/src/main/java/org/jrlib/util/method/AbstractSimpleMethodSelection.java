package org.jrlib.util.method;

import org.jrlib.CalculationData;
import org.jrlib.vector.AbstractVector;
import org.jrlib.vector.Vector;

/**
 * This class allows extending classes to easily interpolate, fill in
 * missing values from an input calculations. The class should provide
 * implementation for {@link #getLength() getLength()} method from the
 * Vector interface and the rest is taken care of.
 * 
 * At recalculation the method fills in an array of the given length,
 * with the values from a default method. If the default method
 * returns `NaN` for a given index, then the estimator method is
 * used to fill in a value (which could still be `NaN`).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSimpleMethodSelection<T extends CalculationData, M extends SelectableMethod<T>> extends AbstractVector<T> implements Vector {

    protected M defaultMethod;
    protected M estimatorMethod;
    protected double[] values;
    
    /**
     * Creates an instance which uses only one method to estimate the values.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    protected AbstractSimpleMethodSelection(T source, M method) {
        this(source, method, method);
    }
    
    /**
     * Creates an instance with the given source, default- and estimation method.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    protected AbstractSimpleMethodSelection(T source, M defaultMethod, M estimatorMethod) {
        super(source);
        
        if(defaultMethod == null)
            throw new NullPointerException("Default method can not be null!");
        this.defaultMethod = defaultMethod;
        if(estimatorMethod == null)
            throw new NullPointerException("Estimator method can not be null!");
        this.estimatorMethod = estimatorMethod;
        
        doRecalculate();
    }
    
    /**
     * Returns the value for the given index;
     */
    @Override
    public double getValue(int index) {
        return withinBonds(index)? values[index] : Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initCalculation();
        fitMethods();
        fillValues();
    }
    
    /**
     * Called before fitting values to the methods. Extending
     * classes can calculate their state needed to perform calls
     * to overriden methods during the recalculation phase.
     */
    protected abstract void initCalculation();
    
    private void fitMethods() {
        defaultMethod.fit(source);
        if(estimatorMethod != defaultMethod)
            estimatorMethod.fit(source);
    }
    
    private void fillValues() {
        int size = getLength();
        if(values == null || values.length != size)
            values = new double[size];
        
        for(int i=0; i<size; i++) {
            double v = defaultMethod.getValue(i);
            values[i] = Double.isNaN(v)? estimatorMethod.getValue(i) : v;
        }
    }
    
    /**
     * Recalculates the value for the given index. If the
     * default method returns a non `NaN` value, than that 
     * is the result, otherwise the value returned from
     * the estimator method.
     */
    protected double recalculateValue(int index) {
        double value = defaultMethod.getValue(index);
        return Double.isNaN(value)?
                estimatorMethod.getValue(index) :
                value;
    }
}
