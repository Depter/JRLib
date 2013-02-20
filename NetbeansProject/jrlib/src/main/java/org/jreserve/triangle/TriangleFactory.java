package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleFactory {
    
    public static TriangleFactory create(double[][] data) {
        return new TriangleFactory(data);
    }
    
    public static TriangleFactory create(Triangle source) {
        return new TriangleFactory(source);
    }
    
    private Triangle triangle;
    
    private TriangleFactory(double[][] data) {
        this(new InputTriangle(data));
    }
    
    private TriangleFactory(Triangle source) {
        if(source == null)
            throw new NullPointerException("Source triangle is null!");
        this.triangle = source;
    }
    
    public TriangleFactory cummulate() {
        checkState();
        triangle = new TriangleCummulation(triangle);
        return this;
    }
    
    private void checkState() {
        if(triangle == null)
            throw new IllegalStateException("Triangle is already built!");
    }
    
    public TriangleFactory corrigate(Cell cell, double correction) {
        return corrigate(cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    public TriangleFactory corrigate(int accident, int development, double correction) {
        checkState();
        triangle = new TriangleCorrection(triangle, accident, development, correction);
        return this;
    }
    
    public Triangle build() {
        checkState();
        Triangle result = triangle;
        triangle = null;
        return result;
    }
}
