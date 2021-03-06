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
package org.jreserve.jrlib.util.method;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;

/**
 * The AbstractMethodSelection is a basic implementation for the
 * {@link MethodSelection MethodSelection} interface. It provides
 * implementations and utilities for the methods decalred in the 
 * interface.
 * 
 * A MethodSelection is mostly used in situations where the results 
 * of an underlying calculation should be extrapolated or interpolated
 * to ill in NaN values.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMethodSelection<T extends CalculationData, M extends SelectableMethod<T>> extends AbstractCalculationData<T> implements MethodSelection<T, M> {

    private SelectableMethod[] methods = new SelectableMethod[0];
    private M defaultMethod;

    /**
     * Creates an instance with the given source and default method.
     * The default method is used, when the user does not specified a
     * method for the given position.
     * 
     * @throws NullPointerException when `source` or `defaultMethod` is null.
     */
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

    /**
     * Sets the default method used, when there is no method specified
     * for a given location.
     * 
     * @throws NullPointerException when `defaultMethod` is null.
     * @see #setMethod(SelectableMethod, int) 
     */
    @Override
    public void setDefaultMethod(M defaultMethod) {
        initDefaultMethod(defaultMethod);
    }

    /**
     * Sets the used method for the given index. If `method` is
     * null, that means that the default method will be used.
     * 
     * Calling this method fires a change event.
     */
    @Override
    public void setMethod(M method, int index) {
        if(index >= 0) {
            saveMethodAt(method, index);
            methodsChanged();
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
    
    private void methodsChanged() {
        setState(CalculationState.INVALID);
        recalculateLayer();
        setState(CalculationState.VALID);
    }

    /**
     * Sets the methods used for the given indices.
     * 
     * @see #setMethod(SelectableMethod, int) 
     */
    @Override
    public void setMethod(M method, int... indices) {
        if(indices.length == 0) return;
        for(int index : indices)
            saveMethodAt(method, index);
        methodsChanged();
    }

    /**
     * Sets the methods used for the given indices.
     * 
     * @throws NullPointerException if one of the indices is null.
     * @see #setMethod(SelectableMethod, int) 
     */
    @Override
    public void setMethods(Map<Integer, M> methods) {
        for(Integer index : methods.keySet())
            if(index >= 0)
                saveMethodAt(methods.get(index), index);
        methodsChanged();
    }

    /**
     * Returns the method used at the given index. If no specific method
     * is set, then the default method is returned.
     */
    @Override
    public M getMethod(int index) {
        if(index < 0)
            throw new IllegalArgumentException("Index must be at least 0, but was " + index + "!");
        Object method = (index < methods.length) ? methods[index] : null;
        return method == null ? defaultMethod : (M) method;
    }
    
    /**
     * Fits all methods to the input data. The method 
     * {@link SelectableMethod#fit(java.lang.Object) fit()} will be
     * called only once for all methods, even if the same method is
     * used for multiple locations.
     */
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
    
    /**
     * Retunrs the fitted values for the given length. Callin this method
     * will result in a call to 
     * {@link SelectableMethod#getValue(int) SelectableMethod.getValue()}
     * for the methods at indices [0,..., length-1].
     * 
     * @throws IllegalArgumentException if `length` is less then 0.
     */
    protected double[] getFittedValues(int length) {
        double[] result = new double[length];
        for(int i=0; i<length; i++)
            result[i] = getMethod(i).getValue(i);
        return result;
    }
    
    /**
     * Retunrs the size, where a specific method is set. For the returned 
     * value s is the largest value for wich 
     * `getMethod(s-1) != getDefaultMethod()`.
     */
    protected int getSize() {
        int index = methods.length - 1;
        while(index >= 0 && methods[index] == null)
            index--;
        return index+1;
    }
}
