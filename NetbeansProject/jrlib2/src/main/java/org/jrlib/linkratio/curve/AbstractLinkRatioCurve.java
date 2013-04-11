package org.jrlib.linkratio.curve;

import org.jrlib.Copyable;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.util.AbstractLinearRegression;

/**
 * Abstract class for curves that want to estimate the link ratios
 * as a linear regression. The general formula for the linear regression
 * is:
 *      g(lr(d)) = a + b * (d-1)
 * where
 * -   `g()` is a onedimensional function,
 * -   `lr(d)` is the estimated link ratio for development period `d`
 * It should be noted that this class fits a model to the 1-based
 * development period indices.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLinkRatioCurve extends AbstractLinearRegression<LinkRatio> implements LinkRatioCurve {

    protected double pa = 1d;
    protected double pb = 1d;
    
    /**
     * Retunrs the intercept for the linear regression model.
     */
    public double getA() {
        return pa;
    }
    
    /**
     * Retunrs the slope for the linear regression model.
     */
    public double getB() {
        return pb;
    }
    
    @Override
    protected double[] getYs(LinkRatio source) {
        int devs = source.getLength();
        double[] y = new double[devs];
        for(int d=0; d<devs; d++)
            y[d] = getY(source.getValue(d));
        return y;
    }
    
    /**
     * Extending classes should transform the given link-ratio
     * to the form expected by it's regression model (for 
     * exmple taking the natural logarithm etc).
     */
    protected abstract double getY(double lr);

    @Override
    protected double[] getXs(LinkRatio source) {
        return super.getXOneBased(source.getLength());
    }
    
    /**
     * Utility method for implementing the {@link Copyable Copyable}
     * interface. Copies the inner state (fitted model) from
     * the input data.
     * 
     * @throws NullPointerException if `original` is null.
     */
    protected void copyStateFrom(AbstractLinkRatioCurve original) {
        super.copyStateFrom(original);
        this.pa = original.pa;
        this.pb = original.pb;
    }
}
