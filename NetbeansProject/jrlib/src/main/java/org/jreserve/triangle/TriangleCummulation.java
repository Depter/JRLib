package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCummulation extends AbstractTriangleModification {

    private double[][] values;

    public TriangleCummulation(Triangle source) {
        super(source);
        values = (source==null)? new double[0][] : source.toArray();
        TriangleUtil.cummulate(values);
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        values = (source==null)? new double[0][] : source.toArray();
        TriangleUtil.cummulate(values);
    }
}
