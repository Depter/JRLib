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
public abstract class AbstractMethodSelection<C extends CalculationData, M> extends AbstractCalculationData<C> implements MethodSelection<C, M> {

    private Object[] methods = new Object[0];
    private M defaultMethod;

    protected AbstractMethodSelection(C source, M defaultMethod) {
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
            Object[] redim = new Object[index + 1];
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
    
    protected Set<M> getMethods() {
        Set<M> result = new HashSet<M>();
        for(Object o : methods) {
            if(o != null && !result.contains(o))
                result.add((M) o);
        }
        if(!result.contains(defaultMethod))
            result.add(defaultMethod);
        return result;
    }
    
    protected int getSize() {
        int index = methods.length - 1;
        while(index >= 0 && methods[index] == null)
            index--;
        return index+1;
    }
}
