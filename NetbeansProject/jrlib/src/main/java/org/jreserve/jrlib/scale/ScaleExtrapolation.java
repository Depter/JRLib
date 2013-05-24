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
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.util.method.AbstractLinearRegression;

/**
 * Calculates a scale parameter by using linear regression on the input.
 * The model for the linear regression is specified as:
 *      ln(s(d)) ~ a + b * t
 *      t = d + 1
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleExtrapolation<T extends ScaleInput> extends AbstractLinearRegression<Scale<T>> implements ScaleEstimator<T> {
    
    private boolean isEmptyModel = true;

    public ScaleExtrapolation() {
    }
    
    @Override
    public void fit(Scale<T> source) {
        int developments = (source==null)? 0 : source.getLength();
        if(developments <= 1) {
            fitEmptyModel();
        } else {
            isEmptyModel = false;
            super.fit(source);
        }
    } 
    
    private void fitEmptyModel() {
        isEmptyModel = true;
        intercept = Double.NaN;
        slope = Double.NaN;
    }
    
    /**
     * Returns the natural logarithm of the scale parameters from 
     * the source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    @Override
    protected double[] getYs(Scale<T> source) {
        int developments = source.getLength();
        double[] y = new double[developments];
        for(int d=0; d<developments; d++)
            y[d] = getLnY(source.getValue(d));
        return y;
    }
    
    private double getLnY(double y) {
        if(!Double.isNaN(y) && y > 0d)
            return Math.log(y);
        return Double.NaN;
    }

    /**
     * Returns an array containing the values 
     * `[1, ..., {@link Scale#getLength() source.getLength()}]`.
     * 
     * @throws NullPointerException if `source` is null.
     */
    @Override
    protected double[] getXs(Scale<T> source) {
        int developments = source.getLength();
        return super.getXOneBased(developments);
    }
    
    /**
     * Retunrs the estimated value for the given development period.
     */
    @Override
    public double getValue(int development) {
        if(isEmptyModel || development<0)
            return Double.NaN;
        double x = (double) (development+1);
        return Math.exp(intercept + slope * x);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ScaleExtrapolation);
    }
    
    @Override
    public int hashCode() {
        return ScaleExtrapolation.class.hashCode();
    }
    
    @Override
    public String toString() {
        if(isEmptyModel)
            return "ScaleExtrapolation [ln(sigma(k)) = NaN]";
        return String.format(
            "ScaleExtrapolation [ln(sigma(k)) = %f + (%f) * k]",
            intercept, slope);
    }
}