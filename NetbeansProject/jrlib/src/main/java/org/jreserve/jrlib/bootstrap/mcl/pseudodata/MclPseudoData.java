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

import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.util.random.Random;

/**
 * Pseudo data generator for MCL-Bootstrapping. The class generates parallel
 * residuals for all four triangles:
 * <ul>
 *   <li>Paid development factors</li>
 *   <li>Incurred/Paid ratios</li>
 *   <li>Incurred development factors</li>
 *   <li>Paid/Incurred ratios</li>
 * </ul>
 * 
 * The MCL-bootsrap method extends the Mack-bootstrap method, by including
 * the Paid/Incurred and Incurred/Paid ratios. In order to preserve
 * the correlation between paid and incurred claims, the residuals of the
 * four residual triangle (link-ratios and claim-ratios) are linked together.
 * This means that if cell (1,1) in the paid link-ratio triangle got
 * it's residual from cell(2,3), the all other residuals will get their
 * residuals from the same cell.
 * 
 * @see "Liu, Verrall [2010]: Bootstrap Estimation of the Predictive Distributions of Reserves Using Paid and Incurred Claims, Variance 4:2, 2010, pp. 121-135."
 * @author Peter Decsi
 * @version 1.0
 */
public class MclPseudoData extends AbstractCalculationData<CalculationData> {
    
    private int accidents;
    private int[] developments;
    
    private MclResidualBundle bundle;
    private MclResidualGenerator residuals;
    
    private MclPseudoFactorTriangle paidFactors;
    private MclPseudoFactorTriangle incurredFactors;
    private MclPseudoRatioTriangle paidRatios;
    private MclPseudoRatioTriangle incurredRatios;
    private PseudoLambdaCalculator lambdas = new PseudoLambdaCalculator();
    
    private MclPseudoClaimRatio paidCr;
    private MclPseudoClaimRatio incurredCr;
    
    private MclPseudoClaimRatioScale paidCrScale;
    private MclPseudoClaimRatioScale incurredCrScale;
    private LinkRatioScale paidLrScale;
    private LinkRatioScale incurredLrScale;
    
    public MclPseudoData(Random rnd, MclResidualBundle bundle) {
        this(rnd, bundle, Collections.EMPTY_LIST);
    }

    public MclPseudoData(Random rnd, MclResidualBundle bundle, List<int[][]> segments) {
        this.bundle = bundle;
        residuals = new MclResidualGenerator(rnd, bundle, segments);
        initialise();
    }
    
    private void initialise() {
        initBounds();
        initPseudoData();
        linkData();
    }
    
    private void initBounds() {
        this.accidents = bundle.getAccidentCount();
        developments = new int[accidents];
        for(int a=0; a<accidents; a++)
            developments[a] = bundle.getDevelopmentCount(a);
    }
    
    private void initPseudoData() {
        paidFactors = MclPseudoFactorTriangle.createPaid(bundle);
        incurredFactors = MclPseudoFactorTriangle.createIncurred(bundle);
        paidRatios = MclPseudoRatioTriangle.createPaid(bundle);
        incurredRatios = MclPseudoRatioTriangle.createIncurred(bundle);
        
        paidCr = MclPseudoClaimRatio.createPaid(bundle);
        incurredCr = MclPseudoClaimRatio.createIncurred(bundle);
        
        paidCrScale = MclPseudoClaimRatioScale.createPaid(bundle);
        incurredCrScale = MclPseudoClaimRatioScale.createIncurred(bundle);
        
        paidLrScale = bundle.getSourcePaidLRResidualTriangle().getSourceLinkRatioScales();
        incurredLrScale = bundle.getSourceIncurredLRResidualTriangle().getSourceLinkRatioScales();
    }
    
    public int getAccidentCount() {
        return accidents;
    }
    
    private void linkData() {
        paidLrScale.getSourceLinkRatios().setSource(paidFactors);
        incurredLrScale.getSourceLinkRatios().setSource(incurredFactors);
        paidCr.setSource(paidRatios);
        incurredCr.setSource(incurredRatios);
        paidCrScale.getSourceInput().setSource(paidCr);
        incurredCrScale.getSourceInput().setSource(incurredCr);
    }
    
