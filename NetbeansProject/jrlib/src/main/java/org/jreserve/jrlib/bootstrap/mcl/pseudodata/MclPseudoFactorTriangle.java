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

import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoFactorTriangle 
    extends AbstractTriangle<ClaimTriangle> 
    implements FactorTriangle {
    
    static MclPseudoFactorTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoFactorTriangle(bundle, true);
    }
    
    static MclPseudoFactorTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoFactorTriangle(bundle, false);
    }
    
    private boolean isPaid;
    
    private int accidents;
    private int developments;
    private double[][] pseudoValues;
    private double[][] wik;
    private double[] lrs;
    private double[] scales;
    
    private MclPseudoFactorTriangle(MclResidualBundle bundle, boolean isPaid) {
        this.isPaid = isPaid;
        source = getSourceResiduals(bundle).getSourceTriangle();
        initState(bundle);
        super.setState(CalculationState.INVALID);
    }

    private void initState(MclResidualBundle bundle) {
        LinkRatio linkRatios = getLinkRatio(bundle);
        initFactors(linkRatios);
        initWeights(linkRatios);
        initLinkRatios(linkRatios);
        initScales(bundle);
    }
    
    private LinkRatio getLinkRatio(MclResidualBundle bundle) {
        return getSourceResiduals(bundle).getSourceLinkRatios();
    }
    
    private LRResidualTriangle getSourceResiduals(MclResidualBundle bundle) {
        return isPaid? 
                bundle.getSourcePaidLRResidualTriangle() :
                bundle.getSourceIncurredLRResidualTriangle();
    }
    
    private void initFactors(LinkRatio linkRatios) {
        FactorTriangle original = linkRatios.getSourceFactors();
        accidents = original.getAccidentCount();
        developments = original.getDevelopmentCount();
        pseudoValues = new double[accidents][];
        for(int a=0; a<accidents; a++)
            pseudoValues[a] = new double[original.getDevelopmentCount(a)];
    }
    
    private void initWeights(LinkRatio lrs) {
        wik = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length;
            wik[a] = new double[devs];
            for(int d=0; d<devs; d++)
                wik[a][d] = Math.sqrt(lrs.getWeight(a, d));
        }
    }
    
    private void initLinkRatios(LinkRatio lrs) {
        this.lrs = new double[developments];
        for(int d=0; d<developments; d++)
            this.lrs[d] = lrs.getValue(d);
    }
    
    private void initScales(MclResidualBundle bundle) {
        LinkRatioScale sigma = getLinkRatioScales(bundle);
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = sigma.getValue(d);
    }
    
    private LinkRatioScale getLinkRatioScales(MclResidualBundle bundle) {
        return getSourceResiduals(bundle).getSourceLinkRatioScales();
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
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

    @Override
    protected boolean withinBounds(int accident) {
        return 0<=accident && accident<accidents;
    }

    @Override
    public void recalculate() {
    }
    
    @Override
    public void recalculateLayer() {
    }
    
    @Override
    protected void setState(CalculationState state) {
        super.setState(state);
    }
    
    void setValueAt(int accident, int development, MclResidualCell cell) {
        if(withinBounds(accident, development)) {
            double r = getResidualFromCell(cell);
            double l = lrs[development];
            double s = scales[development];
            double w = wik[accident][development];
            pseudoValues[accident][development] = l + r * s / w;
        }
    }
    
    private double getResidualFromCell(MclResidualCell cell) {
        if(cell == null)
            return Double.NaN;
        else if(isPaid)
            return cell.getPaidLRResidual();
        else
            return cell.getIncurredLRResidual();
    }
    
    
}
