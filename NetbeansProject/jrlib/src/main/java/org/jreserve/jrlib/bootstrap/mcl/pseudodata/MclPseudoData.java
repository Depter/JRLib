package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.estimate.mcl.MclCorrelationInput;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
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
    
    private MclResidualGenerator residuals;
    
    private MclPseudoLRResidualTriangle incurredLRRes;
    private MclPseudoLRResidualTriangle paidLRRes;
    private MclPseudoCRResidualTriangle incurredCRRes;
    private MclPseudoCRResidualTriangle paidCRRes;
    
    private MclPseudoFactorTriangle paidFactors;
    private MclPseudoFactorTriangle incurredFactors;
    private MclPseudoRatioTriangle paidRatios;
    private MclPseudoRatioTriangle incurredRatios;

    public MclPseudoData(Random rnd, MclResidualBundle bundle) {
        this(rnd, bundle, Collections.EMPTY_LIST);
    }

    public MclPseudoData(Random rnd, MclResidualBundle bundle, List<int[][]> segments) {
        initBounds(bundle);
        initPseudoTriangles(bundle);
        residuals = new MclResidualGenerator(rnd, bundle, segments);
    }
    
    private void initBounds(MclResidualBundle bundle) {
        this.accidents = bundle.getAccidentCount();
        developments = new int[accidents];
        for(int a=0; a<accidents; a++)
            developments[a] = bundle.getDevelopmentCount(a);
    }
    
    private void initPseudoTriangles(MclResidualBundle bundle) {
        paidLRRes = MclPseudoLRResidualTriangle.createPaid(bundle);
        incurredLRRes = MclPseudoLRResidualTriangle.createIncurred(bundle);
        paidCRRes = MclPseudoCRResidualTriangle.createPaid(bundle);
        incurredCRRes = MclPseudoCRResidualTriangle.createIncurred(bundle);
        
        
        paidFactors = MclPseudoFactorTriangle.createPaid(bundle);
        incurredFactors = MclPseudoFactorTriangle.createIncurred(bundle);
        paidRatios = MclPseudoRatioTriangle.createPaid(bundle);
        incurredRatios = MclPseudoRatioTriangle.createIncurred(bundle);
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
    
    public LRResidualTriangle getPaidLRResidualTriangle() {
        return paidLRRes;
    }
    
    public LRResidualTriangle getIncurredLRResidualTriangle() {
        return incurredLRRes;
    }
    
    public CRResidualTriangle getPaidCRResidualTriangle() {
        return paidCRRes;
    }
    
    public CRResidualTriangle getIncurredCRResidualTriangle() {
        return incurredCRRes;
    }
    
    @Override
    protected void recalculateLayer() {
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
        
        paidLRRes.setValueAt(accident, development, cell);
        incurredLRRes.setValueAt(accident, development, cell);
        paidCRRes.setValueAt(accident, development, cell);
        incurredCRRes.setValueAt(accident, development, cell);
    }
    
    public MclCalculationBundle createPseudoBundle(MclCalculationBundle bundle) {
        MclCorrelationInput paid = bundle.getSourcePaidCorrelation().getSourceInput();
        paid.getSourceLinkRatios().setSource(paidFactors);
        paid.getSourceClaimRatios().setSource(paidRatios);
        
        MclCorrelationInput incurred = bundle.getSourceIncurredCorrelation().getSourceInput();
        incurred.getSourceLinkRatios().setSource(incurredFactors);
        incurred.getSourceClaimRatios().setSource(incurredRatios);
        
        MclCorrelation paidC = new MclCorrelation(paidLRRes, paidCRRes);
        MclCorrelation incurredC = new MclCorrelation(incurredLRRes, incurredCRRes);
        return new MclCalculationBundle(paidC, incurredC);
    }
    
    
}
