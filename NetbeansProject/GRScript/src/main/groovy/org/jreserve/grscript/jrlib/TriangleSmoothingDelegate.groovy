package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.triangle.smoothing.*

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleSmoothingDelegate {
    private final static int ARITHMETIC_MA      = 0;
    private final static int GEOMETRIC_MA       = 1;
    private final static int HARMONIC_MA        = 2;
    private final static int EXPONENTIAL        = 3;
    private final static int DOUBLE_EXPONENTIAL = 4;
    
    private int type
    private Map typeMap
    private def cells = []
    
    void type(Map map) {
        String name = MapUtil.getString(map, "type", "name")?.toLowerCase()
        switch(name) {
            case "arithmeticmovingaverage":
            case "movingaverage":
            case "amovingaverage":
            case "ama":
            case "ma":
                type = ARITHMETIC_MA
                break
            case "geometricmovingaverage":
            case "gmovingaverage":
            case "gma":
                type = GEOMETRIC_MA
                break
            case "harmonicmovingaverage":
            case "hmovingaverage":
            case "hma":
                type = HARMONIC_MA
                break
            case "exponentialsmoothing":
            case "exponential":
            case "es":
            case "e":
                type = EXPONENTIAL
                break
            case "doubleexponentialsmoothing":
            case "doubleexponential":
            case "des":
            case "de":
                type = DOUBLE_EXPONENTIAL
                break
            default:
                throw new IllegalArgumentException("Unknown smoothing type '${name}'!")
        }
        this.typeMap = map
    }
    
    void cell(Map map) {
        int accident = MapUtil.getAccident(map) 
        int development = MapUtil.getDevelopment(map) 
        boolean applied = MapUtil.getBoolean(map, "applied", "apply")
        cell(accident, development, applied)
    }
    
    void cell(int accident, int development, boolean applied) {
        SmoothingCell cell = new SmoothingCell(accident, development, applied)
        if(cells.contains(cell))
            cells.remove(cell)
        cells.add(cell)
    }
    
    TriangleSmoothing getTriangleSmoothing() {
        SmoothingCell[] cellArr = createCellArray()
        switch(type) {
            case ARITHMETIC_MA:
                return createAMA(cellArr)
            case GEOMETRIC_MA:
                return createGMA(cellArr)
            case HARMONIC_MA:
                return createHMA(cellArr)
            case EXPONENTIAL:
                return createExponential(cellArr)
            case DOUBLE_EXPONENTIAL:
                return createDoubleExponential(cellArr)
            default:
                throw new IllegalStateException("Type is not set!")
        }
        return null
    }
    
    private SmoothingCell[] createCellArray() {
        int size = cells.size()
        if(size == 0)
            throw new IllegalStateException("No cells are specified!")
        
        SmoothingCell[] result = new SmoothingCell[size]
        for(int i in 0..<size)
            result[i] = cells[i]
        return result
    }
    
    private TriangleSmoothing createAMA(SmoothingCell[] cellArr) {
        return new ArithmeticMovingAverage(cellArr, getLength());
    }
    
    private int getLength() {
        return MapUtil.getInt(typeMap, "length", "size", "l")
    }
    
    private TriangleSmoothing createGMA(SmoothingCell[] cellArr) {
        return new GeometricMovingAverage(cellArr, getLength());
    }
    
    private TriangleSmoothing createHMA(SmoothingCell[] cellArr) {
        return new HarmonicMovingAverage(cellArr, getLength());
    }
    
    private TriangleSmoothing createExponential(SmoothingCell[] cellArr) {
        double alpha = MapUtil.getDouble(typeMap, "alpha")
        return new ExponentialSmoothing(cellArr, alpha);
    }
    
    private TriangleSmoothing createDoubleExponential(SmoothingCell[] cellArr) {
        double alpha = MapUtil.getDouble(typeMap, "alpha")
        double beta = MapUtil.getDouble(typeMap, "beta")
        return new DoubleExponentialSmoothing(cellArr, alpha, beta);
    }	
}