    public FactorTriangle getPaidFactorTriangle() {
        return paidFactors;
    }
    
    public RatioTriangle getPaidRatioTriangle() {
        return paidRatios;
    }
    
    public FactorTriangle getIncurredFactorTriangle() {
        return incurredFactors;
    }
    
    public RatioTriangle getIncurredRatioTriangle() {
        return incurredRatios;
    }
    
    public ClaimRatioScale getPaidClaimRatioScale() {
        return paidCrScale;
    }
    
    public ClaimRatioScale getIncurredClaimRatioScale() {
        return incurredCrScale;
    }
    
    public LinkRatioScale getPaidLinkRatioScale() {
        return paidLrScale;
    }
    
    public LinkRatioScale getIncurredLinkRatioScale() {
        return incurredLrScale;
    }
    
    public double getPaidLambda() {
        return lambdas.getPaidLambda();
    }
    
    public double getIncurredLambda() {
        return lambdas.getIncurredLambda();
    }
    
    @Override
    protected void recalculateLayer() {
        lambdas.clearValues();
        setStates(CalculationState.INVALID);
        recalculateCells();
        recalculateScales();
        setStates(CalculationState.VALID);
        lambdas.finnishCalculation();
    }
    
    private void setStates(CalculationState state) {
        paidFactors.setState(state);
        incurredFactors.setState(state);
        paidRatios.setState(state);
        incurredRatios.setState(state);
    }
    
    private void recalculateCells() {
        for(int a=0; a<accidents; a++) {
            int devs = developments[a];
            for(int d=0; d<devs; d++)
                recalculateCell(a, d);
        }
    }
    
    private void recalculateCell(int accident, int development) {
        MclResidualCell cell = residuals.getCell(accident, development);
        paidFactors.setValueAt(accident, development, cell);
        paidRatios.setValueAt(accident, development, cell);
        incurredFactors.setValueAt(accident, development, cell);
        incurredRatios.setValueAt(accident, development, cell);
        lambdas.processCell(cell);
    }
    
    private void recalculateScales() {
        paidLrScale.recalculate();
        incurredLrScale.recalculate();
        paidCrScale.recalculate();
        incurredCrScale.recalculate();
    }
    
    private class PseudoLambdaCalculator {
        private double spLrCr = 0d; //sum(lr(p) * i/p)
        private double spCrCr = 0d; //sum(i/p ^2)
        private double siLrCr = 0d; //sum(lr(i) * p/i)
        private double siCrCr = 0d; //sum(p/i ^2)
        private double lambdaP = 0d;
        private double lambdaI = 0d;
        
        void clearValues() {
            spLrCr = 0d;
            spCrCr = 0d;
            siLrCr = 0d;
            siCrCr = 0d;
            lambdaP = 0d;
            lambdaI = 0d;
        }
        
        void processCell(MclResidualCell cell) {
            if(cell == null)
                return;
            double pLr = cell.getPaidLRResidual();
            double pCr = cell.getPaidCRResidual();
            double iLr = cell.getIncurredLRResidual();
            double iCr = cell.getIncurredCRResidual();
            
            if(canProcessValues(pLr, pCr, iLr, iCr)) {
                spLrCr += pLr * pCr;
                spCrCr += pCr * pCr;
                siLrCr += iLr * iCr;
                siCrCr += iCr * iCr;
            }
        }
        
        private boolean canProcessValues(double pLr, double pCr, double iLr, double iCr) {
            return !Double.isNaN(pLr) && !Double.isNaN(pCr) &&
                   !Double.isNaN(iLr) && !Double.isNaN(iCr);
        }
        
        void finnishCalculation() {
            lambdaP = spLrCr / spCrCr;
            lambdaI = siLrCr / siCrCr;
        }
        
        double getPaidLambda() {
            return lambdaP;
        }
        
        double getIncurredLambda() {
            return lambdaI;
        }
    }
}
