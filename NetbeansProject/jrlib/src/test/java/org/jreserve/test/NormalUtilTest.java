package org.jreserve.test;

import org.jreserve.JRLibTestUtl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class NormalUtilTest {

    private final static double[] X = {
        -3.5, -2.3, -1.1, 0.1, 1.2, 
         2.3,  3.4,  4.5, 5.6, 6.7, 7.8
    };
    
    private final static double MEAN = 1.25;
    private final static double SIGMA = 2.34;
    
    private final static double[] DF = {
        0.00087268, 0.02832704, 0.21785218, 0.39695255, 0.19418605, 
        0.02832704, 0.00123222, 0.00001598, 0.00000006, 0.00000000, 0.00000000
    };
    
    private final static double[] DF_MEAN_SIGMA = {
        0.02172338, 0.05394034, 0.10296439, 0.15109410, 0.17044924, 
        0.15416014, 0.11178372, 0.06498521, 0.03028865, 0.01131813, 0.00339078
    };
    
    private final static double[] CDF = {
        0.00023263, 0.01072411, 0.13566606, 0.53982784, 0.88493033, 
        0.98927589, 0.99966307, 0.99999660, 0.99999999, 1.00000000, 1.00000000
    };
    
    private final static double[] CDF_MEAN_SIGMA = {
        0.02118261, 0.06462147, 0.15762340, 0.31155305, 0.49147624, 
        0.67318243, 0.82090080, 0.91756673, 0.96848461, 0.99007205, 0.99743810
    };
    
    public NormalUtilTest() {
    }

    @Test
    public void testNormDF_double() {
        for(int i=0; i<X.length; i++)
            assertEquals(DF[i], NormalUtil.normDF(X[i]), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testNormDF_3args() {
        for(int i=0; i<X.length; i++)
            assertEquals(DF_MEAN_SIGMA[i], NormalUtil.normDF(X[i], MEAN, SIGMA), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testNormCDF() {
        for(int i=0; i<X.length; i++)
            assertEquals(CDF[i], NormalUtil.normCDF(X[i]), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testNormCDF_3args() {
        for(int i=0; i<X.length; i++)
            assertEquals(CDF_MEAN_SIGMA[i], NormalUtil.normCDF(X[i], MEAN, SIGMA), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testInvNormCDF() {
        for(int i=0; i<X.length; i++) {
            double x = X[i];
            double cdf = NormalUtil.normCDF(x);
            assertEquals(x, NormalUtil.invNormCDF(cdf), 0.01);
        }
    }
    
    @Test
    public void testInvNormCDF_3args() {
        for(int i=0; i<X.length; i++) {
            double x = X[i];
            double cdf = NormalUtil.normCDF(x, MEAN, SIGMA);
            assertEquals(x, NormalUtil.invNormCDF(cdf, MEAN, SIGMA), JRLibTestUtl.EPSILON);
        }
    }
}