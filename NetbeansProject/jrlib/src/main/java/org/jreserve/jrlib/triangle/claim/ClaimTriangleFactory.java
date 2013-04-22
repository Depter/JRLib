package org.jreserve.jrlib.triangle.claim;

import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 * Factor class for creating {@link ClaimTriangle ClaimTriangles}.
 * 
 * The order in which the methods of the factory are called is important.
 * For example first cummulating then applying a correction will result
 * in a different triangle then first applying the correction then
 * cummulating.
 * 
 * Only one instance can be built with factory instance. Using the
 * factory after the triangle was built will result in an 
 * IllegalStateException.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleFactory {
    
    /**
     * Creates a factory with the given values as input.
     * 
     * @see ClaimTriangleFactory#ClaimTriangleFactory(double[][])
     * @see InputClaimTriangle#InputClaimTriangle(double[][]) 
     */
    public static ClaimTriangleFactory create(double[][] data) {
        return new ClaimTriangleFactory(data);
    }
    
    /**
     * Creates a factory with the given values as input.
     * 
     * @see ClaimTriangleFactory#ClaimTriangleFactory(ClaimTriangle) 
     */
    public static ClaimTriangleFactory create(ClaimTriangle source) {
        return new ClaimTriangleFactory(source);
    }
    
    private ClaimTriangle triangle;
    
    private ClaimTriangleFactory(double[][] data) {
        this(new InputClaimTriangle(data));
    }
    
    private ClaimTriangleFactory(ClaimTriangle source) {
        if(source == null)
            throw new NullPointerException("Source triangle is null!");
        this.triangle = source;
    }
    
    /**
     * The triangle is cummulated at this step.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangleFactory cummulate() {
        checkState();
        triangle = new CummulatedClaimTriangle(triangle);
        return this;
    }
    
    private void checkState() {
        if(triangle == null)
            throw new IllegalStateException("Triangle is already built!");
    }
    
    /**
     * The triangle is corrigated with the given value 
     * at this step.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangleFactory corrigate(Cell cell, double correction) {
        return corrigate(cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    /**
     * The triangle is corrigated with the given value 
     * at this step.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangleFactory corrigate(int accident, int development, double correction) {
        checkState();
        triangle = new ClaimTriangleCorrection(triangle, accident, development, correction);
        return this;
    }
    
    /**
     * The value at the given accident and development period
     * will be NaN from this step.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangleFactory exclude(int accident, int development) {
        return corrigate(accident, development, Double.NaN);
    }
    
    /**
     * Applies the given smoothing at this step.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangleFactory smooth(TriangleSmoothing smoothing) {
        checkState();
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        triangle = new SmoothedClaimTriangle(triangle, smoothing);
        return this;
    }
    
    /**
     * Builds the triangle.
     * 
     * @throws IllegalStateException if {@link #build() build()} was 
     * already called.
     */
    public ClaimTriangle build() {
        checkState();
        ClaimTriangle result = triangle;
        triangle = null;
        return result;
    }

}
