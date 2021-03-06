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
package org.jreserve.jrlib.util.filter;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;

/**
 * A calculation data filter enables to filter the output 
 * of a {@link CalculationData CalculationData}.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationDataFilter<T extends CalculationData> extends AbstractCalculationData<T> {
    
    protected final static double DEFAULT_TRESHOLD = 1.5;
    
    protected Filter filter; 
    
    /**
     * Creates an instance with the given source, and a
     * {@link SigmaFilter SigmaFilter}, with the treshold
     * set to {@link #DEFAULT_TRESHOLD DEFAULT_TRESHOLD}.
     * 
     * @throws NullPointerException if 'source' is 'null'.
     */
    protected AbstractCalculationDataFilter(T source) {
        this(source, new SigmaFilter(DEFAULT_TRESHOLD));
    }

    /**
     * Creates an instance with the given source and filter.
     * 
     * @throws NullPointerException if 'source' is 'null'.
     * @throws NullPointerException if 'filter' is 'null'.
     */
    protected AbstractCalculationDataFilter(T source, Filter filter) {
        super(source);
        initFilter(filter);
    }
    
    private void initFilter(Filter filter) {
        if(filter == null)
            throw new NullPointerException("Filter can not be null!");
        this.filter = filter;
    }
    
    /**
     * Returns the used filter.
     */
    public Filter getFilter() {
        return filter;
    }
    
    /**
     * Sets the used filter. Calling this method fires a change event.
     * 
     * @throws NullPointerException when `filter` is null.
     */
    public void setFilter(Filter filter) {
        setState(CalculationState.INVALID);
        initFilter(filter);
        recalculateLayer();
        setState(CalculationState.VALID);
    }
}
