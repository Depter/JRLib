package org.jreserve.util;

import java.util.Map;
import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MethodSelection<T, M extends SelectableMethod<T>> extends CalculationData {

    /**
     * Returns the method used, when no method is specified for
     * a given development period.
     */
    public M getDefaultMethod();
    
    /**
     * Sets the default method used, when no method is specified for
     * a given development period.
     */
    public void setDefaultMethod(M defaultMethod);
    
    /**
     * Sets the method used for the given development period. 
     * Implementations shoudl allow setting methods the more 
     * development periods then the actual development periods 
     * found in the source.
     * 
     * <p>If method is null, then the default method should be
     * used for the given develoment period.</p>
     * 
     * <p>Calling this method should fire a change event.</p>
     */
    public void setMethod(M method, int index);
    
    /**
     * Sets multiple methods for the given development periods. This
     * method enables to set multiple methods, but fire only on change
     * event.
     */
    public void setMethods(Map<Integer, M> methods);
    
    /**
     * Retunrs the method used for the given position. If
     * <i>index</i> is less then 0, implementations may throw
     * an exception, in other cases this method should never return 
     * <i>null</i>.
     */
    public M getMethod(int index);
}
