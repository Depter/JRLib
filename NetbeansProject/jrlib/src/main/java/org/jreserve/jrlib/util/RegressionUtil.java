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
package org.jreserve.jrlib.util;

/**
 * Utility class to perform linear regressions (y ~ intercept + slope * x).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class RegressionUtil {

    public final static int INTERCEPT = 0;
    public final static int SLOPE = 1;
    
    /**
     * Linear regression (`y ~ a + b * x`) with x values `1, ..., y.length`.
     * 
     * @see #linearRegression(double[], double[], boolean)
     */
    public static double[] linearRegression(double[] y) {
        return linearRegression(y, true);
    }
    
    /**
     * Linear regression (`y ~ a + b * x`) with x values `1, ..., y.length`,
     * if `withIntercept` is true and `y ~ b * x` if `withIntercept` is false.
     * 
     * @see #linearRegression(double[], double[], boolean)
     */
    public static double[] linearRegression(double[] y, boolean withIntercept) {
        int size = y.length;
        double[] x = new double[size];
        for(int i=0; i<size; i++)
            x[i] = (i+1);
        return linearRegression(x, y, withIntercept);
    }
    
    /**
     * Fits the model `y ~ intercept + slope * x`.
     */
    public static double[] linearRegression(double[] x, double[] y) {
        return linearRegression(x, y, true);
    }
    
    /**
     * Finds the slope (b) and intercept (a) for the linear regression
     * `y = a + bx` ('withIntercept' is true) or `y ~ bx` (`withIntercept`
     * is false). The input values should not be null, must have to same 
     * length and they must contain at least two rows, where both array 
     * contains real values (thus not `Double.NaN` or `Double.INFINITY`
     * or `Double.NEGATIVE_INFINITY`). All value pairs, where one of
     * the values does not conform this criteria will be ignored.
     * 
     * If `withIntercept` is true then:
     *         (sum(y)*sum(x^2) - sum(x)*sum(x*y))
     *     a = --------------------------------
     *             n*sum(x^2) - sum(x)^2
     *     
     *          n*sum(x*y) - sum(x) * sum(y)
     *     b = --------------------------------
     *             n * sum(x^2) - sum(x)^2
     * 
     * If `withIntercept` is false then:
     *     a = 0
     * 
     *         sum(x*y)
     *     b = --------
     *         sum(x^2)
     * 
     * @return am array with length of 2, the first element containing
     * the intercept, the second containing the slope.
     */
    public static double[] linearRegression(double[] x, double[] y, boolean withIntercept) {
        checkInput(x, y);
        boolean[] used = getUsed(x, y);

        double sxy = 0d;
        double sx = 0d;
        double sy = 0d;
        double sxs = 0d;
        int n = 0;
        int size = used.length;
        for(int i=0; i<size; i++) {
            if(used[i]) {
                double xv = x[i];
                double yv = y[i];
                
                sxy += (xv*yv);
                sx += xv;
                sy += yv;
                sxs += (xv*xv);
                n++;
            }
        }
        
        if(!withIntercept)
            return new double[]{0d, sxy / sxs};
        
        double dn = (double) n;
        double denom = dn*sxs - sx*sx;
        double slope = (dn*sxy - sx*sy) / denom;
        double intercept = (sy*sxs - sx*sxy) / denom;
        return new double[]{intercept, slope};
    }
    
    private static void checkInput(double[] x, double[] y) {
        if(x==null || y==null)
            throw new NullPointerException();
        if(x.length != y.length)
            throw new IllegalArgumentException("Length of x ("+x.length+") must be equal to length of y ("+y.length+")!");
        if(x.length < 2)
            throw new IllegalArgumentException("Length of input is less then 2!");
    }
    
    private static boolean[] getUsed(double[] x, double[] y) {
        int size = x.length;
        boolean[] used = new boolean[size];
        
        for(int i=0; i<size; i++)
            if(isValidInput(x[i]) && isValidInput(y[i]))
                used[i] = true;
        
        return used;
    }
    
    private static boolean isValidInput(double v) {
        return !Double.isNaN(v) && !Double.isInfinite(v);
    }
    
    private static double mean(double[] x, boolean[] used) {
        int size = x.length;
        double sum = 0d;
        int n = 0;
        
        for(int i=0; i<size; i++) {
            if(used[i]) {
                sum += x[i];
                n++;
            }
        }
        
        return sum / (double)n;
    }
    
    /**
     * Returns the R-square for the given model. The model is an
     * array, with length=2 and `model[0] = intercept` and
     * `model[1] = slope`.
     * 
     * @throws NullPointerException when `orogonal` or `model` is null.
     * @throws IndexOutOfBoundsException when `model.length &lt; 2`.
     */
    public static double rSquareModel(double[] original, double[] model) {
        double[] fitted = predict(model[0], model[1], original.length);
        return rSquare(original, fitted);
    }
    
    /**
     * Calculates the R-Square for the given fitted values.
     * 
     * R-Square:
     *           SST - SSE
     *     R^2 = ---------
     *              SST
     * where:
     *     SSE = sum((original - fitted)^2),
     *     SST = sum((original - mean)^2),
     *     mean(y) = sum(y) / count(y)
     * 
     * @throws NullPointerException if one of the arrays is null.
     * @throws IllegalArgumentException if the length of the arrays 
     * do not match.
     */
    public static double rSquare(double[] original, double[] fitted) {
        checkInput(original, fitted);
        boolean[] used = getUsed(original, fitted);
        double sst = sst(original, used);
        double sse = sse(original, fitted, used);
        if(Double.isNaN(sse) || Double.isNaN(sst) || sst==0)
            return Double.NaN;
        return 1d - sse/sst;
    }
    
    private static double sse(double[] original, double[] fitted, boolean[] used) {
        int size = original.length;
        double sse = 0d;
        for(int i=0; i<size; i++)
            if(used[i])
                sse += Math.pow(original[i] - fitted[i], 2d);
        return sse;
    }
    
    private static double sst(double[] y, boolean[] used) {
        int size = y.length;
        double mean = mean(y, used);
        
        double sst = 0d;
        for(int i=0; i<size; i++)
            if(used[i])
                sst += Math.pow(y[i] - mean, 2d);
        return sst;
    }
    
    /**
     * Creates `n` predictions, where x'es are 1 based indices.
     * Calling this has the same result as calling
     * {@link #predict(double, double, double[]) predict}(intercept, slope, {1, ..., n).
     * 
     * @see #predict(double, double, double[]) 
     */
    public static double[] predict(double intercept, double slope, int n) {
        double[] result = new double[n];
        for(int i=0; i<n; i++)
            result[i] = intercept + (i+1) * slope;
        return result;
    }
    
    /**
     * Creates a prediction in the form of `y ~ intercept + slope * x`.
     * The resulting array will have the same length as x.
     * 
     * @throws NullPointerException if x is null.
     */
    public static double[] predict(double intercept, double slope, double[] x) {
        int size = x.length;
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = intercept + slope * x[i];
        return result;
    }
    
    private RegressionUtil() {
    }

}
