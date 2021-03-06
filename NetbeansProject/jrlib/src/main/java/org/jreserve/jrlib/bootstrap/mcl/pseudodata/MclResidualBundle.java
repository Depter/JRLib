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
package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * MclResidualBundle bundles together the values of 
 * {@link LRResidualTriangle LRResidualTriangles} and 
 * {@link CRResidualTriangle CRResidualTriangles} for paid and incurred
 * claims. This class is used to generate residuals for the MCL-Bootstrap method.
 * 
 * For each dimension (accident and development counts) this method will
 * use the smallest triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclResidualBundle extends AbstractMultiSourceCalculationData<CalculationData>{
    
    private LRResidualTriangle sourcePaidLr;
    private CRResidualTriangle sourcePaidCr;
    private LRResidualTriangle sourceIncurredLr;
    private CRResidualTriangle sourceIncurredCr;
    
    private int accidents;
    private int developments;
    private int[] accidentDevelopments;
    
    private double[][] paidLr;
    private double[][] paidCr;
    private double[][] incurredLr;
    private double[][] incurredCr;
    
    /**
     * Creates an instance for the given residuals.
     * 
     * @throws NullPointerException if any of the parameters is null.
     */
    public MclResidualBundle(LRResidualTriangle paidLr, CRResidualTriangle paidCr,
            LRResidualTriangle incurredLr, CRResidualTriangle incurredCr) {
        super(paidLr, paidCr, incurredLr, incurredCr);
        this.sourcePaidLr = paidLr;
        this.sourcePaidCr = paidCr;
        this.sourceIncurredLr = incurredLr;
        this.sourceIncurredCr = incurredCr;
        doRecalculate();
    }

    public LRResidualTriangle getSourcePaidLRResidualTriangle() {
        return sourcePaidLr;
    }

    public LRResidualTriangle getSourceIncurredLRResidualTriangle() {
        return sourceIncurredLr;
    }

    public CRResidualTriangle getSourcePaidCRResidualTriangle() {
        return sourcePaidCr;
    }

    public CRResidualTriangle getSourceIncurredCRResidualTriangle() {
        return sourceIncurredCr;
    }
    
    /**
     * Returns the number of accident periods. This is the smallest
     * amoung the input triangles.
     */
    public int getAccidentCount() {
        return accidents;
    }
    
    /**
     * Returns the number of development periods. This is the smallest
     * amoung the input triangles.
     */
    public int getDevelopmentCount() {
        return developments;
    }
    
    /**
     * Returns the number of development periods for the given accident
     * period. This is the smallest amoung the input triangles.
     */
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? accidentDevelopments[accident] : 0;
    }
    
    private boolean withinBounds(int accident) {
        return 0 <= accident && accident < accidents;
    }
    
    /**
     * Retunrs the paid link-ratio residual for the given accident and
     * development period. If `accident` and `development` period
     * falls outside the bounds, than NaN is returned.
     */
    public double getPaidLRResidual(int accident, int development) {
        return getValue(paidLr, accident, development);
    }
    
    private double getValue(double[][] values, int accident, int development) {
        return withinBounds(accident, development)?
                values[accident][development] :
                Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               0 <= development && 
               development < accidentDevelopments[accident];
    }
    
    /**
     * Retunrs the paid claim-ratio residual for the given accident and
     * development period. If `accident` and `development` period
     * falls outside the bounds, than NaN is returned.
     */
    public double getPaidCRResidual(int accident, int development) {
        return getValue(paidCr, accident, development);
    }
    
    /**
     * Retunrs the incurred link-ratio residual for the given accident and
     * development period. If `accident` and `development` period
     * falls outside the bounds, than NaN is returned.
     */
    public double getIncurredLRResidual(int accident, int development) {
        return getValue(incurredLr, accident, development);
    }
    
    /**
     * Retunrs the incurred claim-ratio residual for the given accident and
     * development period. If `accident` and `development` period
     * falls outside the bounds, than NaN is returned.
     */
    public double getIncurredCRResidual(int accident, int development) {
        return getValue(incurredCr, accident, development);
    }
    
    /**
     * Returns wether the given cell should be NaN. A cell should be nan, if
     * it's coordinates fall outside of the bound of this instance, or
     * one of the residuals is NaN.
     */
    public boolean isNaN(int accident, int development) {
        return !withinBounds(accident, development) ||
               Double.isNaN(paidLr[accident][development]) ||
               Double.isNaN(paidCr[accident][development]) ||
               Double.isNaN(incurredLr[accident][development]) ||
               Double.isNaN(incurredCr[accident][development]);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        recalculateDimensions();
        calculateValues();
    }
    
    private void recalculateDimensions() {
        recalculateAccidents();
        recalculateDevelopments();
        recalculateAccidentDevelopments();
    }
    
    private void recalculateAccidents() {
        accidents = min(
                sourcePaidLr.getAccidentCount(),
                sourcePaidCr.getAccidentCount(),
                sourceIncurredLr.getAccidentCount(),
                sourceIncurredCr.getAccidentCount()
                );
    }
    
    private int min(int i1, int i2, int i3, int i4) {
        return Math.min(Math.min(i1, i2), Math.min(i3, i4));
    }
    
    private void recalculateDevelopments() {
        developments = min(
                sourcePaidLr.getDevelopmentCount(),
                sourcePaidCr.getDevelopmentCount(),
                sourceIncurredLr.getDevelopmentCount(),
                sourceIncurredCr.getDevelopmentCount()
                );
    }
    
    private void recalculateAccidentDevelopments() {
        accidentDevelopments = new int[accidents];
        for(int a=0; a<accidents; a++)
            accidentDevelopments[a] = min(
                sourcePaidLr.getDevelopmentCount(a), 
                sourcePaidCr.getDevelopmentCount(a),
                sourceIncurredLr.getDevelopmentCount(a),
                sourceIncurredCr.getDevelopmentCount(a)
            );
    }
    
    private void calculateValues() {
        paidLr = calculateValues(sourcePaidLr);
        paidCr = calculateValues(sourcePaidCr);
        incurredLr = calculateValues(sourceIncurredLr);
        incurredCr = calculateValues(sourceIncurredCr);
    }
    
    private double[][] calculateValues(Triangle triangle) {
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = accidentDevelopments[a];
            result[a] = new double[devs];
            for(int d=0; d<devs; d++)
                result[a][d] = triangle.getValue(a, d);
        }
        return result;
    }
}
