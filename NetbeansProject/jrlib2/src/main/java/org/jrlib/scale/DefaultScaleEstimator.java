package org.jrlib.scale;

import org.jrlib.triangle.TriangleUtil;

/**
 * This class simply mirrors the fitted {@link Scale Scale}. For 
 * development periods, where there is no input scale returns 
 * Double.NaN.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleEstimator<T extends ScaleInput> implements ScaleEstimator<T> {
    
    private double[] scales;
    private int developments;
    
    @Override
    public void fit(Scale<T> source) {
        scales = source.toArray();
        developments = source.getLength();
    }

    /**
     * This class simply mirrors the fitted {@link Scale Scale}. For 
     * development periods, where there is no input scale returns 
     * Double.NaN.
     */
    @Override
    public double getValue(int development) {
        if(0 <= development && development < developments)
            return scales[development];
        return Double.NaN;
    }

    /**
     * The method copies the inner state (length and values) of the given 
     * original instance.
     * 
     * @throws NullPointerException if `original` is null.
     */
    protected void copyStateFrom(DefaultScaleEstimator<T> original) {
        developments = original.developments;
        if(developments > 0)
            scales = TriangleUtil.copy(original.scales);
    }
}
