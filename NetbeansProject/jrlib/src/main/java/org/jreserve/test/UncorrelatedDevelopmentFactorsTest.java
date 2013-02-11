package org.jreserve.test;

import java.util.Arrays;
import java.util.Comparator;
import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UncorrelatedDevelopmentFactorsTest extends AbstractCalculationData<Triangle> {
    
    private final static Comparator<Double> RANK_COMPARATOR = new Comparator<Double>() {
        @Override
        public int compare(Double o1, Double o2) {
            if(o1==null)
                return o2==null? 0 : 1;
            return o2==null? -1 : comparePrimitive(o1, o2);
        }
        
        private int comparePrimitive(double d1, double d2) {
            if(Double.isNaN(d1))
                return Double.isNaN(d2)? 0 : 1;
            if(Double.isNaN(d2)) 
                return -1;
            if(d1 == d2)
                return 0;
            return d1<d2? -1 : 1;
        }
    };
    
    private double testValue;
    private double lowerBound;
    private double alpha;
    private double pValue;
    
    public UncorrelatedDevelopmentFactorsTest(Triangle factors) {
        this(factors, 0.5);
    }
    
    public UncorrelatedDevelopmentFactorsTest(Triangle factors, double alpha) {
        super(factors);
        checkAlpha(alpha);
        this.alpha = alpha;
        doRecalculate();
    }
    
    public boolean isTestPassed() {
        return pValue <= alpha;
    }
    
    public double getTestValue() {
        return testValue;
    }
    
    public double getLowerBound() {
        return lowerBound;
    }
    
    public double getUpperBound() {
        return -lowerBound;
    }
    
    public double getAlpha() {
        return alpha;
    }
    
    public void setAlpha(double alpha) {
        checkAlpha(alpha);
        this.alpha = alpha;
        fireChange();
    }
    
    private void checkAlpha(double alpha) {
        if(alpha < 0d || alpha>1d)
            throw new IllegalArgumentException("Alpha must be within [0;1], but it was "+alpha+"!");
    }
    public double getPValue() {
        return pValue;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        testValue = 0d;
        double testSigma = 0d;
        for(RankHelper helper : createHelpers()) {
            if(helper.shouldUse()) {
                double nMin1 = (helper.getN() - 1);
                testSigma += nMin1;
                testValue += helper.getT() * nMin1;
            }
        }
        testValue = testValue/testSigma;
        testSigma = Math.sqrt(1d/testSigma);
        lowerBound = NormalUtil.invNormCDF(0.5-alpha/2d) * testSigma;
        pValue = 1d - 2d * NormalUtil.normCDF(testValue/testSigma);
        if(pValue < 0)
            pValue = -pValue;
    }
    
    private RankHelper[] createHelpers() {
        int developments = source.getDevelopmentCount();
        RankHelper[] helpers = new RankHelper[developments];
        for(int d=0; d<developments; d++)
            helpers[d] = new RankHelper(d, source);
        return helpers;
    }
    
    static class RankHelper {
        private final int development;
        private int n;
        private int t;
        
        RankHelper(int development, Triangle source) {
            this.development = development;
            calculate(source);
        }
        
        private void calculate(Triangle source) {
            int[] ri = getRanks(createRi(source));
            int[] si = getRanks(createSi(source, ri.length));
            calculate(si, ri);
        }
        
        private Double[] createRi(Triangle source) {
            int dPlus1 = development+1;
            int size = TriangleUtil.getAccidentCount(source, dPlus1);
            Double[] values = new Double[size];
            for(int a=0; a<size; a++)
                values[a] = source.getValue(a, dPlus1);
            return values;
        }
        
        private int[] getRanks(Double[] values) {
            int size = values.length;
            Double[] copy = new Double[size];
            System.arraycopy(values, 0, copy, 0, size);
            Arrays.sort(copy, RANK_COMPARATOR);
            
            int[] ranks = new int[size];
            for(int i=0; i<size; i++) {
                Double value = values[i];
                ranks[i] = (value==null || Double.isNaN(value))? -1 : (1+Arrays.binarySearch(copy, value));
            }
            return ranks;
        }
        
        private Double[] createSi(Triangle source, int size) {
            Double[] si = new Double[size];
            for(int a=0; a<size; a++)
                si[a] = source.getValue(a, development);
            return si;
        }
        
        private void calculate(int[] si, int[] ri) {
            n=0;
            t=0;
            int size = si.length;
            for(int i=0; i<size; i++) {
                int s = si[i];
                int r = ri[i];
                if(r>0 && s>0) {
                    n++;
                    t+= ((r-s) * (r-s));
                }
            }
        }
        
        boolean shouldUse() {
            return n>1;
        }
        
        double getT() {
            return n<2?
                    Double.NaN : 
                    1d - 6d*((double)t)/((double)(n*(n*n-1)));
        }
        
        int getN() {
            return n;
        }
    }
}
