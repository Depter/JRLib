package org.jreserve.triangle.claim;

import org.jreserve.smoothing.TriangleSmoothing;
import org.jreserve.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleFactory {
    
    public static ClaimTriangleFactory create(double[][] data) {
        return new ClaimTriangleFactory(data);
    }
    
    public static ClaimTriangleFactory create(ClaimTriangle source) {
        return new ClaimTriangleFactory(source);
    }
    
    private ClaimTriangle triangle;
    
    private ClaimTriangleFactory(double[][] data) {
        this(new InputTriangle(data));
    }
    
    private ClaimTriangleFactory(ClaimTriangle source) {
        if(source == null)
            throw new NullPointerException("Source triangle is null!");
        this.triangle = source;
    }
    
    public ClaimTriangleFactory cummulate() {
        checkState();
        triangle = new CummulatedClaimTriangle(triangle);
        return this;
    }
    
    private void checkState() {
        if(triangle == null)
            throw new IllegalStateException("Triangle is already built!");
    }
    
    public ClaimTriangleFactory corrigate(Cell cell, double correction) {
        return corrigate(cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    public ClaimTriangleFactory corrigate(int accident, int development, double correction) {
        checkState();
        triangle = new ClaimTriangleCorrection(triangle, accident, development, correction);
        return this;
    }
    
    public ClaimTriangleFactory exclude(int accident, int development) {
        return corrigate(accident, development, Double.NaN);
    }
    
    public ClaimTriangleFactory smooth(TriangleSmoothing smoothing) {
        checkState();
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        triangle = new SmoothedClaimTriangle(triangle, smoothing);
        return this;
    }
    
    public ClaimTriangle build() {
        checkState();
        ClaimTriangle result = triangle;
        triangle = null;
        return result;
    }
}
