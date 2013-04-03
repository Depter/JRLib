package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrection<T extends Triangle> extends AbstractTriangleModification<T> {

    protected final int accident;
    protected final int development;
    protected double correction;
    
    public TriangleCorrection(T source, Cell cell, double correction) {
        this(source, cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    public TriangleCorrection(T source, int accident, int development, double correction) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.correction = correction;
    }

    public int getCorrigatedAccident() {
        return accident;
    }
    
    public int getCorrigatedDevelopment() {
        return development;
    }
    
    public double getCorrigatedValue() {
        return correction;
    }
    
    public void setCorrigatedValue(double correction) {
        this.correction = correction;
        fireChange();
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(shouldCorrigateCell(accident, development))
            return correction;
        return source.getValue(accident, development);
    }
    
    private boolean shouldCorrigateCell(int accident, int development) {
        return withinBounds(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public String toString() {
        return String.format(
            "TriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
