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

import java.util.Map;

/**
 * Common interface for class that would like to allow the user
 * to plug in different calculation methods for different positions.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface MethodSelection<T, M extends SelectableMethod<T>> {

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
     * If method is null, then the default method should be
     * used for the given develoment period.
     * 
     * Calling this method should fire a change event if the implementing
     * class also implements {@link org.jreserve.jrlib.CalculationData CalculationData}.
     */
    public void setMethod(M method, int index);
    
    /**
     * Sets the method used for the given development periods. 
     * Implementations shoudl allow setting methods the more 
     * development periods then the actual development periods 
     * found in the source.
     * 
     * If method is null, then the default method should be
     * used for the given develoment period.
     * 
     * Calling this method should fire a change event if the implementing
     * class also implements {@link org.jreserve.jrlib.CalculationData CalculationData}.
     */
    public void setMethod(M method, int... indices);
    
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
