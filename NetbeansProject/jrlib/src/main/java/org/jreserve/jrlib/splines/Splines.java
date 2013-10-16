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
package org.jreserve.jrlib.splines;

import java.util.Arrays;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Splines {
    
    private static double[] indexArray(int lenght) {
        double[] result = new double[lenght];
        for(int i=0; i<lenght; i++)
            result[i] = i+1;
        return result;
    }
    
    public static SplineCurve qubicSpline(double[] y) {
        return qubicSpline(indexArray(y.length), y);
    }
    
    public static SplineCurve qubicSpline(double[] x, double[] y) {
        checkInputArr(x, y);
        
        int n = y.length;
        double[] h = new double[n];
        double[] p = new double[n];
        double[] q = new double[n];
        double[] b = new double[n];
        
        double[] ra = new double[n];
        double[] rb = new double[n];
        double[] rc = new double[n];
        double[] rd = new double[n];
        
        h[0] = x[1] - x[0];
        for(int i=1; i<(n-1); i++) {
            h[i] = x[i+1] - x[i];
            p[i] = 2d * (x[i+1] - x[i-1]);
            q[i] = 3d * (y[i+1] - y[i]) / h[i] - 3d * (y[i] - y[i-1]) / h[i-1];
        }
        h[n-1] = 0d - x[n-1];
        p[n-1] = 2d * (0d - x[n-2]);
        q[n-1] = 3d * (0d - y[n-1]) / h[n-1] - 3d * (y[n-1] - y[n-2]) / h[n-2];
        
        //gaussian elimination
        for(int i=2; i<n; i++) {
            p[i] = p[i] - h[i-1] * h[i-1] / p[i-1];
            q[i] = q[i] - q[i-1] * h[i-1] / p[i-1];
        }
        
        //backsubstitution
        b[n-1] = q[n-1] / p[n-1];
        for(int i=2; i<n; i++) {
            b[n-i] = (q[n-i] - h[n-i] * b[n-i+1]) / p[n-i];
        }
        
        //Spline parameters
        ra[0] = b[1] / (3d * h[0]);
        rb[0] = 0;
        rc[0] = (y[1] - y[0]) / h[0] - b[1] * h[0] / 3d;
        rd[0] = y[0];
        for(int i=1; i<n; i++) {
            double bIPlus1 = (i==(n-1))? 0 : b[i+1];
            ra[i] = (bIPlus1 - b[i]) / (3d * h[i]);
            rb[i] = b[i];
            rc[i] = (b[i] + b[i-1]) * h[i-1] + rc[i-1];
            rd[i] = y[i];
        }
        
        return createCurve(x, ra, rb, rc, rd);
    }
    
    private static void checkInputArr(double[] x, double[] y) {
        if(x == null)
            throw new NullPointerException("x[] is null!");
        if(y == null)
            throw new NullPointerException("y[] is null!");
        if(y.length < 2)
            throw new IllegalArgumentException("y.length < 2! "+y.length);
        if(x.length != y.length) {
            String msg = "x.length(%d) != y.length(%d)!";
            msg = String.format(msg, x.length, y.length);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private static SplineCurve createCurve(double[] x, double[] a, double[] b, double[] c, double[] d) {
        SplineCurve sc = new SplineCurve();
        int count = x.length;
        for(int i=0; i<count; i++) {
            sc.addSpline(new Spline(x[i], a[i], b[i], c[i], d[i]));
        }
        return sc;
    }
    
    private static double[] constantArray(int length, double value) {
        double[] result = new double[length];
        Arrays.fill(result, value);
        return result;
    }
    
    public static SplineCurve smoothSpline(double[] y, double lambda) {
        return smoothSpline(y, constantArray(y.length, 1d), lambda);
    }
    
    public static SplineCurve smoothSpline(double[] y, double[] sigma, double lambda) {
        return smoothSpline(indexArray(y.length), y, sigma, lambda);
    }
    
    public static SplineCurve smoothSpline(double[] x, double[] y, double[] sigma, double lambda) {
        checkInputArr(x, y);
        if(sigma == null)
            throw new NullPointerException("sigma[] is null!");
        if(sigma.length != y.length) {
            String msg = "sigma.length(%d) != y.length(%d)!";
            msg = String.format(msg, sigma.length, y.length);
            throw new IllegalArgumentException(msg);
        }
        
        int n = y.length;
        double[] h = new double[n];
        double[] r = new double[n];
        double[] f = new double[n];
        double[] p = new double[n];
        double[] q = new double[n];
        double[] u = new double[n];
        double[] v = new double[n];
        double[] w = new double[n];
        
        double[] ra = new double[n];
        double[] rb = new double[n];
        double[] rc = new double[n];
        double[] rd = new double[n];
        
        //initial values
        h[0] = x[1] - x[0];
        r[0] = 3d/h[0];
        for(int i=1; i<(n-1); i++) {
            h[i] = x[i+1] - x[i];
            r[i] = 3d / h[i];
            f[i] = -(r[i-1] + r[i]);
            p[i] = 2d * (x[i+1] - x[i-1]);
            q[i] = 3d * (y[i+1] - y[i]) / h[i] - 3d * (y[i] - y[i-1]) / h[i-1];
        }
        
        //diagonals of [W+LAMBDA * T' * SIGMA * T]
        for(int i=1; i<(n-1); i++) {
            u[i] = r[i-1] * r[i-1] * sigma[i-1] + f[i] * f[i] * sigma[i] + r[i] * r[i] * sigma[i+1];
            u[i] = lambda * u[i] + p[i];
            
            v[i] = f[i] * r[i] * sigma[i] + r[i] * f[i+1] * sigma[i+1];
            v[i] = lambda * v[i] + h[i];
            
            w[i] = lambda * r[i] * r[i+1] * sigma[i+1];
        }
        
        //decomposition, L' * D * L
        v[1] = v[1] / u[1];
        w[1] = w[1] / u[1];
        for(int i=2; i<(n-1); i++) {
            u[i] = u[i] - u[i-2] * w[i-2] * w[i-2] - u[i-1] * v[i-1] * v[i-1];
            v[i] = (v[i] - u[i-1] * v[i-1] * w[i-1]) / u[i];
            w[i] = w[i] / u[i];
        }
        
        //gaussian elimination for Lx = T'y
        q[0] = 0d;
        for(int i=2; i<(n-1); i++)
            q[i] = q[i] - v[i-1] * q[i-1] - w[i-2] * q[i-2];
        for(int i=1; i<(n-1); i++)
            q[i] = q[i] / u[i];
        
        //gaussian elimination for L'c = (D^-1)y
        q[n-2] = q[n-2] - v[n-2] * q[n-1];
        for(int i=n-3; i>0; i--)
            q[i] = q[i] - v[i] * q[i+1] - w[i] * q[i+2];
        
        //coefficients
        rd[0] = y[0] - lambda * r[0] * q[1] * sigma[0];
        rd[1] = y[1] - lambda * (f[1] * q[1] + r[1] * q[2]) * sigma[0];
        ra[0] = q[1] / (3d * h[0]);
        rb[0] = 0d;
        rc[0] = (rd[1] - rd[0]) / h[0] - q[1] * h[0] / 3d;
        for(int i=1; i<(n-1); i++) {
            ra[i] = (q[i+1] - q[i]) / (3d * h[i]);
            rb[i] = q[i];
            rc[i] = (q[i] + q[i-1]) * h[i-1] + rc[i-1];
            
            rd[i] = r[i-1] * q[i-1] + f[i] * q[i] + r[i] * q[i+1];
            rd[i] = y[i] - lambda * rd[i] * sigma[i];
        }
        rd[n-1] = y[n-1] - lambda * r[n-2] * q[n-2] * sigma[n-1];
        
        return createCurve(x, ra, rb, rc, rd);
    }
    
    private Splines() {
    }
}
