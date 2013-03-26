package org.jreserve.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRndFunctionTest {
    
    private final static int N = 100000;
    private final static double R_SQUARE_TRESHOLD = 0.99;
    
    @Test
    public void testSimulateEstimate() {
        for(int t=0; t<10; t++) {
            double[] sample = generateSample();
            double[] uBounds = intervalUpperBounds(sample);
            long[] found = countValues(sample, uBounds);
            double[] expected = getExpected(N, uBounds);
            double rSquare = RegressionUtil.rSquare(toDouble(found), expected);
            boolean goodFit = rSquare >= R_SQUARE_TRESHOLD;
            
            if(!goodFit) {
                System.out.println("Test run: "+t+" R^2: "+rSquare);
                
                System.out.println("Index\tExpected\tFound");
                for(int i=0; i<expected.length; i++)
                    System.out.println(""+i+"\t"+expected[i]+"\t"+found[i]);
            }
            
            assertTrue(goodFit);
        }
    }
    
    private double[] generateSample() {
        double[] sample = new double[N];
        for(int i=0; i<N; i++)
            sample[i] = nextRndValue();
        return sample;
    }
    
    protected abstract double nextRndValue();
    

    private double[] intervalUpperBounds(double[] values) {
        int n = values.length;
        int iCount = (int)Math.sqrt((double) n)+1;
        
        double min = values[0];
        double max = values[0];
        for(double v : values) {
            if(v < min) min = v;
            if(v > max) max = v;
        }
        double step = (max - min) / (iCount - 1);
        
        double[] uBounds = new double[iCount];
        uBounds[0] = min + step/((double)2);
        for(int i=1; i<iCount; i++)
            uBounds[i] = uBounds[i-1] + step;
        return uBounds;
    }
    
    private long[] countValues(double[] values, double[] uBounds) {
        long[] counts = new long[uBounds.length];
        for(double v : values)
            counts[getIndex(v, uBounds)]++;
        return counts;
    }
    
    private int getIndex(double value, double[] uBounds) {
        int maxIndex = uBounds.length - 1;
        for(int i=0; i<maxIndex; i++)
            if(value < uBounds[i])
                return i;
        return maxIndex;
    }
     
    
    private double[] getExpected(int count, double[] uBounds) {
        double n = (double) count;
        int maxIndex = uBounds.length - 1;
        double[] expected = new double[maxIndex+1];
        expected[0] = getCummulativeProbability(uBounds[0]) * n;
        for(int i=1; i<maxIndex; i++)
            expected[i] = (getCummulativeProbability(uBounds[i]) - getCummulativeProbability(uBounds[i-1])) * n;
        expected[maxIndex] = (1d - getCummulativeProbability(uBounds[maxIndex-1])) * n;
        return expected;
    }
    
    protected abstract double getCummulativeProbability(double x);
    
    private double[] toDouble(long[] values) {
        int size = values.length;
        double[] result = new double[size];
        for(int i=0;i<size; i++)
            result[i] = values[i];
        return result;
    }
}
