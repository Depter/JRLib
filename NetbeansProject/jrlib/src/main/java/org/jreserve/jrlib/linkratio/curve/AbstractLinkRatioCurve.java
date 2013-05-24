/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.util.method.AbstractLinearRegression;

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
}
