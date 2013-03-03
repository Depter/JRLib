package org.jreserve.util;

import org.jreserve.AbstractCalculationData;
import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationDataFilter<T extends CalculationData> extends AbstractCalculationData<T> {
    
    protected final static double DEFAULT_TRESHOLD = 1.5;
    
    protected Filter filter; 

    protected AbstractCalculationDataFilter(T source) {
        this(source, new SigmaFilter(DEFAULT_TRESHOLD));
    }

    protected AbstractCalculationDataFilter(T source, Filter filter) {
        super(source);
        initFilter(filter);
    }
    
    private void initFilter(Filter filter) {
        if(filter == null)
            throw new NullPointerException("Filter can not be null!");
        this.filter = filter;
    }
    
    public Filter getFilter() {
        return filter;
    }
    
    public void setFilter(Filter filter) {
        initFilter(filter);
        recalculateLayer();
        fireChange();
    }
}
