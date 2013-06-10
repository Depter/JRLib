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
package org.jreserve.jrlib.bootstrap.odp;

import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.AbstractChangeable;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.OdpScaledResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.bootstrap.residualgenerator.DoubleResidualGenerator;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpPseudoClaimTriangle 
    extends AbstractChangeable
    implements ClaimTriangle {
    
    private DoubleResidualGenerator<OdpScaledResidualTriangle> residuals;
    private int accidents;
    private int developments;
    private double[][] fitted;
    private double[][] pseudoValues;
    private double[] scales;
    
    public OdpPseudoClaimTriangle(Random rnd, OdpScaledResidualTriangle residuals) {
        this(rnd, residuals, Collections.EMPTY_LIST);
    }
    
    public OdpPseudoClaimTriangle(Random rnd, OdpScaledResidualTriangle residuals, List<int[][]> segments) {
        this.residuals = new DoubleResidualGenerator<OdpScaledResidualTriangle>(rnd, residuals, segments);
        initData(residuals);
        initScales(residuals.getSourceOdpResidualScales());
    }

    private void initData(OdpScaledResidualTriangle rt) {
        this.fitted = rt.toArrayFittedValues();
        this.pseudoValues = TriangleUtil.copy(fitted);
        this.accidents = rt.getSourceTriangle().getAccidentCount();
        this.developments = rt.getSourceTriangle().getDevelopmentCount();
    }
    
    private void initScales(OdpResidualScale scales) {
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = scales.getValue(d);
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
        return withinBounds(accident)?
                pseudoValues[accident].length : 
                0;
    }
    
    private boolean withinBounds(int accident) {
        return 0<=accident && accident < accidents;
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                pseudoValues[accident][development] :
                Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               0 <= development && 
               development < pseudoValues[accident].length;
    }

    @Override
    public double[][] toArray() {
        return TriangleUtil.copy(pseudoValues);
    }
    
    @Override
    protected void recalculateLayer() {
        for(int a=0; a<accidents; a++) {
            int devs = fitted[a].length;
            for(int d=0; d<devs; d++)
                pseudoValues[a][d] = recalculatePseudoValue(a, d);
        }
        TriangleUtil.cummulate(pseudoValues);
    }
    
    @Override
    protected CalculationState getSourceState() {
        return CalculationState.VALID;
    }
    
    private double recalculatePseudoValue(int accident, int development) {
        double f = fitted[accident][development];
        double s = scales[development];
        double r = residuals.getValue(accident, development);
        return f + r * Math.sqrt(f * s);
    }

    
    @Override
    public void detach() {
    }
    
    @Override
    public void detach(CalculationData source) {
    }
}
