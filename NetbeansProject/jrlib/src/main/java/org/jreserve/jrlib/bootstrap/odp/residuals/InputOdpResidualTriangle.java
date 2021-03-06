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
package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * InputOdpResidualTriangle calculates the adjusted pearson residuals for
 * a given set of link-ratios. The formula to calculate the residual
 * for accident period `a` and development period `d` is:
 * <pre>
 *               o(a,d) - f(a,d)
 *     r(a,d) =  ---------------
 *                 f(a,d)^0.5
 * </pre>
 * where:
 * <ul>
 *   <li>`o(a,d)` is the original incremental value.</li>
 *   <li>`f(a,d)` is teh fitted incremental value.</li>
 * </ul>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InputOdpResidualTriangle extends AbstractTriangle<LinkRatio> implements OdpResidualTriangle {
    
    private final static double EPSILON = 1E-10;
    
    private ClaimTriangle cik;
    private int accidents;
    private int developments;
    private double[][] values;
    private double[][] fitted;
    
    /**
     * Creates an instance for the given source.
     */
    public InputOdpResidualTriangle(LinkRatio source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? values[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)? 
                values[accident][development] :
                Double.NaN;
    }

    @Override
    protected boolean withinBounds(int accident) {
        return 0<=accident && accident<accidents;
    }

    @Override
    public double getFittedValue(int accident, int development) {
        return withinBounds(accident, development)?
                fitted[accident][development] :
                Double.NaN;
    }
    
    @Override
    public double[][] toArrayFittedValues() {
        return TriangleUtil.copy(fitted);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        recalculateBounds();
        calculateFittedValues();
        TriangleUtil.deCummulate(values);
        calculateResiduals(fitted);
    }
    
    private void recalculateBounds() {
        cik = source.getSourceTriangle();
        accidents = cik.getAccidentCount();
        developments = cik.getDevelopmentCount();
        values = cik.toArray();
    }
    
    private void calculateFittedValues() {
        fitted = TriangleUtil.copy(values);
        for(int a=0; a<accidents; a++) {
            int devs = values[a].length - 2;
            for(int d=devs; d>=0; d--)
                fitted[a][d] = fitted[a][d+1]/source.getValue(d);
        }
        TriangleUtil.deCummulate(fitted);
    }
    
    private void calculateResiduals(double[][] fitted) {
        for(int a=0; a<accidents; a++) {
            int devs = values[a].length;
            for(int d=0; d<devs; d++) 
                values[a][d] = calculateResidual(values[a][d], fitted[a][d]);
        }
        escapeTopRightCorner();
    }
    
    private double calculateResidual(double original, double fitted) {
        if(Double.isNaN(original) || Double.isNaN(fitted) || fitted <= 0d)
            return Double.NaN;
        double diff = (original - fitted);
        return (-EPSILON<diff &&  diff<EPSILON)? 0d : diff/Math.sqrt(fitted);
    }
    
    /**
     * The top-right corner of the residuals is based only on one development
     * factor. this means that the residuals shoudl be 0. In some cases,
     * thanks to floating point operation, this is not the case, so we
     * are setting the values manually to 0.
     */
    private void escapeTopRightCorner() {
        int firstDev = (accidents==1)? 0 : values[1].length;
        for(int d=firstDev; d<developments; d++)
            values[0][d] = 0d;
    }
}
