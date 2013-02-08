package org.jreserve.factor.curve;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinearRegression {

    public static double[] fit(double[] y) {
        int size = y.length;
        double[] x = new double[size];
        for(int i=0; i<size; i++)
            x[i] = (i+1);
        return fit(x, y);
    }
    
    public static double[] fit(double[] x, double[] y) {
        checkInput(x, y);
        boolean[] used = getUsed(x, y);
        double slope = slope(x, y, used);
        double intercept = intercept(x, y, used, slope);
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
    
    private static double slope(double[] x, double[]y, boolean[] used) {
        //SLOPE = sum((x-mean(x)) * (y-mean(y))) / sum((x-mean(x))^2)
        int size = x.length;
        
        double meanX = mean(x, used);
        double meanY = mean(y, used);
        
        double s1 = 0d;
        double s2 = 0d;
        for(int i=0; i<size; i++) {
            if(used[i]) {
                double xmX = x[i]-meanX;
                double ymY = y[i]-meanY;
                s1 += (xmX * ymY);
                s2 += Math.pow(xmX, 2);
            }
        }
        return (Double.isNaN(s1) || Double.isNaN(s2) || s2==0)? 0 : s1/s2;
    }
    
    private static double intercept(double[] x, double[] y, boolean[] used, double slope) {
        int n=0;
        int size = x.length;
        double sum = 0d;
        
        for(int i=0; i<size; i++) {
            if(used[i]) {
                sum += y[i] - x[i] * slope;
                n++;
            }
        }
        
        return sum / (double)n;
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
    
    private LinearRegression() {
    }
}
