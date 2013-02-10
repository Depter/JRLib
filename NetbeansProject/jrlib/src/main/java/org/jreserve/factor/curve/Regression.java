package org.jreserve.factor.curve;

import org.jreserve.factor.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Regression {

    public final static int INTERCEPT = 0;
    public final static int SLOPE = 1;
    
    public static double[] fit(double[] y) {
        int size = y.length;
        double[] x = new double[size];
        for(int i=0; i<size; i++)
            x[i] = (i+1);
        return linearRegression(x, y);
    }
    
    public static double[] linearRegression(double[] x, double[] y) {
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
        if(x.length == 0)
            throw new IllegalArgumentException("Length of input is 0!");
    }
    
    private static boolean[] getUsed(double[] x, double[] y) {
        int size = x.length;
        boolean[] used = new boolean[size];
        boolean hasUsed = false;
        
        for(int i=0; i<size; i++) {
            if(!Double.isNaN(x[i]) && !Double.isNaN(y[i])) {
                used[i] = true;
                hasUsed = true;
            }
        }
        
        if(!hasUsed)
            throw new IllegalArgumentException("All rows contains NaN!");
        
        return used;
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
    
    public static double rSquareModel(LinkRatio lr, LinkRatioFunction lrf) {
        double[] original = lr.toArray();
        double[] fitted = predict(lrf, original.length);
        return rSquare(original, fitted);
    }
    
    private static double[] predict(LinkRatioFunction lrf, int developments) {
        double[] result = new double[developments];
        for(int d=0; d<developments; d++)
            result[d] = lrf.getValue(d+1);
        return result;
    }
    
    public static double rSquareModel(double[] original, double[] model) {
        double[] fitted = predict(model[0], model[1], original.length);
        return rSquare(original, fitted);
    }
    
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
    
    public static double[] predict(double intercept, double slope, int n) {
        double[] result = new double[n];
        for(int i=0; i<n; i++)
            result[i] = intercept + (i+1) * slope;
        return result;
    }
    
    private Regression() {
    }
}
