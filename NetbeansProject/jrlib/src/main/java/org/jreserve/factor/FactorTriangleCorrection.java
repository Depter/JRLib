package org.jreserve.factor;

import org.jreserve.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleCorrection extends AbstractFactorTriangleModification {

    private final int accident;
    private final int development;
    private double value;
    
    public FactorTriangleCorrection(FactorTriangle source, Cell cell, double value) {
        this(source, cell.getAccident(), cell.getDevelopment(), value);
    }
    
    public FactorTriangleCorrection(FactorTriangle source, int accident, int development, double value) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.value = value;
    }

    public int getCorrigatedAccident() {
        return accident;
    }
    
    public int getCorrigatedDevelopment() {
        return development;
    }
    
    public double getCorrigatedValue() {
        return value;
    }
    
    public void setCorrigatedValue(double value) {
        this.value = value;
        fireChange();
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(shouldCorrigateCell(accident, development))
            return value;
        return super.getValue(accident, development);
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
            "FactorTriangleCorrection [%d; %d] = %f",
            accident, development, value);
    }
}
