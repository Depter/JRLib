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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.NormalUtil;

/**
 * Performs the test for calendar year effects. The basic idea of the
 * test is to divide each columns in a triangle of development factors
 * to a group containing elements smaller then the median of the column 
 * (mark this elements with `S`) and one that contains the elements 
 * larger then the median (mark this elements with `L`). If there is no 
 * calendar year effect, then the diagonals of the triangle should 
 * contain roughly the same number of `L` and `S` elements.
 * 
 * @see "Mack[1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public class CalendarEffectTest extends AbstractCalculationData<FactorTriangle> implements Test {
    
    public final static double DEFAULT_ALPHA = 0.05;
    
    private final static int S = 0;
    private final static int L = 1;
    private final static int STAR = 2;
    private final static int NaN = 3;
    
    private double testValue;
    private double lowerBound;
    private double upperBound;
    private double alpha;
    private double pValue;
    
    /**
     * Initialises the test with the {@link #DEFAULT_ALPHA default}
     * value of 0.05.
     * 
     * @throws NullPointerException if `triangle` is null.
     */
    public CalendarEffectTest(ClaimTriangle triangle) {
        this(triangle, DEFAULT_ALPHA);
    }
    
    /**
     * Initialises the test with the given alpha.
     * 
     * @throws NullPointerException if `triangle` is null.
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public CalendarEffectTest(ClaimTriangle triangle, double alpha) {
        this(new DevelopmentFactors(triangle), alpha);
    }
    
    /**
     * Initialises the test with the {@link #DEFAULT_ALPHA default}
     * value of 0.05.
     * 
     * @throws NullPointerException if `factors` is null.
     */
    public CalendarEffectTest(FactorTriangle factors) {
        this(factors, DEFAULT_ALPHA);
    }
    
    /**
     * Initialises the test with the given alpha.
     * 
     * @throws NullPointerException if `triangle` is null.
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public CalendarEffectTest(FactorTriangle factors, double alpha) {
        super(factors);
        checkAlpha(alpha);
        this.alpha = alpha;
        doRecalculate();
    }
    
    /**
     * Retunrs true when there is no significant calendar year effect.
     */
    @Override
    public boolean isTestPassed() {
        return (!Double.isNaN(pValue)) && pValue >= alpha;
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
        return upperBound;
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
     * accepted.
     * 
     * Calling this method fires a change event.
     * 
     * @throws IllegalArgumentException if `alpha` is not in [0;1].
     */
    public void setAlpha(double alpha) {
        checkAlpha(alpha);
        this.alpha = alpha;
        fireChange();
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
        int[][] sl = getSL();
        boolean hasValue = false;
        testValue = 0d;
        double ez = 0d;
        double sigma = 0d;
        
        int diagonals = source.getDevelopmentCount();
        for(int d=0; d<diagonals; d++) {
            DiagonalHelper helper = new DiagonalHelper(sl, d, source);
            if(helper.shouldUse()) {
                hasValue = true;
                testValue += helper.getZ();
                ez += helper.getEZ();
                sigma += helper.getVarZ();
            }
        }
        
        if(hasValue) {
            sigma = Math.sqrt(sigma);
            double bound = NormalUtil.invNormCDF(alpha/2d) * sigma;
            lowerBound = ez + bound;
            upperBound = ez - bound;
            double norm = NormalUtil.normCDF((testValue - ez) / sigma);
            pValue = (testValue<ez)? 2d * norm : 2d * (1d - norm);
        } else {
            testValue = Double.NaN;
            lowerBound = Double.NaN;
            upperBound = Double.NaN;
            pValue = Double.NaN;
        }
    }
    
    private int[][] getSL() {
        double[] medians = getMedians();
        int accidents = source.getAccidentCount();
        int[][] sl = new int[accidents][];
        
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            sl[a] = new int[devs];
            for(int d=0; d<devs; d++) {
                double value = source.getValue(a, d);
                double median = medians[d];
                if(Double.isNaN(value) || Double.isNaN(median))
                    sl[a][d] = NaN;
                else if(value==median)
                    sl[a][d] = STAR;
                else
                    sl[a][d] = (value<median)? S : L;
            }
        }
        return sl;
    }
    
    private double[] getMedians() {
        int developments = source.getDevelopmentCount();
        double[] medians = new double[developments];
        for(int d=0; d<developments; d++)
            medians[d] = getMedian(d);
        return medians;
    }
    
    private double getMedian(int development) {
        List<Double> values = getValuesForDevelopment(development);
        int size = values.size();
        if(size == 0)
            return Double.NaN;
        return (size % 2 == 0)?
                (values.get(size/2)+values.get(size/2-1))/2d :
                values.get((size)/2);
    }
    
    private List<Double> getValuesForDevelopment(int development) {
        int accidents = source.getAccidentCount();
        List<Double> values = new ArrayList<Double>(accidents);
        for(int a=0; a<accidents; a++) {
            double value = source.getValue(a, development);
            if(!Double.isNaN(value))
                values.add(value);
        }
        Collections.sort(values);
        return values;
    }
    
    static class DiagonalHelper {
    
        private int[][] sl;
        private int diagonal;
        private FactorTriangle source;
        
        private double eZ;
        private double varZ;
        private int z;
        
        DiagonalHelper(int[][] sl, int diagonal, FactorTriangle source) {
            this.sl = sl;
            this.diagonal = diagonal;
            this.source = source;
            calculate();
        }
        
        private void calculate() {
            int[] values = getDiagonal();
            calculate(values);
        }
        
        private int[] getDiagonal() {
            int accidents = source.getAccidentCount();
            int[] values = new int[accidents];
            for(int a=0; a<accidents; a++)
                values[a] = getDiagonalValue(a);
            return values;
        }
        
        private int getDiagonalValue(int accident) {
            int corr = source.getDevelopmentCount(accident) - source.getDevelopmentCount();
            int development = diagonal + corr;
            if(development < 0)
                return NaN;
            return sl[accident][development];
        }
        
        private void calculate(int[] values) {
            int s = 0;
            int l = 0;
            for(int i : values) {
                if(i == S) s++;
                if(i == L) l++;
            }
            calculate(s, l);
        }
        
        private void calculate(int s, int l) {
            z = (s<l)? s : l;
            int n = s+l;
            if(n < 2) {
                eZ = Double.NaN;
                varZ = Double.NaN;
            } else {
                int m = (n>0)? (n-1)/2 : 0;
                double nm = nm(n-1, m);
                double dn = (double) n;
                double twoPn = (Math.pow(2d, dn));
                eZ = dn / 2d - nm * dn / twoPn;
                double dd = dn * (dn - 1d);
                varZ = dd / 4d - nm * dd / twoPn + eZ - eZ * eZ;
            }
        }
        
        private double nm(int n, int k) {
            double result = fact(n) / fact(n-k);
            return result / fact(k);
        }
        
        private double fact(int value) {
            double f = 1d;
            for(int i=1; i<=value; i++)
                f *= (double)i;
            return f;
        }
        
        boolean shouldUse() {
            return !Double.isNaN(eZ);
        }
        
        double getEZ() {
            return eZ;
        }
        
        double getVarZ() {
            return varZ;
        }
        
        double getZ() {
            return (double)z;
        }
    }
}
