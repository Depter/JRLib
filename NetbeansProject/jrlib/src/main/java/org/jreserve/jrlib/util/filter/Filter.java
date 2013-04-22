package org.jreserve.jrlib.util.filter;

/**
 * Implementations of the <b>Filter</b> interface are able to filter an input 
 * array on some predefined criteria.
 * 
 * Filtering can be used to mark outlieres in a triangle or in a vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Filter {
    
    /**
     * Returns a boolean array <b>b</b> whith the same length as <i>x</i>, 
     * where b[i] is true if x[i] is filtered.
     * 
     * @param x the input to filter. May contain NaNs, not null.
     * @return the filter array.
     * @throws NullPointerException When x is <i>null</i>.
     */
    public boolean[] filter(double[] x);

}
