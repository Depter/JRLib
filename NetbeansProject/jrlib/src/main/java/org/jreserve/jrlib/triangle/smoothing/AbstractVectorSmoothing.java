package org.jreserve.jrlib.triangle.smoothing;

import java.util.Arrays;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * Base class for most of the smoothing methods. Extending classes
 * are able to perform smoothing on vector data.
 * 
 * The class will extract the values represented by the input cells. It is
 * not expected that the class is initialized with a sorted array of cells, 
 * but the {@link #smooth(double[]) smooth()} method will be called with 
 * sorted values (sorted according to the input cells).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVectorSmoothing implements TriangleSmoothing {

    private int cellCount;
    private int[][] cells;
    private boolean[] applied;
    
    /**
     * Creates an instance, which will use the given cells.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     */
    protected AbstractVectorSmoothing(SmoothingCell[] cells) {
        if(cells == null)
            throw new NullPointerException("Cells were null!");
        this.cellCount = cells.length;
        initCells(cells);
    }
    
    protected AbstractVectorSmoothing() {
    }
    
    private void initCells(SmoothingCell[] cells) {
        Arrays.sort(cells);
        
        this.cells = new int[cellCount][];
        applied = new boolean[cellCount];
        for(int i=0; i<cellCount; i++) {
            this.cells[i] = cells[i].toArray();
            applied[i] = cells[i].isApplied();
        }
    }
    
    @Override
    public double[][] smooth(Triangle input) {
        double[] smoothInput = getSmoothedVector(input);
        double[][] result = input.toArray();
        smooth(result, smoothInput);
        return result;
    }
    
    private double[] getSmoothedVector(Triangle input) {
        double[] values = getValues(input);
        smooth(values);
        return values;
    }
    
    private double[] getValues(Triangle input) {
        double[] result = new double[cellCount];
        for(int i=0; i<cellCount; i++)
            result[i] = input.getValue(cells[i][0], cells[i][1]);
        return result;
    }
    
    /**
     * Extending classes should smooth this array. The values will 
     * contain the values from the input triangle, sorted based on 
     * their coordinates. The implementing class should smooth the 
     * whole array, this class will sort out, which value should
     * be used in the output of the calculation.
     * 
     * @see org.jreserve.jrlib.triangle.Cell#compareTo(org.jreserve.jrlib.triangle.Cell) 
     */
    protected abstract void smooth(double[] input);
    
    private void smooth(double[][] values, double[] smoothInput) {
        for(int i=0; i<cellCount; i++) {
            if(isApplied(i, values)) {
                int accident = cells[i][0];
                int development = cells[i][1];
                values[accident][development] = smoothInput[i];
            }
        }
    }
    
    private boolean isApplied(int i, double[][] values) {
        if(!applied[i]) return false;
        int accident = cells[i][0];
        int development = cells[i][1];
        return accident >=0 && accident < values.length &&
               development >= 0 && development < values[accident].length;
    }
    
    /**
     * Copies the state form the input.
     * 
     * @throws NullPointerException if `other` is null.
     */
    protected void copyStateFrom(AbstractVectorSmoothing other) {
        this.cellCount = other.cellCount;
        this.cells = new int[cellCount][2];
        this.applied = new boolean[cellCount];
        for(int i=0; i<cellCount; i++) {
            applied[i] = other.applied[i];
            cells[i][0] = other.cells[i][0];
            cells[i][1] = other.cells[i][1];
        }
    }
}
