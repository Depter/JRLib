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
package org.jreserve.jrlib.test;

import java.util.Arrays;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.NormalUtil;

/**
 * Performs the test for uncorrelated development factors. The basic 
 * idea of the test is to perform a rank correlation amoung the
 * columns of a {@link FactorTriangle FactorTriangle}.
 * 
 * @see "Mack[1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public class UncorrelatedDevelopmentFactorsTest extends AbstractCalculationData<FactorTriangle> implements Test {
    
    private final static double DEFAULT_ALPHA = 0.5;
    
    private double testValue;
    private double lowerBound;
    private double alpha;
    private double pValue;
    
    /**
     * Initialises the test with the {@link #DEFAULT_ALPHA default}
     * value of 0.5 (for more details about the high alpha, see
     * the article of Mack).
     * 
     * @throws NullPointerException if `triangle` is null.
     */
    public UncorrelatedDevelopmentFactorsTest(ClaimTriangle claims) {
        this(claims, DEFAULT_ALPHA);
    }
    
    /**
     * Initialises the test with the given alpha. Remember that
     * Mack suggest an alpha of 0.5 for this test.
     * 
     * @throws NullPointerException if `triangle` is null.
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public UncorrelatedDevelopmentFactorsTest(ClaimTriangle claims, double alpha) {
        this(new DevelopmentFactors(claims), alpha);
    }
    
    /**
     * Initialises the test with the {@link #DEFAULT_ALPHA default}
     * value of 0.5 (for more details about the high alpha, see
     * the article of Mack).
     * 
     * @throws NullPointerException if `factors` is null.
     */
    public UncorrelatedDevelopmentFactorsTest(FactorTriangle factors) {
        this(factors, DEFAULT_ALPHA);
    }
    
    /**
     * Initialises the test with the given alpha. Remember that
     * Mack suggest an alpha of 0.5 for this test.
     * 
     * @throws NullPointerException if `factors` is null.
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public UncorrelatedDevelopmentFactorsTest(FactorTriangle factors, double alpha) {
        super(factors);
        checkAlpha(alpha);
        this.alpha = alpha;
        doRecalculate();
    }
    
    /**
     * Retunrs true when there is no significant correlation amoung
     * subsequent development factors.
     */
    @Override
    public boolean isTestPassed() {
        return !Double.isNaN(pValue) && pValue >= alpha;
    }
    
    /**
     * Returns the test value.
     */
    @Override
    public double getTestValue() {
        return testValue;
    }
    
    /**
     * Returns the lower-bound for the test value.
     */
    public double getLowerBound() {
        return lowerBound;
    }
    
    /**
     * Returns the upper-bound for the test value.
     */
    public double getUpperBound() {
        return -lowerBound;
    }
    
    /**
     * Returns the alpha used to check if the test value 
     * accepted.
     */
    @Override
    public double getAlpha() {
        return alpha;
    }
    
    /**
     * Sets the alpha used to check if the test value 
     * accepted. Remember that Mack suggests an alpha
     * of 0.5 for this test.
     * 
     * Calling this method fires a change event.
     * 
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public void setAlpha(double alpha) {
        setState(CalculationState.INVALID);
        checkAlpha(alpha);
        this.alpha = alpha;
        setState(CalculationState.VALID);
    }
    
    private void checkAlpha(double alpha) {
        if(alpha < 0d || alpha>1d)
            throw new IllegalArgumentException("Alpha must be within [0;1], but it was "+alpha+"!");
    }
    
    /**
     * Returns the p-value for the test.
     */
    @Override
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
        double norm = NormalUtil.normCDF(testValue / testSigma);
        pValue = (testValue<0d)?
                2d * norm :
                2d * (1d - norm);
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
        
        RankHelper(int development, FactorTriangle source) {
            this.development = development;
            calculate(source);
        }
        
        private void calculate(FactorTriangle source) {
            int[] ri = getRanks(createRi(source));
            int[] si = getRanks(createSi(source, ri.length));
            calculate(si, ri);
        }
        
        private Double[] createRi(FactorTriangle source) {
            int dPlus1 = development+1;
            int size = TriangleUtil.getAccidentCount(source, dPlus1);
            Double[] values = new Double[size];
            for(int a=0; a<size; a++)
                values[a] = source.getValue(a, dPlus1);
            return values;
        }
        
        private int[] getRanks(Double[] values) {
            int size = values.length;
            Double[] sorted = new Double[size];
            System.arraycopy(values, 0, sorted, 0, size);
            Arrays.sort(sorted, DoubleComparator.INSTANCE);
            
            int[] ranks = new int[size];
            for(int i=0; i<size; i++) {
                Double value = values[i];
                ranks[i] = (value==null || Double.isNaN(value))? -1 : (1+getRank(sorted, value, size));
            }
            return ranks;
        }
        
        private int getRank(Double[] values, Double value, int size) {
            for(int i=0; i<size; i++)
                if(value.equals(values[i]))
                    return i;
            return -1;
        }
        
        private Double[] createSi(FactorTriangle source, int size) {
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
