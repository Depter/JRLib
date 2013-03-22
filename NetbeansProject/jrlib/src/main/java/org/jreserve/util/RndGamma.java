package org.jreserve.util;

import org.jreserve.bootstrap.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RndGamma {
    private final static double Q1 =  0.0416666664;
    private final static double Q2 =  0.0208333723;
    private final static double Q3 =  0.0079849875;
    private final static double Q4 =  0.0015746717;
    private final static double Q5 = -0.0003349403;
    private final static double Q6 =  0.0003340332;
    private final static double Q7 =  0.0006053049;
    private final static double Q8 = -0.0004701849;
    private final static double Q9 =  0.0001710320;

    private final static double A1 =  0.333333333;
    private final static double A2 = -0.249999949;
    private final static double A3 =  0.199999867;
    private final static double A4 = -0.166677482;
    private final static double A5 =  0.142873973;
    private final static double A6 = -0.124385581;
    private final static double A7 =  0.110368310;
    private final static double A8 = -0.112750886;
    private final static double A9 =  0.104089866;

    private final static double E1 = 1.000000000;
    private final static double E2 = 0.499999994;
    private final static double E3 = 0.166666848;
    private final static double E4 = 0.041664508;
    private final static double E5 = 0.008345522;
    private final static double E6 = 0.001353826;
    private final static double E7 = 0.000247453;
    
    private final Random rnd;
    
    public RndGamma(Random random) {
        this.rnd = random;
    }
    
    public double rndGammaFromMeanVariance(double mean, double variance) {
        double alpha = mean * mean / variance; //mean ^2 / variance;
        double lambda = 1d / (variance/mean); //1d / (variance / mean)
        return rndGamma(alpha, lambda);
    }
    
    public double rndGamma(double alpha, double lambda) {
        if(alpha <= 0.0) throw new IllegalArgumentException(); 
        if(lambda <= 0.0) new IllegalArgumentException(); 
        return (alpha < 1d)?
            // CASE A: Acceptance rejection algorithm gs
            rndGammaAcceptanceRejection(alpha, lambda) :
            // CASE B: Acceptance complement algorithm gd (gaussian distribution, box muller transformation)
            rndGammaAcceptanceComplement(alpha, lambda);
    }
    
    private double rndGammaAcceptanceRejection(double alpha, double lambda) {
            double b = 1.0 + 0.36788794412 * alpha;              // Step 1
            for(;;) {
                double gds;
                double p = b * rnd.nextNonZeroDouble();
                if(p <= 1.0) {                       // Step 2. Case gds <= 1
                    gds = Math.exp(Math.log(p) / alpha);
                    if(Math.log(rnd.nextNonZeroDouble()) <= -gds) 
                        return(gds/lambda);
                } else {                                // Step 3. Case gds > 1
                    gds = - Math.log ((b - p) / alpha);
                    if (Math.log(rnd.nextNonZeroDouble()) <= ((alpha - 1.0) * Math.log(gds))) 
                        return(gds/lambda);
                }
            }
    }
    
    private double rndGammaAcceptanceComplement(double alpha, double lambda) {
        
        
        // Step 1. Preparations
        double ss = 0d, s = 0d, d = 0d;
        if(alpha != -1d) {
            ss = alpha - 0.5;
            s = Math.sqrt(ss);
            d = 5.656854249 - 12.0 * s;
        }
        
        // Step 2. Normal deviate
        double t, x, gds;
        t = initT();
        x = s + 0.5 * t;
        gds = x * x;
        // Immediate acceptance
        if(t >= 0.0) 
            return(gds/lambda);         
        
        // Step 3. Uniform random number
        double u;
        u = rnd.nextNonZeroDouble();
        if(d * u <= t * t * t) 
            return(gds/lambda); // Squeeze acceptance
        
        // Step 4. Set-up for hat case
        double q0 = 0d, b = 0d, si = 0d, c = 0d;
        if(alpha != -1d) {
            q0 = approxQ0(alpha);
            if(alpha > 3.686) {
                if(alpha > 13.022) {
                    b = 1.77;
                    si = 0.75;
                    c = 0.1515 / s;
                } else {
                    b = 1.654 + 0.0076 * ss;
                    si = 1.68 / s + 0.275;
                    c = 0.062 / s + 0.024;
                }
            } else {
                b = 0.463 + s - 0.178 * ss;
                si = 1.235;
                c = 0.195 / s - 0.079 + 0.016 * s;
            }
        }
        
        // Step 5. Calculation of q
        double q;
        if(x > 0.0) {
            // Step 6.
            q = calculateQ(q0, t, s, ss);
            
            // Step 7. Quotient acceptance
            if(Math.log(1.0 - u) <= q)
                return(gds/lambda);
        } 
        
        // Step 8. Double exponential deviate t
        double w, sign_u, e;
        for(;;) {
            // Step 9. Rejection of t
            do {
                e = -Math.log(rnd.nextNonZeroDouble());
                u = rnd.nextNonZeroDouble();
                u = u + u - 1.0;
                sign_u = (u > 0)? 1.0 : -1.0;
                t = b + (e * si) * sign_u;
            } while (t <= -0.71874483771719);
            
            // Step 10. New q(t)
            q = calculateQ(q0, t, s, ss);
            
            // Step 11.
            if(q <= 0.0)
                continue;
            w = calculateW(q);
            
            // Step 12. Hat acceptance
            if( c * u * sign_u <= w * Math.exp(e - 0.5 * t * t)) {
                x = s + 0.5 * t;
                return(x*x/lambda);
            }
        }
    }

    private double initT() {
        double v1, v2, v12;
        do{
            v1 = 2.0 * rnd.nextNonZeroDouble() - 1.0;
            v2 = 2.0 * rnd.nextNonZeroDouble() - 1.0;
            v12 = v1*v1 + v2*v2;
        } while( v12 > 1.0 );

        return v1*Math.sqrt(-2.0*Math.log(v12)/v12);
    }
    
    private double approxQ0(double alpha) {
        double r = 1.0 / alpha;
        return ((((((((Q9*r+Q8)*r+Q7)*r+Q6)*r+Q5)*r+Q4)*r+Q3)*r+Q2)*r+Q1)*r;
    }
    
    private double calculateQ(double q0, double t, double s, double ss) {
        double v = t / (s + s);
        return (Math.abs(v) > 0.25)?
            q0 - s * t + 0.25 * t * t + (ss + ss) * Math.log(1.0 + v) :
            approxQ(q0, t, v);
    }
    
    private double approxQ(double q0, double t, double v) {
        double a = ((((((((A9*v+A8)*v+A7)*v+A6)*v+A5)*v+A4)*v+A3)*v+A2)*v+A1)*v;
        return q0 + 0.5 * t * t * a;
    }
    
    private double calculateW(double q) {
        if(q > 0.5)
            return Math.exp(q) - 1.0;
        return ((((((E7*q+E6)*q+E5)*q+E4)*q+E3)*q+E2)*q+E1)*q;
    }    
}
