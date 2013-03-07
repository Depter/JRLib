package org.jreserve.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jreserve.AbstractCalculationData;
import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMethodSelection<T extends CalculationData, M extends SelectableMethod<T>> extends AbstractCalculationData<T> implements MethodSelection<T, M> {

    private SelectableMethod[] methods = new SelectableMethod[0];
    private M defaultMethod;

    protected AbstractMethodSelection(T source, M defaultMethod) {
        super(source);
        initDefaultMethod(defaultMethod);
    }

    private void initDefaultMethod(M defaultMethod) {
        if(defaultMethod == null)
            throw new NullPointerException("Default method can not be null!");
        this.defaultMethod = defaultMethod;
    }

    @Override
    public M getDefaultMethod() {
        return defaultMethod;
    }

    @Override
    public void setDefaultMethod(M defaultMethod) {
        initDefaultMethod(defaultMethod);
    }

    @Override
    public void setMethod(M method, int index) {
        if(index >= 0) {
            saveMethodAt(method, index);
            recalculateLayer();
            fireChange();
        }
    }

    private void saveMethodAt(M method, int index) {
        if(method == null) {
            if(index < methods.length)
                methods[index] = null;
        } else {
            setMethodsSize(index);
            methods[index] = method;
        }
    }

    private void setMethodsSize(int index) {
        if(index >= methods.length) {
            SelectableMethod[] redim = new SelectableMethod[index + 1];
            System.arraycopy(methods, 0, redim, 0, methods.length);
            methods = redim;
        }
    }

    @Override
    public void setMethods(Map<Integer, M> methods) {
        for(Integer index : methods.keySet())
            if(index >= 0)
                saveMethodAt(methods.get(index), index);
        recalculateLayer();
        fireChange();
    }

    @Override
    public M getMethod(int index) {
        if(index < 0)
            throw new IllegalArgumentException("Index must be at least 0, but was " + index + "!");
        Object method = (index < methods.length) ? methods[index] : null;
        return method == null ? defaultMethod : (M) method;
    }
    
    protected void fitMethods() {
        Set<SelectableMethod> recalculated = new HashSet<SelectableMethod>();
        for(SelectableMethod m : methods)
            if(m != null && !recalculated.contains(m))
                fitMethod(recalculated, m);
        fitMethod(recalculated, defaultMethod);
    }
    
    private void fitMethod(Set<SelectableMethod> recalculated, SelectableMethod m) {
        if(m != null && !recalculated.contains(m)) {
            recalculated.add(m);
            m.fit(source);
        }
    }
    
    protected double[] getFittedValues(int length) {
        double[] result = new double[length];
        for(int i=0; i<length; i++)
            result[i] = getMethod(i).getValue(i);
        return result;
    }
    
    protected int getSize() {
        int index = methods.length - 1;
        while(index >= 0 && methods[index] == null)
            index--;
        return index+1;
    }
    
    
}
