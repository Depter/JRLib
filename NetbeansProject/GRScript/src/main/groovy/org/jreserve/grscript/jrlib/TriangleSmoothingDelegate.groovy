package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.triangle.smoothing.*
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 */
class TriangleSmoothingDelegate {
    
    private SmoothTypes type
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
                type = SmoothTypes.ARITHMETIC_MA
                break
            case "geometricmovingaverage":
            case "gmovingaverage":
            case "gma":
                type = SmoothTypes.GEOMETRIC_MA
                break
            case "harmonicmovingaverage":
            case "hmovingaverage":
            case "hma":
                type = SmoothTypes.HARMONIC_MA
                break
            case "exponentialsmoothing":
            case "exponential":
            case "es":
            case "e":
                type = SmoothTypes.EXPONENTIAL
                break
            case "doubleexponentialsmoothing":
            case "doubleexponential":
            case "des":
            case "de":
                type = SmoothTypes.DOUBLE_EXPONENTIAL
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
            case SmoothTypes.ARITHMETIC_MA:
                return createAMA(cellArr)
            case SmoothTypes.GEOMETRIC_MA:
                return createGMA(cellArr)
            case SmoothTypes.HARMONIC_MA:
                return createHMA(cellArr)
            case SmoothTypes.EXPONENTIAL:
                return createExponential(cellArr)
            case SmoothTypes.DOUBLE_EXPONENTIAL:
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

