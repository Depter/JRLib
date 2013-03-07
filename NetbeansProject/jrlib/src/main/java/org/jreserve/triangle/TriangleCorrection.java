package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrection extends AbstractTriangleModification {

    private final int accident;
    private final int development;
    private final double correction;
    
    public TriangleCorrection(Triangle source, int accident, int development, double correction) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.correction = correction;
    }
    
    @Override
    protected void recalculateLayer() {
    }

    public int getAccident() {
        return accident;
    }

    public double getCorrection() {
        return correction;
    }

    public int getDevelopment() {
        return development;
    }

    @Override
    public double getValue(int accident, int development) {
        if(isMyCell(accident, development))
            return correction;
        return super.getValue(accident, development);
    }
    
    private boolean isMyCell(int accident, int development) {
        return withinBounds(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }
    
    @Override
    public String toString() {
        return String.format(
            "TriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
