package org.jreserve.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.jreserve.bootstrap.JavaRandom;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class RndNormalTest extends AbstractRndFunctionTest {
    private final static long SEED = 1000;
    private final static double MEAN = 1000;
    private final static double SIGMA = 100;

    private RndNormal rnd;
    private NormalDistribution nd;
    
    public RndNormalTest() {
    }

    @Before
    public void setUp() {
        rnd = new RndNormal(new JavaRandom(SEED));
        nd = new NormalDistribution(MEAN, SIGMA);
    }
    
    @Override
    protected double nextRndValue() {
        return rnd.nextNormal(MEAN, SIGMA);
    }

    @Override
    protected double getCummulativeProbability(double x) {
        return nd.cumulativeProbability(x);
    }
//
//    private final static int N = 100;
//    private final static double[] EXPECTED = {
//          0.00002867,  0.00313846,  0.13182268, 2.14002339, 13.59051220, 
//         34.13447461, 34.13447461, 13.59051220, 2.14002339,  0.13182268, 
//          0.00313846 
//    };
//    private final static double ALPHA = 0.05;
//    
//    private ChiSquareTest test;
//
//    @Test
//    public void testNextNormal() {
//        for(int t=0; t<10; t++) {
//            double[] values = new double[N];
//            for(int i=0; i<N; i++)
//                values[i] = rnd.nextNormal(MEAN, SIGMA);
//            long[] found = countFrequencies(values);
//            assertTrue(test.chiSquareTest(EXPECTED, found, ALPHA));
//        }
//    }
//    
//    private long[] countFrequencies(double[] values) {
//        long[] freqs = new long[11];
//        for(double value : values) {
//            int index = getFrequencyIndex(value);
//            freqs[index]++;
//        }
//        return freqs;
//    }
//    
//    private int getFrequencyIndex(double value) {
//        if(value <= 1000d)
//            return 0;
//        else if(1000d < value && value <= 1100d)
//            return 1;
//        else if(1100d < value && value <= 1200d)
//            return 2;
//        else if(1200d < value && value <= 1300d)
//            return 3;
//        else if(1300d < value && value <= 1400d)
//            return 4;
//        else if(1400d < value && value <= 1500d)
//            return 5;
//        else if(1500d < value && value <= 1600d)
//            return 6;
//        else if(1600d < value && value <= 1700d)
//            return 7;
//        else if(1700d < value && value <= 1800d)
//            return 8;
//        else if(1800d < value && value <= 1900d)
//            return 9;
//        else
//            return 10;
//    }
//
//    @Test
//    public void testNextNormalFromVariance() {
//        double var = SIGMA * SIGMA;
//        for(int t=0; t<10; t++) {
//            double[] values = new double[N];
//            for(int i=0; i<N; i++)
//                values[i] = rnd.nextNormalFromVariance(MEAN, var);
//            long[] found = countFrequencies(values);
//            assertTrue(test.chiSquareTest(EXPECTED, found, ALPHA));
//        }
//    }
}