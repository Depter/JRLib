package org.jrlib.scale;

import org.jrlib.AbstractCalculationData;
import org.jrlib.Copyable;
import org.jrlib.triangle.TriangleUtil;

/**
 * This class calculates variance scale parameters for evry development
 * period from the given input.
 * 
 * The formula for calculating the scale parameter `s(d)` for 
 * development period `d` is:
 *             sum(w(a,d) * (r(a,d) - r(d))^2
 *      s(d) = ------------------------------
 *                          n - 1
 * where:
 * -   `w(a,d)` are the weights returned by {@link ScaleInput#getWeight(int, int) getWeight}
 *     for accident period `a` and development period `d`.
 * -   `r(a,d)` are the ratios returned by {@link ScaleInput#getRatio(int, int) getRatio}
 *     for accident period `a` and development period `d`.
 * -   `r(d)` are the expected ratios returned by {@link ScaleInput#getRatio(int) getRatio}
 *     for development period `d`.
 * -   if either `w(a,d)`, `r(a,d)` or `r(d)` is NaN, then the cell is ingored
 * -   `n` is the number of not ignored cells in development period `d`. If n
 *     is less then 2, then `s(d)` is NaN.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @see "Quarg & Mack[2004]: Munich Chain Ladder"
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleCalculator<T extends ScaleInput> extends AbstractCalculationData<T> implements Scale<T> {
    
    protected int developments;
    protected double[] scales;
    
    /**
     * Creates an instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultScaleCalculator(T source) {
        super(source);
        doRecalculate();
    }
    
    /**
     * Utility constructor for extending classes implementing the
     * {@link Copyable Copyable} interface. This constructor
     * copies the internal statie (source, length, values) from
     * the original instance, without recalculating itself.
     * 
     * @throws NullPointerException if `original` is null.
     */
    protected DefaultScaleCalculator(DefaultScaleCalculator<T> original) {
        super((T)original.source.copy());
        developments = original.developments;
        if(developments > 0)
            scales = TriangleUtil.copy(original.scales);
    }
    
    /**
     * Returns the source for this calculation.
     */
    @Override
    public T getSourceInput() {
        return source;
    }
    
    /**
     * Retunrs the number of estimated scale parameters.
     * 
     * @see ScaleInput#getDevelopmentCount() 
     */
    @Override
    public int getLength() {
        return developments;
    }
    
    /**
     * Retunrs the estimated scale parameter or NaN if
     * `development` is outside of the bounds
     * `[0; getLength()]`.
     */
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return scales[development];
    }
    
    @Override
    public double[] toArray() {
        return TriangleUtil.copy(scales);
    }
    
    @Override
    public DefaultScaleCalculator<T> copy() {
        return new DefaultScaleCalculator<T>((T)source.copy());
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        scales = new double[developments];
        for(int d=0; d<developments; d++)
            scales[d] = calculateScale(d);
    }
    
    private double calculateScale(int development) {
        double ratio = source.getRatio(development);
        if(Double.isNaN(ratio)) return Double.NaN;
        
        int n = 0;
        double sDik = 0d;
        double t;
        
        int accidents = source.getAccidentCount();
        
        for(int a=0; a<accidents; a++) {
            double w = source.getWeight(a, development);
            double rik = source.getRatio(a, development);
            
            if(!Double.isNaN(rik) && !Double.isNaN(w)) {
                n++;
                t = rik - ratio;
                sDik += w * t * t;
            }
        }
        
        return (--n <= 0 || !(sDik > 0d))?  
                Double.NaN : 
                Math.sqrt(sDik/(double)n);
    }
}