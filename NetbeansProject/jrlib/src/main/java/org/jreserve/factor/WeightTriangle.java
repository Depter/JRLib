package org.jreserve.factor;

import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeightTriangle extends InputTriangle {

    public static Triangle getDefault() {
        return new WeightTriangle(new double[0][]);
    }
    
    public WeightTriangle(double[][] weights) {
        super(weights);
    }
    
    public void setWeights(double[][] weights) {
        super.setData(weights);
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return super.getValue(accident, development);
        return 1d;
    }
}
