package org.jreserve.triangle;

import org.jreserve.AbstractDoubleInputCalculationData;

//TODO add javadoc
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CompositeTriangle extends AbstractDoubleInputCalculationData<Triangle, Triangle> implements Triangle {

    private TriangleOperation operation;
    private int accidents;
    private int developments;
    private double[][] values;
    
    public CompositeTriangle(Triangle primary, Triangle secondary, TriangleOperation operation) {
        super(primary, secondary);
        initOperation(operation);
        doRecalculate();
    }
    
    private void initOperation(TriangleOperation operation) {
        if(operation == null)
            throw new NullPointerException("Operation is null!");
        this.operation = operation;
    }

    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(withinBound(accident))
            return values[accident].length;
        return 0;
    }
    
    private boolean withinBound(int accident) {
        return accident >=0 && accident < accidents;
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return withinBound(accident) &&
               development>=0 && development < values[accident].length;
    }

    @Override
    public double[][] toArray() {
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        int devs = values[accident].length;
        double[] result = new double[devs];
        System.arraycopy(values[accident], 0, result, 0, devs);
        return result;
    }

    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initAccidents();
        initDevelopments();
        recalculateValues();
    }
    
    private void initAccidents() {
        int a1 = primary.getAccidentCount();
        int a2 = secondary.getAccidentCount();
        this.accidents = (a1<a2)? a1 : a2;
    }
    
    private void initDevelopments() {
        int d1 = primary.getDevelopmentCount();
        int d2 = secondary.getDevelopmentCount();
        this.developments = (d1<d2)? d1 : d2;
    }
    
    private void recalculateValues() {
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = recalculateAccident(a);
    }
    
    private double[] recalculateAccident(int accident) {
        int devs = getDevelopments(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = recalculateCell(accident, d);
        return result;
    }
    
    private int getDevelopments(int accident) {
        int d1 = primary.getDevelopmentCount(accident);
        int d2 = secondary.getDevelopmentCount(accident);
        return (d1<d2)? d1 : d2;
    }
    
    private double recalculateCell(int accident, int development) {
        double v1 = primary.getValue(accident, development);
        double v2 = secondary.getValue(accident, development);
        return operation.operate(v1, v2);
    }
}
