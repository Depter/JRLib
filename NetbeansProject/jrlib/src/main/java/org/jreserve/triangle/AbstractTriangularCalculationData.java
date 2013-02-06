package org.jreserve.triangle;

import org.jreserve.AbstractCalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangularCalculationData extends AbstractCalculationData<TriangularCalculationData> implements TriangularCalculationData{

    protected AbstractTriangularCalculationData() {
    }

    protected AbstractTriangularCalculationData(TriangularCalculationData source) {
        super(source);
    }

    @Override
    public int getAccidentCount() {
        if(source == null)
            return 0;
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        if(source == null)
            return 0;
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(source == null)
            return 0;
        return source.getDevelopmentCount(accident);
    }
    
    @Override
    public double getValue(int accident, int development) {
        return source == null?
                Double.NaN :
                source.getValue(accident, development);
    }

    @Override
    public double[][] toArray() {
        int accidents = getAccidentCount();
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }

    private double[] toArray(int accident) {
        int devs = getDevelopmentCount(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getValue(accident, d);
        return result;
    }
    
    protected boolean withinSourceBound(int accident, int development) {
        return source != null &&
               accident >=0 && accident < getAccidentCount() &&
               development >=0 && development < getDevelopmentCount(accident);
    }
}
