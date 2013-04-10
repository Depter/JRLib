package org.jrlib.test;

import java.util.Comparator;

/**
 * The class is a {@link Comparator Comparator} for {@link Double Doubles}.
 * It logically compares the doubles, with the extension for null values.
 * Nulls are considered less than any non null values.
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