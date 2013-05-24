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

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoRatioTriangle extends AbstractTriangle<RatioTriangleInput> implements RatioTriangle {
    
    static MclPseudoRatioTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoRatioTriangle(bundle, true);
    }
    
    static MclPseudoRatioTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoRatioTriangle(bundle, false);
    }
    
    private boolean isPaid;
    private int accidents;
    private int developments;
    private double[][] pseudoValues;
    private double[][] wik;
    private double[] crs;
    private double[] scales;

    private MclPseudoRatioTriangle(MclResidualBundle bundle, boolean isPaid) {
        this.isPaid = isPaid;
        initState(bundle);
        detach();
        setCallsForwarded(false);
    }
    
    private void initState(MclResidualBundle bundle) {
        CRResidualTriangle res = getSourceResidualss(bundle);
        initSource(res);
        initRatios(res.getSourceRatioTriangle());
        initWeights(res.getSourceDenominatorTriangle());
        initClaimRatios(res.getSourceClaimRatios());
        initScales(res.getSourceClaimRatioScales());
    }
    
    private CRResidualTriangle getSourceResidualss(MclResidualBundle bundle) {
        return isPaid? 
                bundle.getSourcePaidCRResidualTriangle() :
                bundle.getSourceIncurredCRResidualTriangle();
    }
    
    private void initSource(CRResidualTriangle res) {
        ClaimTriangle n = res.getSourceNumeratorTriangle();
        ClaimTriangle d = res.getSourceDenominatorTriangle();
        this.source = new RatioTriangleInput(n, d);
        this.source.detach();
    }
    
    private void initRatios(RatioTriangle ratios) {
        accidents = ratios.getAccidentCount();
        developments = ratios.getDevelopmentCount();
        pseudoValues = ratios.toArray();
        excludeLastDiagonal();
    }
    
    private void excludeLastDiagonal() {
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length - 1;
            if(devs >= 0)
                pseudoValues[a][devs] = Double.NaN;
        }
    }
    
    private void initWeights(ClaimTriangle cik) {
        wik = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length;
            wik[a] = new double[devs];
            for(int d=0; d<devs; d++)
                wik[a][d] = Math.sqrt(cik.getValue(a, d));
        }
    }
    
    private void initClaimRatios(ClaimRatio ratios) {
        this.crs = new double[developments];
        for(int d=0; d<developments; d++)
            this.crs[d] = ratios.getValue(d);
    }
    
    private void initScales(ClaimRatioScale rho) {
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = rho.getValue(d);
    }
    
    @Override
    public RatioTriangleInput getSourceInput() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }

    @Override
    public void setSource(RatioTriangleInput source) {
        throw new UnsupportedOperationException("Can not set source on a pseudo triangle!");
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
        return withinBounds(accident)? pseudoValues[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                pseudoValues[accident][development] : 
                Double.NaN;
    }
    
    void setValueAt(int accident, int development, MclResidualCell cell) {
        double r = getResidualFromCell(cell);
        double cr = crs[development];
        double s = scales[development];
        double w = wik[accident][development];
        pseudoValues[accident][development] = cr + r * s / w;
    }
    
    private double getResidualFromCell(MclResidualCell cell) {
        if(cell == null)
            return Double.NaN;
        else if(isPaid)
            return cell.getPaidCRResidual();
        else
            return cell.getIncurredCRResidual();
    }

    @Override
    public void recalculate() {
    }
    
    @Override
    public void recalculateLayer() {
    }
}
