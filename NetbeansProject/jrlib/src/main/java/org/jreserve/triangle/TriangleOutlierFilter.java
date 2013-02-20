package org.jreserve.triangle;

import org.jreserve.util.AbstractCalculationDataFilter;
import org.jreserve.util.Filter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleOutlierFilter extends AbstractCalculationDataFilter<Triangle> {

    private boolean[][] isOutlier;

    public TriangleOutlierFilter(Triangle source) {
        super(source);
    }

    public TriangleOutlierFilter(Triangle source, Filter filter) {
        super(source, filter);
        doRecalculate();
    }
    
    public int getAccidentCount() {
        return isOutlier.length;
    }
    
    public int getDevelopmentCount() {
        return getAccidentCount() == 0?
                0:
                isOutlier[0].length;
    }

    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return isOutlier[accident].length;
        return 0;
    }
    
    private boolean withinBounds(int accident) {
        return accident>=0 && accident < isOutlier.length;
    }
    
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