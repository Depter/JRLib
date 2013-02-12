package org.jreserve.test;

import java.util.Comparator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DoubleComparator implements Comparator<Double> {
    
    final static Comparator<Double> INSTANCE = new DoubleComparator();
    
    private DoubleComparator() {
    }
    
    @Override
    public int compare(Double o1, Double o2) {
        if(o1 == null)
            return o2 == null ? 0 : 1;
        return o2 == null ? -1 : comparePrimitive(o1, o2);
    }

    private int comparePrimitive(double d1, double d2) {
        if(Double.isNaN(d1))
            return Double.isNaN(d2) ? 0 : 1;
        if(Double.isNaN(d2))
            return -1;
        if(d1 == d2)
            return 0;
        return d1 < d2 ? -1 : 1;
    }
    
}
