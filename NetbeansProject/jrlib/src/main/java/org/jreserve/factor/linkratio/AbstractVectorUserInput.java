package org.jreserve.factor.linkratio;

import java.util.Arrays;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractVectorUserInput {
    protected double[] values;
    
    protected AbstractVectorUserInput() {
        this.values = new double[0];
    }
    
    protected AbstractVectorUserInput(double[] values) {
        int length = values.length;
        this.values = new double[length];
        System.arraycopy(values, 0, this.values, 0, length);
    }
    
    
    public void setValue(int development, double value) {
        ensureIndexExists(development);
        values[development] = value;
    }
    
    private void ensureIndexExists(int development) {
        int length = values.length;
        if(length <= development)
            redimValues(development+1);
    }
    
    private void redimValues(int length) {
        int oldLength = values.length;
        double[] redim = new double[length];
        System.arraycopy(values, 0, redim, 0, oldLength);
        values = redim;
        Arrays.fill(values, oldLength, length-1, Double.NaN);
    }
    
    public double getValue(int development) {
        if(development < 0 || development >= values.length)
            return Double.NaN;
        return values[development];
    }
    
}
