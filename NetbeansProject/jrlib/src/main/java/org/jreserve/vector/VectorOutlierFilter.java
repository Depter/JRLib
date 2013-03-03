package org.jreserve.vector;

import org.jreserve.util.AbstractCalculationDataFilter;
import org.jreserve.util.Filter;
import org.jreserve.util.SigmaFilter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorOutlierFilter extends AbstractCalculationDataFilter<Vector> {
    
    private boolean[] isOutlier;
    
    public VectorOutlierFilter(Vector source) {
        this(source, new SigmaFilter(DEFAULT_TRESHOLD));
    }

    public VectorOutlierFilter(Vector source, Filter filter) {
        super(source, filter);
        doRecalculate();
    }
    
    public int getLength() {
        return isOutlier.length;
    }
    
    private boolean withinBound(int index) {
        return index>=0 && index < isOutlier.length;
    }
    
    public boolean[] toArray() {
        int length = isOutlier.length;
        boolean[] copy = new boolean[length];
        System.arraycopy(isOutlier, 0, copy, 0, length);
        return copy;
    }
    
    public boolean isOutlier(int index) {
        return withinBound(index) &&
               isOutlier[index];
    } 
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int length = (source==null)? 0 : source.getLength();
        if(length == 0)
            isOutlier = new boolean[length];
        else
            isOutlier = filter.filter(source.toArray());
    }
}