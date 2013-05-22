package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.vector.Vector as JRVector
import org.jreserve.jrlib.vector.InputVector
import org.jreserve.jrlib.vector.VectorCorrection
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.vector.smoothing.SmoothingIndex
import org.jreserve.jrlib.vector.smoothing.VectorSmoothing
import org.jreserve.jrlib.vector.smoothing.VectorSmoothingMethod
import org.jreserve.jrlib.vector.smoothing.AbstractSmoothing
import org.jreserve.jrlib.vector.smoothing.ArithmeticMovingAverageMethod
import org.jreserve.jrlib.vector.smoothing.GeometricMovingAverageMethod
import org.jreserve.jrlib.vector.smoothing.HarmonicMovingAverageMethod
import org.jreserve.jrlib.vector.smoothing.ExponentialSmoothingMethod
import org.jreserve.jrlib.vector.smoothing.DoubleExponentialSmoothingMethod
import org.jreserve.jrlib.vector.SmoothedVector
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class VectorDelegate extends AbstractDelegate {
    
    private final static int ARITHMETIC_MA      = 0;
    private final static int GEOMETRIC_MA       = 1;
    private final static int HARMONIC_MA        = 2;
    private final static int EXPONENTIAL        = 3;
    private final static int DOUBLE_EXPONENTIAL = 4;
	
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.vector    << this.&vector
        emc.corrigate << this.&corrigate
        emc.exclude   << this.&exclude
        emc.smooth    << this.&smooth
    }
    
    JRVector vector(double[] data) {
        return new InputVector(data)
    }
    
    JRVector vector(int length, double value) {
        double[] data = new double[length]
        Arrays.fill(data, value)
        return this.vector(data)
    }
    
    JRVector vector(Collection values) {
        int size = values.size()
        double[] data = new double[size]
        values.eachWithIndex {value, index -> data[index] = value}
        return this.vector(data)
    }
    
    JRVector corrigate(JRVector vector, int index, double value) {
        return new VectorCorrection(vector, index, value)
    }
    
    JRVector corrigate(JRVector vector, Map map) {
        map.each {index, value -> vector = corrigate(vector, index, value)}
        return vector
    }
    
    JRVector exclude(JRVector vector, int... indices) {
        indices.each {vector = corrigate(vector, it, Double.NaN)}
        return vector
    }
    
    JRVector exclude(JRVector vector, Collection indices) {
        indices.each {vector = corrigate(vector, it, Double.NaN)}
        return vector
    }
    
    JRVector smooth(JRVector vector, Closure cl) {
        SmoothingBuilder builder = new SmoothingBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return new SmoothedVector(vector, builder.getVectorSmoothing())
    }
    
    JRVector vector(double[] data, Closure cl) {
        Builder builder = new Builder(data, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.vector
    }
    
    private class Builder {
        JRVector vector
        private VectorDelegate delegate
        
        Builder(double[] data, VectorDelegate delegate) {
            this.vector = delegate.vector(data)
            this.delegate = delegate
        }
        
        void corrigate(int index, double value) {
            vector = delegate.corrigate(vector, index, value)
        }
    
        void corrigate(Map map) {
            vector = delegate.corrigate(vector, map)
        }
    
        void exclude(int... indices) {
            vector = delegate.exclude(vector, indices)
        }
    
        void exclude(Collection indices) {
            vector = delegate.exclude(vector, indices)
        }
    
        void smooth(Closure cl) {
            vector = delegate.smooth(vector, cl)
        }
    }
    
    private class SmoothingBuilder {

        private int type
        private Map typeMap
        private def cells = []
        private MapUtil mapUtil = MapUtil.getInstance()
        
        void type(Map map) {
            String name = mapUtil.getString(map, "type", "name")?.toLowerCase()
            switch(name) {
            case "arithmeticmovingaverage":
            case "arithmetic moving average":
            case "arithmetic-moving-average":
            case "movingaverage":
            case "moving-average":
            case "moving average":
            case "amovingaverage":
            case "ama":
            case "ma":
                type = ARITHMETIC_MA
                break
            case "geometricmovingaverage":
            case "geometric moving average":
            case "geometric-moving-average":
            case "gmovingaverage":
            case "gma":
                type = GEOMETRIC_MA
                break
            case "harmonicmovingaverage":
            case "harmonic moving average":
            case "harmonic-moving-average":
            case "hmovingaverage":
            case "hma":
                type = HARMONIC_MA
                break
            case "exponentialsmoothing":
            case "exponential smoothing":
            case "exponential-smoothing":
            case "exponential":
            case "es":
            case "e":
                type = EXPONENTIAL
                break
            case "doubleexponentialsmoothing":
            case "doubleexponential":
            case "double exponential smoothing":
            case "double exponential":
            case "double-exponential-smoothing":
            case "double-exponential":
            case "des":
            case "de":
                type = DOUBLE_EXPONENTIAL
                break
            default:
                throw new IllegalArgumentException("Unknown smoothing type '${name}'!")
            }
            this.typeMap = map
        }
        
        void cells(Map map) {
            map.each {index, applied -> cell(index, applied)}
        }
        
        void cell(int index, boolean applied) {
            cells << new SmoothingIndex(index, applied)
        }
    
        VectorSmoothing getVectorSmoothing() {
            SmoothingIndex[] cellArr = createCellArray()
            VectorSmoothingMethod method = createMethod()
            return new AbstractSmoothing(cellArr, method)
        }
    
        private SmoothingIndex[] createCellArray() {
            int size = cells.size()
            if(size == 0)
            throw new IllegalStateException("No cells are specified!")
        
            SmoothingIndex[] result = new SmoothingIndex[size]
            for(int i in 0..<size)
            result[i] = cells[i]
            return result
        }
        
        private VectorSmoothingMethod createMethod() {
            switch(type) {
            case ARITHMETIC_MA:
                return createAMA()
            case GEOMETRIC_MA:
                return createGMA()
            case HARMONIC_MA:
                return createHMA()
            case EXPONENTIAL:
                return createExponential()
            case DOUBLE_EXPONENTIAL:
                return createDoubleExponential()
            default:
                throw new IllegalStateException("Type is not set!")
            }
        }
    
        private VectorSmoothingMethod createAMA() {
            return new ArithmeticMovingAverageMethod(getLength());
        }
    
        private int getLength() {
            return mapUtil.getInt(typeMap, "length", "size", "l")
        }
    
        private VectorSmoothingMethod createGMA() {
            return new GeometricMovingAverageMethod(getLength());
        }
    
        private VectorSmoothingMethod createHMA() {
            return new HarmonicMovingAverageMethod(getLength());
        }
    
        private VectorSmoothingMethod createExponential() {
            double alpha = mapUtil.getDouble(typeMap, "alpha")
            return new ExponentialSmoothingMethod(alpha);
        }
    
        private VectorSmoothingMethod createDoubleExponential() {
            double alpha = mapUtil.getDouble(typeMap, "alpha")
            double beta = mapUtil.getDouble(typeMap, "beta")
            return new DoubleExponentialSmoothingMethod(alpha, beta);
        }	
    }
}

