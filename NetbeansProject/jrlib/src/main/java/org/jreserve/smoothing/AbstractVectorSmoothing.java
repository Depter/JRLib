package org.jreserve.smoothing;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVectorSmoothing implements TriangleSmoothing {

    private int cellCount;
    private int[][] cells;
    private boolean[] applied;
    
    public AbstractVectorSmoothing(SmoothingCell[] cells) {
        if(cells == null)
            throw new NullPointerException("Cells were null!");
        this.cellCount = cells.length;
        initCells(cells);
    }
    
    private void initCells(SmoothingCell[] cells) {
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
}
