package org.jrlib.triangle;

import org.jrlib.util.AbstractCalculationDataFilter;
import org.jrlib.util.Filter;

/**
 * Outlier filter for triangular data. The output of
 * this calculation is a triangular boolean array, with
 * the same dimensions as the surce triangle. the output
 * array contains true where the elements of the input
 * are outliers.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleOutlierFilter extends AbstractCalculationDataFilter<Triangle> {

    private boolean[][] isOutlier;
    
    /**
     * Creates an instance for he given triangle with a default
     * filter defined by the super class.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public TriangleOutlierFilter(Triangle source) {
        super(source);
    }

    /**
     * Creates an instance for the given triangle wich uses the given filter.
     * 
     * @throws NullPointerException if `source` or `filter` is null.
     */
    public TriangleOutlierFilter(Triangle source, Filter filter) {
        super(source, filter);
        doRecalculate();
    }
    
    /**
     * Returns the number of accident periods.
     * 
     * @see Triangle#getAccidentCount() 
     */
    public int getAccidentCount() {
        return isOutlier.length;
    }
    
    /**
     * Returns the number of development periods.
     * 
     * @see Triangle#getDevelopmentCount() 
     */
    public int getDevelopmentCount() {
        return getAccidentCount() == 0?
                0:
                isOutlier[0].length;
    }

    /**
     * Returns the number of development periods in the
     * given accident period.
     * 
     * @see Triangle#getDevelopmentCount(int) 
     */
    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return isOutlier[accident].length;
        return 0;
    }
    
    private boolean withinBounds(int accident) {
        return accident>=0 && accident < isOutlier.length;
    }
    
    /**
     * Returns an array representing this triangle. Modifying
     * the array does not affect the inner state of this instance.
     * 
     * @see Triangle#toArray()
     */
    public boolean[][] toArray() {
        int accidents = isOutlier.length;
        boolean[][] copy = new boolean[accidents][];
        for(int a=0; a<accidents; a++)
            copy[a] = toArray(a);
        return copy;
    }
    
    private boolean[] toArray(int accident) {
        boolean[] data = isOutlier[accident];
        boolean[] copy = new boolean[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);
        return copy;
    }
    
    /**
     * Returns wether the value in the given cell is an outlier. 
     * Values outside the bound of the source triangle are never 
     * outliers.
     * 
     * @throws NullPointerException if `cell` is null.
     */
    public boolean isOutlier(Cell cell) {
        return isOutlier(cell.getAccident(), cell.getDevelopment());
    }
    
    /**
     * Returns wether the value at the given accident and 
     * development period is an outlier. Values outside
     * the bound of the source triangle are never outliers.
     */
    public boolean isOutlier(int accident, int development) {
        return withinBounds(accident, development) &&
               isOutlier[accident][development];
    } 
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               development>=0 &&
               development < isOutlier[accident].length;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        createOutliers();
        int developments = source==null? 0 : source.getDevelopmentCount();
        for(int d=0; d<developments; d++)
            fillOutliers(d);
    }
    
    private void createOutliers() {
        int accidents = source==null? 0 : source.getAccidentCount();
        isOutlier = new boolean[accidents][];
        for(int a=0; a<accidents; a++)
            isOutlier[a] = new boolean[source.getDevelopmentCount(a)];
    }
    
    private void fillOutliers(int development) {
        double[] values = TriangleUtil.getAccidentValues(source, development);
        boolean[] outliers = filter.filter(values);
        fillOutliers(development, outliers);
    }
    
    private void fillOutliers(int development, boolean[] outliers) {
        int size = outliers.length;
        for(int a=0; a<size; a++)
            if(outliers[a])
                isOutlier[a][development] = true;
    }
}
