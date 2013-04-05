package org.jreserve.scale;

import static java.lang.Double.isNaN;
import static java.lang.Math.sqrt;
import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RatioScaleCalculator<T extends RatioScaleInput> extends AbstractCalculationData<T> implements RatioScale<T> {
    
    protected int developments;
    protected double[] scales;
    
    protected RatioScaleCalculator(T source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public T getSourceInput() {
        return source;
    }
    
    @Override
    public int getDevelopmentCount() {
        return developments;
    }
    
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
    public RatioScaleCalculator<T> copy() {
        return new RatioScaleCalculator<T>((T)source.copy());
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
        if(isNaN(ratio)) return Double.NaN;
        
        int n = 0;
        double sDik = 0d;
        double t;
        
        int accidents = source.getAccidentCount(development);
        
        for(int a=0; a<accidents; a++) {
            double w = source.getWeight(a, development);
            double rik = source.getRatio(a, development);
            
            if(!isNaN(rik)) {
                n++;
                t = rik - ratio;
                sDik += w * t * t;
            }
        }
        
        return (--n <= 0 || !(sDik > 0d))?  Double.NaN : sqrt(sDik/(double)n);
    }
}
